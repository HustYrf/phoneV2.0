package hust.phone.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.common.msg_set_mode;

import hust.phone.mapper.mapper.UavMapper;
import hust.phone.mapper.pojo.Uav;
import hust.phone.service.interFace.UavService;

@Service
public class UavServiceImpl implements UavService {

	@Autowired 
	UavMapper uavMapper;
	@Autowired 
	PlaneCommandImpl planeCommandImpl;
	
	InputStream input = null;
	Socket socket = null;

	
	@Override
	public Uav getPlaneByPlane(Uav uav) {
		
		return uavMapper.getPlaneByPlane(uav);
	}
	//飞机起飞
	@Override
	public void takeoff(int id) {
		short sysid=(short) id;
		 try {
			 //发送消息
			 socket = new Socket("0.0.0.0", 18888);
            //采用打包的方式
            OutputStream out=socket.getOutputStream();
            //起飞类 需要参数
            msg_command_long msg3 =new msg_command_long();
            msg3.target_system =sysid;//无人机编号
            msg3.command=400;
            msg3.param1=1;
            
            //设置起飞
            msg_set_mode msg=new msg_set_mode();
            msg.base_mode=29;
            msg.target_system=sysid;//无人机编号
            msg.custom_mode=67371008;
            int j=80;
            int i=2;
            while(i>0)
            {
            	MAVLinkPacket pack3 = msg3.pack();
            	pack3.sysid=sysid;
            	pack3.seq=++j;
            	byte[] encodePacket3 = pack3.encodePacket();
            	MAVLinkPacket pack = msg.pack();
            	pack.sysid=sysid;
            	pack.seq=++j;
            	byte[] encodePacket = pack.encodePacket();
            	out.write(encodePacket3);
            	out.write(encodePacket);
            	i--;
            	j++;
            	Thread.sleep(1000);
            }
//            //一键起飞后顺便接收经纬度数据
//    		socket.setSoTimeout(5000);
//    		  while (true) {
//              	 	input = socket.getInputStream();
//                   ObjectInputStream ois = new ObjectInputStream(input);
//                   Plane p = (Plane) ois.readObject();
//                    Date updateTime =new Date();
//			        p.setUpdateTime(updateTime);
//		           //将无人机的数据插入到数据库中
//		           planeCommandImpl.updateById(p);
//               	if(socket.isClosed())
//      			{
//      				System.out.println("手机端已经关闭");
//      				break;
//      			}
//    		  }
		 }catch(Exception e){
			 try {
				 if(socket!=null)
				 {
					 socket.close();
				 }
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }finally {
			 try {
				 if(socket!=null)
				 {
					 socket.close();
				 }
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	//分开接收无人机数据
	@Override
	public void showData(int id) {
		short sysid=(short) id;
		try {
			msg_command_long msg=new msg_command_long();
			MAVLinkPacket pack = msg.pack();
			pack.sysid =sysid;
			byte[] encodePacket = pack.encodePacket();
			OutputStream out=socket.getOutputStream();
			out.write(encodePacket);
   			//设置socket的为收到信息的接收时间
   			socket.setSoTimeout(3000);
   			while (true) {
   				input = socket.getInputStream();
               ObjectInputStream ois = new ObjectInputStream(input);
               Uav p = (Uav) ois.readObject();
               Date updateTime =new Date();
               p.setUpdatetime(updateTime);
              //将无人机的数据插入到数据库中
              planeCommandImpl.updateById(p);
           	if(socket.isClosed())
  			{
  				System.out.println("手机端已经关闭");
  				break;
  			}
		  }
		}catch (Exception e) {
			if(socket!=null)
			{
				socket.isClosed();
			}
		}finally {
			if(socket!=null)
			{
				socket.isClosed();
			}
		}
	}
	//飞机降落
	@Override
	public void planeLand(int id) {
		short sysid=(short)id;
		 try {
			 //发送消息
			 socket = new Socket("0.0.0.0", 18888);
            //采用打包的方式
            OutputStream out=socket.getOutputStream();
            //准备数据，先模拟只发一次
            msg_set_mode msg=new msg_set_mode();
            msg.base_mode=29;
            msg.target_system=sysid;//无人机编号
            //land
            msg.custom_mode=100925440;
            int i=3;
            while(i>0)
            {
            	
            	MAVLinkPacket pack = msg.pack();
            	byte[] encodePacket = pack.encodePacket();
            	out.write(encodePacket);
            	i--;
            	Thread.sleep(1000);
            }
		 }catch(Exception e){
			 try {
				 if(socket!=null)
				 {
					 socket.close();
				 }
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }finally {
			 try {
				 if(socket!=null)
				 {
					 socket.close();
				 }
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	@Override
	public void planeReturn(int id) {
		short sysid=(short) id;
		 try {
			 //发送消息
			 socket = new Socket("0.0.0.0", 18888);
            //采用打包的方式
            OutputStream out=socket.getOutputStream();
            //准备数据，先模拟只发一次
            msg_set_mode msg=new msg_set_mode();
            msg.base_mode=29;
            msg.target_system=sysid;//无人机编号
            //land
            msg.custom_mode=84148224;
            int i=3;
            while(i>0)
            {
            	
            	MAVLinkPacket pack = msg.pack();
            	byte[] encodePacket = pack.encodePacket();
            	out.write(encodePacket);
            	i--;
            	Thread.sleep(1000);
            }
		 }catch(Exception e){
			 try {
				 if(socket!=null)
				 {
					 socket.close();
				 }
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		 }finally {
			 try {
				 if(socket!=null)
				 {
					 socket.close();
				 }
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	@Override
	public void updatePositionByUav(Uav uav) {
		uavMapper.updatePositionByPlane(uav);
	}
	

}
