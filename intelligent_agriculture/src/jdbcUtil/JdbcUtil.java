package jdbcUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.*;
import java.net.Socket;
import java.util.Properties;

import org.omg.CORBA.portable.OutputStream;

import com.mysql.jdbc.Statement;

public class JdbcUtil {

	// 锟斤拷示锟斤拷锟斤拷锟斤拷锟捷匡拷锟斤拷没锟斤拷锟�
	private static String USERNAME ;

	// 锟斤拷锟斤拷锟斤拷锟捷匡拷锟斤拷锟斤拷锟�
	private static String PASSWORD;

	// 锟斤拷锟斤拷锟斤拷锟捷匡拷锟斤拷锟斤拷锟斤拷锟较�
	private static String DRIVER;

	// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷菘锟侥碉拷址
	private static String URL;

	// 锟斤拷锟斤拷锟斤拷锟捷匡拷锟斤拷锟斤拷锟�
	private Connection connection;

	// 锟斤拷锟斤拷sql锟斤拷锟斤拷执锟叫讹拷锟斤拷
	private PreparedStatement pstmt;

	// 锟斤拷锟斤拷锟窖拷锟斤拷氐慕锟斤拷锟斤拷锟斤拷
	private ResultSet resultSet;
	
	static{
		loadConfig();
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟捷匡拷锟斤拷锟斤拷锟斤拷息锟斤拷锟斤拷锟斤拷锟斤拷氐锟斤拷锟斤拷愿锟街�
	 */
	
	public static void openled(){
		new Runnable(){
		 public void run() {
				System.out.println("openled");
				Socket socket=null;
				String IP="192.168.0.105";
				java.io.OutputStream os = null;//socket.getOutputStream();//字节输出流
				PrintWriter pw =null;//new PrintWriter(os);//将输出流包装成打印流
				// TODO Auto-generated method stub
				System.out.println("eieiei");
				try {
					socket =new Socket(IP,10086);
				    os = socket.getOutputStream();
					pw =new PrintWriter(os);
					pw.write("1");
					pw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					// br.close();
					// is.close();
					 pw.close();
					 try {
						os.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	 
				}	
			}
		}.run();
	}
	public static void test(){
		System.out.println("Test!");
		Socket socket=null;
		String IP="192.168.0.105";
		java.io.OutputStream os = null;//socket.getOutputStream();//字节输出流
		PrintWriter pw =null;//new PrintWriter(os);//将输出流包装成打印流
		// TODO Auto-generated method stub
		System.out.println("eieiei");
		try {
			socket =new Socket(IP,10086);
		    os = socket.getOutputStream();
			pw =new PrintWriter(os);
			pw.write("1");
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			// br.close();
			// is.close();
			 pw.close();
			 try {
				os.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	 
		}	
	}
	public static void closeled(){
		new Runnable(){
			public void run() {
				System.out.println("closeled");
				Socket socket=null;
				String IP="192.168.0.105";
				java.io.OutputStream os = null;//socket.getOutputStream();//字节输出流
				PrintWriter pw =null;//new PrintWriter(os);//将输出流包装成打印流
				// TODO Auto-generated method stub
				try {
					socket =new Socket(IP,10086);
				    os = socket.getOutputStream();
					pw =new PrintWriter(os);
					pw.write("0");
					pw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					// br.close();
					// is.close();
					 pw.close();
					 try {
						os.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	 
				}	
			}
		}.run();
	}
	public static void loadConfig() {
		try {
			InputStream inStream = JdbcUtil.class
					.getResourceAsStream("jdbc.properties");
			Properties prop = new Properties();
			prop.load(inStream);
			USERNAME = prop.getProperty("jdbc.username");
			PASSWORD = prop.getProperty("jdbc.password");
			DRIVER= prop.getProperty("jdbc.driver");
			URL = prop.getProperty("jdbc.url");
		} catch (Exception e) {
			throw new RuntimeException("exception", e);
		}
	}

	public JdbcUtil() {

	}

	/**
	 * 锟斤拷取锟斤拷锟捷匡拷锟斤拷锟斤拷
	 * 
	 * @return 锟斤拷锟捷匡拷锟斤拷锟斤拷
	 */
	public Connection getConnection() {
		try {
			Class.forName(DRIVER); // 注锟斤拷锟斤拷锟斤拷
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // 锟斤拷取锟斤拷锟斤拷
			if(connection == null){
				System.out.println("鏁版嵁搴撹繛鎺ラ敊璇紒锛侊紒");
			}
		} catch (Exception e) {
			throw new RuntimeException("get connection error!", e);
		}
		return connection;
	}
    
	/**
	 * 执锟叫革拷锟铰诧拷锟斤拷
	 * 
	 * @param sql
	 *            sql锟斤拷锟�
	 * @param params
	 *            执锟叫诧拷锟斤拷
	 * @return 执锟叫斤拷锟�
	 * @throws SQLException
	 */
	public boolean updateByPreparedStatement(String sql, List<?> params)
			throws SQLException {
		boolean flag = false;
		int result = -1;// 锟斤拷示锟斤拷锟矫伙拷执锟斤拷锟斤拷锟缴撅拷锟斤拷锟斤拷薷牡锟绞憋拷锟斤拷锟接帮拷锟斤拷锟斤拷菘锟斤拷锟斤拷锟斤拷
		pstmt = connection.prepareStatement(sql);
		int index = 1;
		// 锟斤拷锟絪ql锟斤拷锟斤拷械锟秸嘉伙拷锟�
		if (params != null && !params.isEmpty()) {
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(index++, params.get(i));
			}
		}
		result = pstmt.executeUpdate();
		flag = result > 0 ? true : false;
		return flag;
	}

	/**
	 * 执锟叫诧拷询锟斤拷锟斤拷
	 * 
	 * @param sql
	 *            sql锟斤拷锟�
	 * @param params
	 *            执锟叫诧拷锟斤拷
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> findResult(String sql, List<?> params)
			throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if (params != null && !params.isEmpty()) {
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();
		if(resultSet == null){
			System.out.println("鏁版嵁搴撹鍙ユ墽琛岄敊璇紒");
		}
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols_len = metaData.getColumnCount();
		while (resultSet.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < cols_len; i++) {
				String cols_name = metaData.getColumnName(i + 1);
				Object cols_value = resultSet.getObject(cols_name);
				if (cols_value == null) {
					cols_value = "--";
				}
				map.put(cols_name, cols_value);
				System.out.println(cols_name+"----->"+cols_value);
			}
			list.add(map);
		}
		return list;
	}
	public Map<String,Object> findNewResult()
	throws SQLException{
		List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
		String sql="select * from atable where id=max(id);";
		pstmt = connection.prepareStatement(sql);
		resultSet = pstmt.executeQuery();
		if(resultSet == null){
			System.out.println("鏁版嵁搴撹鍙ユ墽琛岄敊璇紒");
		}
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols_len = metaData.getColumnCount();
		while (resultSet.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < cols_len; i++) {
				String cols_name = metaData.getColumnName(i + 1);
				Object cols_value = resultSet.getObject(cols_name);
				if (cols_value == null) {
					cols_value = "--";
				}
				map.put(cols_name, cols_value);
				System.out.println(cols_name+"----->"+cols_value);
			}
			list.add(map);
		}
		return list.get(0);
	}
	
	public void  getdata(String sql){
		JdbcUtil 
		jdbcUtil=null;
		ResultSet rs=null;
		Statement statement=null;
		jdbcUtil = new JdbcUtil();  
        jdbcUtil.getConnection(); 
        try {
			statement = (Statement) getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			System.out.println("鍑洪敊1锛侊紒锛�");
			rs = statement.executeQuery(sql);
			System.out.println("鍑洪敊2锛侊紒锛�");
			if(rs==null)
				System.out.println("杩斿洖缁撴灉闆嗗悎涓虹┖锛�");
			else
				System.out.println("杩斿洖缁撴灉闆嗗悎涓嶄负绌猴紒");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("鍑洪敊锛侊紒锛�");
			e.printStackTrace();
		}finally{
			try {
				//rs.close();
				statement.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        //return rs;
    	
    }
	/*public static void main(String[] args){
		String sql="select * from atable;";
		ResultSet rs=null;
		String job = null;
		String id = null;
		//ResultSet rs=null;
		Statement statement=null;
		try {
			//rs=new JdbcUtil().getdata(sql);
			statement = (Statement)new jdbcUtil.JdbcUtil().getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sql);
			System.out.println("noerror1!");
			if(rs==null)
				System.out.println("null");
			//int len=rs.s
			System.out.println("cur_row:"+rs.getRow());
			while(rs.next()){
				if(rs.isLast())
					break;
			}
			if(rs.isLast()){
				System.out.println("noerror!");
				System.out.println("light:");
				System.out.println((String)rs.getString("light"));
				
			}else{
				System.out.println("error!");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	public Map<String, Object> findByid(int id)
	{
		String sql="select * from atable where tableid = ?";
		List<Object> paramList=new ArrayList<Object>();
		paramList.add(id);
		JdbcUtil jdbcUtil=null;
		try{
			jdbcUtil = new JdbcUtil();  
            jdbcUtil.getConnection(); // 锟斤拷取锟斤拷锟捷匡拷锟斤拷锟斤拷  
            List<Map<String, Object>> mapList = jdbcUtil.findResult(  
                    sql.toString(), paramList);  
            if(mapList.size()==1){
            	return mapList.get(0);
            }
		}catch(SQLException e)
		{
			System.out.println(this.getClass()+"执锟叫诧拷询锟斤拷锟斤拷锟阶筹拷锟届常锟斤拷");  
            e.printStackTrace();  
        } finally {  
            if (jdbcUtil != null) {  
                jdbcUtil.releaseConn(); // 一锟斤拷要锟酵凤拷锟斤拷源  
            }  
        }
		return null;  
	}
	
	/**
	 * 锟酵凤拷锟斤拷源
	 */
	public void releaseConn() {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
