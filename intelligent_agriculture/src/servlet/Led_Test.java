package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Led_Test
 */
@WebServlet("/Led_Test")


public class Led_Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String IP = "192.168.43.14";
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	
    public Led_Test() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String option="op";
		String address = "/jsp/index1.jsp";
		System.out.println("doGet");
		for(int i=1;i<=8;++i)
		{
			if(request.getParameter(option+i)!=null)
			{
				switch(i){
				case 1: openled();
				System.out.println("open led");
					break;
				case 2:closeled();
				System.out.println("cloes led");
					break;
				case 3:openwindow();
				System.out.println("open window");
					break;
				case 4:closewinodw();
				System.out.println("close window");
					break;
				case 5:opencurtain();
				System.out.println("open curtain");
				 	break;
				case 6:closecurtain();
				System.out.println("close curtain");
					break;
				case 7:autoControl();
				System.out.println("autoControl");
					break;
				case 8:handleControl();
				System.out.println("handleControl");
				
					
				};
				
				
			}
			
			
		}//for
		request.getRequestDispatcher(address).forward(request, response);
	
		
	}

	public static void openled(){
		new Runnable(){
		 public void run() {
				System.out.println("openled");
				Socket socket=null;
				java.io.OutputStream os = null;//socket.getOutputStream();//字节输出流
				PrintWriter pw =null;//new PrintWriter(os);//将输出流包装成打印流
				// TODO Auto-generated method stub
				try {
					socket =new Socket(IP,10086);
				    os = socket.getOutputStream();
					pw =new PrintWriter(os);
					pw.write("1");
					System.out.println("Socke");
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
	public static void closeled(){
		new Runnable(){
			public void run() {
				System.out.println("closeled");
				Socket socket=null;
				java.io.OutputStream os = null;//socket.getOutputStream();//字节输出流
				PrintWriter pw =null;//new PrintWriter(os);//将输出流包装成打印流
				// TODO Auto-generated method stub
				try {
					socket =new Socket(IP,10086);
				    os = socket.getOutputStream();
					pw =new PrintWriter(os);
					pw.write("2");
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
	
	private static void openwindow() {
		// TODO Auto-generated method stub
		new Runnable(){
			 public void run() {
					System.out.println("openled");
					Socket socket=null;
					java.io.OutputStream os = null;//socket.getOutputStream();//字节输出流
					PrintWriter pw =null;//new PrintWriter(os);//将输出流包装成打印流
					// TODO Auto-generated method stub
					try {
						socket =new Socket(IP,10086);
					    os = socket.getOutputStream();
						pw =new PrintWriter(os);
						pw.write("3");
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
	private static void closewinodw() {
		// TODO Auto-generated method stub
		new Runnable(){
			 public void run() {
					Socket socket=null;
					java.io.OutputStream os = null;//socket.getOutputStream();//字节输出流
					PrintWriter pw =null;//new PrintWriter(os);//将输出流包装成打印流
					// TODO Auto-generated method stub
					try {
						socket =new Socket(IP,10086);
					    os = socket.getOutputStream();
						pw =new PrintWriter(os);
						pw.write("4");
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
	private static void opencurtain() {
		// TODO Auto-generated method stub
		new Runnable(){
			 public void run() {
					Socket socket=null;
					java.io.OutputStream os = null;//socket.getOutputStream();//字节输出流
					PrintWriter pw =null;//new PrintWriter(os);//将输出流包装成打印流
					// TODO Auto-generated method stub
					try {
						socket =new Socket(IP,10086);
					    os = socket.getOutputStream();
						pw =new PrintWriter(os);
						pw.write("5");
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

	private static void closecurtain() {
		// TODO Auto-generated method stub
		new Runnable(){
			 public void run() {
					Socket socket=null;
					java.io.OutputStream os = null;//socket.getOutputStream();//字节输出流
					PrintWriter pw =null;//new PrintWriter(os);//将输出流包装成打印流
					// TODO Auto-generated method stub
					try {
						socket =new Socket(IP,10086);
					    os = socket.getOutputStream();
						pw =new PrintWriter(os);
						pw.write("6");
						System.out.println("close curtain");
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
	
	
	private static void autoControl() {
		// TODO Auto-generated method stub
		new Runnable(){
			 public void run() {
					Socket socket=null;
					java.io.OutputStream os = null;//socket.getOutputStream();//字节输出流
					PrintWriter pw =null;//new PrintWriter(os);//将输出流包装成打印流
					// TODO Auto-generated method stub
					try {
						socket =new Socket(IP,10086);
					    os = socket.getOutputStream();
						pw =new PrintWriter(os);
						pw.write("7");
						System.out.println("close curtain");
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
	private static void handleControl() {
		// TODO Auto-generated method stub
		new Runnable(){
			 public void run() {
					Socket socket=null;
					java.io.OutputStream os = null;//socket.getOutputStream();//字节输出流
					PrintWriter pw =null;//new PrintWriter(os);//将输出流包装成打印流
					// TODO Auto-generated method stub
					try {
						socket =new Socket(IP,10086);
					    os = socket.getOutputStream();
						pw =new PrintWriter(os);
						pw.write("8");
						System.out.println("close curtain");
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
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
