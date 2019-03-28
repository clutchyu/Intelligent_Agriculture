package com.clutch.argriculture;

import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.Pwm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    ///网络编程部分
    ServerSocket serverSocket = null;
    Socket socket = null;//serverSocket.accept();
    InputStream is =null ;// = socket.getInputStream();
    InputStreamReader isr ;//=new InputStreamReader(is);
    BufferedReader br =null;
    //2、调用accept()方法开始监听，等待客户端的连接
    // Socket socket = serverSocket.accept();

    static boolean auto = true;        //控制自动还是手动控制
    /////////////////////////////////////////////////
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String led="BCM26";
    private static final String IN1 = "BCM6";  //窗帘
    private static final String IN2 = "BCM5";
    private static final String IN3 = "BCM12"; //天窗
    private static final String IN4 = "BCM16";
    private static final int INTERVAL_BETWEEN_BLINKS_MS = 1000;
    private Handler mHandler = new Handler();
    private boolean mStatus = false;
    private Gpio led11;
    private Gpio dianjizheng1;
    private Gpio dianjifu1;
    private Gpio tczheng;
    private Gpio tcfu;
    private static final int sensor_addr=0x44; ///温湿度：地址
    private static final int light_addr=0x23;
    private static final int soilhum_addr=0x48;//0x90;///土壤湿度
    private I2cDevice sensor,lightx,soilsensor;
    private float temp,hum;
    private double lx=1;
    private int soilhum;
    private float temp_now[] = new float[10];
    private double light_now[] = new double[10];
    private int soil_now[] = new int[10];

    private boolean flag = true;        //自动为true,手动为false

    Connection con = null;
    Statement sql = null;
    ResultSet rs = null;
    PeripheralManager manager = PeripheralManager.getInstance();

    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            led11=manager.openGpio(led);
            led11.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            dianjizheng1=manager.openGpio(IN1);
            dianjizheng1.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            dianjifu1=manager.openGpio(IN2);
            dianjifu1.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            tczheng = manager.openGpio(IN3);
            tcfu = manager.openGpio(IN4);
            tczheng.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            tcfu.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            //  mHandler.post(mBlinkRunnable);
            new Thread(networkTask).start();
            new Thread(ControlTask).start();
            led11.setValue(true);

        }
        catch (Exception e)
        {
            Log.i("temp","new333");
        }
    }
    Runnable ControlTask = new Runnable() {                     //树莓派手动控制，开关天窗，开关纱帘，开关LED

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("Control1111");
                    serverSocket = new ServerSocket(10086);
                    socket = serverSocket.accept();
                    System.out.println("Control");
                    is = socket.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    int x = br.read();

                    x=x-48;                     //二进制转整数
                    if (x == 1) {//开灯                               //用以模拟灌溉，LED亮代表开始灌溉，LED灭代表停止灌溉
                      /*  led11.setValue(true);
                        Log.i("led_open", "true");
                        serverSocket.close();*/
                    }
                    if (x == 2) {//关灯
                       /* led11.setValue(false);
                        Log.i("led_close", "false");
                        serverSocket.close();*/
                    }
                    if (x == 3) {                               //开关天窗
                        if(flag == false) closeFan();
                        serverSocket.close();
                    }
                    if (x == 4) {
                        if(flag == false) openFan();
                        serverSocket.close();
                    }
                    if (x == 5) {                           //开关纱帘
                        if(flag == false) chuangliankai();
                        serverSocket.close();
                    }
                    if (x == 6) {
                        if(flag == false) chuanglianguan();
                        serverSocket.close();
                    }
                    if (x == 7) {
                        flag = true;
                        led11.setValue(true);
                        Log.i("led_open", "true");
                        serverSocket.close();
                    }
                    if (x == 8) {
                        flag = false;
                        led11.setValue(false);
                        Log.i("led_close", "false");
                        serverSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("control_fail","fail");
                }

            }
        }
    };
    private Runnable mBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            if (led11 == null) {
                return;
            }
            try {
                //  反转 LED  状态
                led11.setValue(mStatus);
                mStatus = !mStatus;
                //  将下一次反转 LED  的事件放到消息队列中，并设定延时为 1s （ 1000ms ）
                mHandler.postDelayed(mBlinkRunnable,
                        INTERVAL_BETWEEN_BLINKS_MS);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    };

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            float humandtemp[] = new float[2];
            //数据库连接
            final String driver = "com.mysql.jdbc.Driver";
            final String uri = "jdbc:mysql://192.168.43.125:3306/intelligent_agriculture";       //远程访问主机数据库，需在数据库中用树莓派IP建立新的用户
            try {
                Class.forName(driver);
                con = DriverManager.getConnection(uri, "wangyu", "wangyu.com123");
                sql = (Statement)con.createStatement();
            } catch (ClassNotFoundException e) {
                Log.i("temp1"," sql error!!!");
                e.printStackTrace();
            } catch (SQLException e) {
                Log.i("temp1"," sql error00!!!");
                e.printStackTrace();
            }
            int i = 0;
            float temp_average = 0;
            double light_average = 0;
            int soil_average = 0;
            while (true) {
                humandtemp = readtempandhum();
                temp = humandtemp[0];///空气温度
                hum = humandtemp[1];///空气湿度
                lx = readlight();///光照
                soilhum = readsoilhum();

                temp_now[i] = temp;
                temp_average += temp_now[i];            //温度5次均值
                light_now[i] = lx;
                light_average += light_now[i];          //光照5次均值
                soil_now[i] = soilhum;
                soil_average += soil_now[i];            //土壤5次均值
                i++;

                while(i==5){
                    i=0;
                    if(flag) {
                        temp_average /= 5;
                        if (temp_average > 25) {
                            openFan();
                            // openTC();
                        } else if (temp_average < 25) {
                            closeFan();
                            //closeTC();
                        }
                        light_average /= 5;
                        if (light_average > 220) {
                            chuanglianguan();
                        } else if (light_average < 80) {
                            chuangliankai();
                        }
                        soil_average /= 5;
                        if (soil_average > 0) {
                            openLed();
                        } else if (soil_average < 0) {
                            closeLed();
                        }
                    }
                    temp_average = 0;
                    light_average = 0;
                    soil_average = 0;

                }


                /*写入数据库*/
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String s = df.format(new Date());
                String sqlStr = "insert into atable(time,temperature,humidity,soil_humidity,light) values(\"" +
                        s + "\",\"" + temp + "\",\"" + hum + "\",\"" + soilhum + "\",\"" + lx + "\");";
                try {
                    sql.executeUpdate(sqlStr);
                    Log.i("temp1", "sql.executeUpdate(sqlStr)");
                } catch (SQLException e) {
                    Log.i("temp1", "database insert error!!!");
                }
                try {
                    Thread.sleep(3000);//3s采集一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private double readlight() {

        byte[] data = {0,0};
        double raw=1;
        try {
            lightx=manager.openI2cDevice("I2C1",light_addr);
            lightx.write(new byte[]{(byte)0x01}, 1);
            lightx.write(new byte[]{(byte)0x07}, 1);
            lightx.write(new byte[]{(byte)0x10}, 1);
            Thread.currentThread().sleep(2000);
            lightx.readRegBuffer(0x00, data, 2);
           // raw = ((int)(data[0]) & 0xff <<8)|((int)(data[1])& 0xff);
            raw = (byteToInt(data[0])<<8)| (byteToInt(data[1]));
            lightx.write(new byte[]{(byte)0x10}, 1);
            lightx.close();
            Log.i("temp:light", ""+raw/1.2);
        }
        catch (Exception e)
        {
            Log.i("temp","光照：error");
        }

        return raw/1.2;
    }
    private int readsoilhum(){
        byte[] hum1=new byte[4];
        int soilhum=0;
        try {
            soilsensor=manager.openI2cDevice("I2C1",soilhum_addr);
            soilsensor.readRegBuffer(0x00,hum1,4);
            soilhum  = hum1[3] & 0xff;
            Log.i("temp:soil", ""+soilhum);
            soilsensor.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("temp:soil","error!");
        }
        return soilhum;
    }
    private float[] readtempandhum()
    {
        float temandhum[] = new float[2];
        byte data[]=new byte[6];
        float final_temp;
        float final_hum;
        byte info[]={0x2C,0x06};
        try
        {
            sensor=manager.openI2cDevice("I2C1",sensor_addr);
            sensor.write(info, 2);
            sensor.readRegBuffer(0x00,data,6);

            int tem1 = byteToInt(data[0])<<8;
            int tem2 = byteToInt(data[1]);
            final_temp = (float)(tem1 + tem2);
            final_temp *=175f;
            final_temp /=((2<<15)-1);
            final_temp = (BigDecimal.valueOf((double)(final_temp - 45f))).setScale(1, RoundingMode.HALF_UP).floatValue();

            int hum1 = ((int)data[3] & 0xff)<<8 ;
            int hum2 = (int)data[4] & 0xff;
            final_hum =(float)(hum1 + hum2);
            final_hum *=100;
            final_hum /=((2<<15)-1);
            final_hum = (BigDecimal.valueOf((double)final_hum)).setScale(1, RoundingMode.HALF_UP).floatValue();

            temandhum[0] = final_temp;
            temandhum[1] = final_hum;
            sensor.close();
            Log.i("temp",""+final_temp);
            Log.i("hum",""+final_hum);
        }
        catch (Exception e)
        {
            Log.i("temp:temandhum","error!");
        }
        return  temandhum;
    }
    public void chuangliankai() {
        try {
            Log. d ( TAG , "chuangliankai: ");
            dianjizheng1.setValue( true);
            Thread. currentThread (). sleep (40000);
            dianjizheng1.setValue( false);
        } catch (Exception e) {
        }
    }
    public void chuanglianguan() {
        try {
            Log. d ( TAG , "chuanglianguan: ");
            dianjifu1.setValue( true);
            Thread. currentThread (). sleep (40000);
            dianjifu1.setValue( false);
        } catch (Exception e) {
        }
    }
    public void closeFan(){
        try {
            Log.d(TAG, "closeFan: ");
            tczheng.setValue(true);
        }catch (Exception e){

        }
    }
    public void openFan(){
        try{
            Log.d(TAG,"openFan:");
            tczheng.setValue(false);
        }catch (Exception e){

        }
    }
    public void openTC() {
        try {
            Log. d ( TAG , "openTC: ");
            tczheng.setValue( true);
            Thread. currentThread (). sleep (5);
            tczheng.setValue( false);
        } catch (Exception e) {
        }
    }
    public void closeTC() {
        try {
            Log. d ( TAG , "closeTC: ");
            tcfu.setValue( true);
            Thread. currentThread (). sleep (5);
            tcfu.setValue( false);
        } catch (Exception e) {
        }
    }
    public void openLed() {
        try {
            led11.setValue(true);
        }catch(Exception e){

        }
    }
    public void closeLed() {
        try {
            led11.setValue(false);
        }catch(Exception e){

        }
    }
    public static int byteToInt(byte b) {

        return b & 0xFF;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if(sensor!=null)
        {
            try
            {
                sensor.close();
                sensor=null;
            }
            catch (Exception e)
            {
                Log.d(TAG, "onDestroy: ");
            }
        }
        if(soilsensor!=null)
        {
            try
            {
                soilsensor.close();
                soilsensor=null;
            }
            catch (Exception e)
            {
                Log.d(TAG, "onDestroy: ");
            }
        }
        if(lightx!=null)
        {
            try
            {
                lightx.close();
                lightx=null;
            }
            catch (Exception e)
            {

            }
        }
        if(rs!=null)
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if(sql!=null)
            try {
                sql.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if(con!=null)
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        /*
        ServerSocket serverSocket = null;
        Socket socket = null;//serverSocket.accept();
        InputStream is =null ;// = socket.getInputStream();
        InputStreamReader isr ;//=new InputStreamReader(is);
        BufferedReader br =null;
         */

        if(br!=null)
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if(isr!=null)
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if(is!=null)
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if(socket==null)
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if(serverSocket!=null)
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }
}
