package hust.phone.web.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer extends Thread{
	private ServerSocket serverSocket;
	//利用线程池来管理客户端的连接线程
	private ExecutorService exec;
	//static Map<String, Socket>  sessionMap = new HashMap<String, Socket>();
	//存取客户端之间的信息,手机端，key为无人机编号
	 static Map<Integer, Socket> phonesessionMap = new HashMap<Integer, Socket>();
	 //无人机端，key为无人机编号
	 static Map<Integer, Socket> planesessionMap = new HashMap<Integer, Socket>();
	
	 public TcpServer()
	 {
		if(serverSocket==null)
		{
			try {
				//创建端口号
				this.serverSocket = new ServerSocket();
				this.serverSocket.bind(new InetSocketAddress("0.0.0.0", 18888));
				System.err.println("socket start");
				 exec = Executors.newCachedThreadPool();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	 }
	 
	
	 public void run()
	 {
		 System.out.println("服务器启动");
		 try {
			 while(true)
			 {
				 Socket socket = serverSocket.accept();
				 System.out.println("客户端连接成功");
				 //开通线程处理客户端的请求
				 	exec.execute(new MavLinkHandler(socket, phonesessionMap,planesessionMap));
				// Thread thread = new Thread(new ClientHandler(socket, ssionMap)
//				 Thread thread = new Thread(new MavLinkHandler(socket, phonesessionMap,planesessionMap));
//                 thread.setDaemon(true);
//                 thread.start();
			 }
		 }catch (Exception e) {
			// TODO: handle exception
		}
	 }
	 
	//关闭
	public void closeSocketServer()
	{
		try {
			if(null !=serverSocket && !serverSocket.isClosed())
			{
				serverSocket.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
		
	public static void main(String[] args) {
		TcpServer serv=new TcpServer();
		serv.start();
	}

}
