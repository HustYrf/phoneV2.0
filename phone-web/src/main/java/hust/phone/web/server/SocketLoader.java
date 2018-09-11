package hust.phone.web.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SocketLoader implements ServletContextListener {
	
	private TcpServer tcpServer;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if(null !=tcpServer && !tcpServer.isInterrupted())
		{
			tcpServer.closeSocketServer();
			tcpServer.interrupt();
		}
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		if(null==tcpServer)
		{
			//创建线程类
			tcpServer = new TcpServer();
			//启动线程
			tcpServer.start();
		}
		
	}

}
