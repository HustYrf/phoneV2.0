package hust.phone.web.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.common.msg_global_position_int;

import hust.phone.mapper.pojo.Uav;
import hust.phone.service.impl.PlaneCommandImpl;


public class MavLinkHandler extends Thread{
	
	@Autowired 
	PlaneCommandImpl planeCommandImpl;
	
	 Socket socket = null;
	 Map<Integer, Socket> phonesessionMap = null;//手机
	 Map<Integer, Socket> planesessionMap = null;//无人机
	 public MavLinkHandler(Socket socket, Map<Integer, Socket> phonesessionMap,Map<Integer, Socket> planesessionMap)
	 {
		 this.socket=socket;
		 this.phonesessionMap=phonesessionMap;
		 this.planesessionMap=planesessionMap;
	 }
	 public void run()
	 {
		 try {
			 InputStream in =socket.getInputStream(); 
			 while(true)
			 {
				 //读取消息
				 byte[]buf = new byte[2];
				 in.read(buf);
				 //解析数据
				 Parser parser = new Parser();
				//先读出包的长度
				int plen = buf[1] & 0x00FF;
				int len = plen +2 +6;
				byte [] lenbuf = new  byte[len];
				lenbuf[0]=buf[0];
				lenbuf[1]=buf[1];
				in.read(lenbuf, 2, len-2);
				 for(int i=0;i<len-1;i++)
				 {
					 int code = lenbuf[i] & 0x00FF;
					 parser.mavlink_parse_char(code);
				 }
				 MAVLinkPacket m = parser.mavlink_parse_char(lenbuf[len-1]  & 0x00FF);
				 if(m!=null)
				 {
					 int toclientid =m.sysid ;
					 if(m.msgid==33)
					 {
						 //无人机发过来的消息 ，来自无人机端的信息放入到planesessionMap中
						planesessionMap.put(toclientid, socket);
						//System.out.println("将无人机客户端"+toclientid+"放入到了planesessionMap中");
					 }
					 else if(m.msgid==11||m.msgid==76)
					 {
						 //来自手机端的信息放入到phonesessionMap中,用于获取地理位置
						 
						 phonesessionMap.put(toclientid, socket);
						 //System.out.println("将手机客户端"+toclientid+"放入到了phonesessionMap中");
						 
					 }
					 //发送数据
					 try {
						 Socket targetSocket =null;
	                	if(m.msgid==11||m.msgid==76)
	                	{
	                		//来自手机端的信息要把信息发给无人机，查询planeSessionmap中的key=无人机编号
	                		 targetSocket = planesessionMap.get(toclientid);
	                		 if(targetSocket!=null&&(!targetSocket.isClosed()))
	                		 {
	                			 //将数据包发送
	                			 OutputStream out = targetSocket.getOutputStream();
	                			 out.write(lenbuf);
	                			// System.out.println("服务端已转发给无人机端"); 
	                		 }
	                	}
	                	else if(m.msgid==33){
	                		//来自无人机的信息，要把信息发给手机，查询phoneSessionMap 中和key=无人机的编号
	                		targetSocket = phonesessionMap.get(toclientid);
	                		 if(targetSocket!=null&&(!targetSocket.isClosed()))
	                		 {
	                			 msg_global_position_int msg=(msg_global_position_int) m.unpack(); 
	                			 //将解析的数据发送给手机
	                			 //得到经度，维度，高度，无人机编号
	                			 Uav p = new Uav();
	                			 double lon =msg.lon/(10000000.0);
	                			 double lat =msg.lat/(10000000.0);
	                			 double alt =msg.alt/1000.0;
	                			 String fl ="Point("+lon+" "+lat+")";
	                			/* p.setPlaneId(m.sysid+"");
	                			 p.setHeight(alt+"");
	                			 p.setFlongda(fl);*/
	                			 //将对象发送给手机
	                			 OutputStream out = targetSocket.getOutputStream();
	                			 ObjectOutputStream oos = new ObjectOutputStream(out);
	                			 oos.writeObject(p);
	                			// System.out.println("服务端已转发手机端");
	                			 //控制接收无人机数据的时间
	                			 Thread.sleep(1000);
	                		 }
	                	}
				 }
				 catch(Exception e)
				 {
					 e.printStackTrace();
				 }
				 if (socket.isClosed()) {
                     break;
                 }
				}
			 }
		 }catch (Exception e) {
			  e.printStackTrace();
              try {
                  socket.close();
              } catch (IOException e1) {
                  e1.printStackTrace();
              }
		}
	 }
}
