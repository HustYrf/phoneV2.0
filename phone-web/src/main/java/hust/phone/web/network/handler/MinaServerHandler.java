package hust.phone.web.network.handler;



import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.common.msg_new_login_res;
import com.MAVLink.common.msg_set_mode;

import hust.phone.mapper.pojo.Uav;
import hust.phone.service.impl.UavServiceImpl;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.message.SlpMsgHandle;
import hust.phone.web.network.SLP.message.SlpMsgHandleRes;
import hust.phone.web.network.SLP.message.SlpMsgLogin;
import hust.phone.web.network.SLP.message.SlpMsgLoginRes;
import hust.phone.web.network.SLP.message.SlpMsgStatus;
import hust.phone.web.network.common.ConstantUtils;
import hust.phone.web.network.common.WebSocketUtil;
import hust.phone.web.network.filter.websocket.MinaBean;
import hust.phone.web.network.iosession.IOSessionManager;


public class MinaServerHandler extends IoHandlerAdapter {
	
	public static final Logger logger = LoggerFactory.getLogger(MinaServerHandler.class);

	
	@Autowired
	UavServiceImpl uavServiceImpl;
	
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("创建连接");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("打开连接");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("关闭连接");
		IOSessionManager.removeSession(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		System.out.println("连接空闲");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		System.out.println(cause.getMessage());
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		//收到消息
		if(message instanceof SlpPacket)
		{
			//socket通信
			SlpPacket packet = (SlpPacket) message;
			//System.out.println(packet);
			//对不同的消息进行处理
			switch(packet.MSG_TYPE)
			{
			case ConstantUtils.MSG_STATUS:
				//获取飞行状态
				processMessageStatus(session,packet);
				break;
			case ConstantUtils.MSG_LOGIN:
				//处理登录的消息
				processMessageLogin(session,packet);
				break;
			case ConstantUtils.MSG_HANDLERES:
				//无人机发出的应答消息
				processMessageHandleRes(session,packet);
			default:
				break;
			}
		}else
		{
			//websocket通信
			MinaBean minaBean = (MinaBean) message;
			String revId =null;
			String mobile_msgtype=null;
			if(minaBean.getContent().contains("@"))
			{
				 revId = minaBean.getContent().split("@")[0];
				 mobile_msgtype = minaBean.getContent().split("@")[1];
				 System.out.println(revId+":"+mobile_msgtype);
			}
			if (minaBean.isWebAccept()) {
				//建立双工通信
				MinaBean sendMessage = minaBean;
				sendMessage.setContent(WebSocketUtil.getSecWebSocketAccept(minaBean
						.getContent()));
				session.write(sendMessage);
				
			} else {
				switch (mobile_msgtype) {
				case ConstantUtils.Mobile_Login:
					//手机登录
					processMobileMessageLogin(session,revId);
					break;
				case ConstantUtils.Mobile_FlYING:
					//给飞机写放飞指令
					processMobileMessageFlying(revId);
					break;
				case ConstantUtils.Mobile_RETURN:
					processMobileMessageReturn(revId);
					break;
				case ConstantUtils.WEB_LOGIN:
					//web端登录
					processWebLogin(session,revId);
					break;
				case ConstantUtils.WEB_BROWSE_LOGIN:
					//处理浏览者登录
					processWebBrowseLogin(session,revId);
					break;
				default:
					break;
				}
			}
		}
	}
	

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		super.inputClosed(session);
	}
	
	private void processWebBrowseLogin(IoSession session,String revId) {
		System.out.println("将web浏览者账号保存在session中");
		session.setAttribute(ConstantUtils.ATTR_USRENO,revId);
		IOSessionManager.addSession(session);
		
		
	}
	private void processMobileMessageLogin(IoSession session,String revId) {
		//设置手机的账号
		System.out.println("将手机账号保存在session中");
		session.setAttribute(ConstantUtils.ATTR_USRENO,revId);
		IOSessionManager.addSession(session);
	}
	
	private void processWebLogin(IoSession session,String revId) {
		System.out.println("将web账号保存在session中");
		session.setAttribute(ConstantUtils.ATTR_USRENO,revId);
		IOSessionManager.addSession(session);
		
	}

	//手机端给无人机发送返回指令
	private void processMobileMessageReturn(String revId) {
	
		System.out.println("下发返回指令");
		String s = revId.substring(0,revId.length()-4);
		byte type[]= SlpPacket.IntToByte((int)Long.parseLong(s));
		if(type[0]==2)
		{
			//思罗普的返航指令
			IoSession sessionPlane = IOSessionManager.getSessionPlane(Long.parseLong(s));
			if(sessionPlane!=null)
			{
				SlpMsgHandle msgHandle = new SlpMsgHandle();
				msgHandle.COM_TYPE = 3;
				msgHandle.RES_RELULT = 0;
				SlpPacket pack = msgHandle.pack();
				pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
				pack.REV_DEVICE_ID = Long.parseLong(s);
				byte[] encoding = pack.encoding();
				sessionPlane.write(IoBuffer.wrap(encoding));
			}
		}else if(type[0]==3)
		{
			//智能鸟的返航指令
			IoSession sessionPlane = IOSessionManager.getSessionPlane(Long.parseLong(s));
			if(sessionPlane!=null)
			{
				int uavId = (type[2] &0xff)*16+(type[3] &0xff);
				msg_set_mode msg1=new msg_set_mode();
				msg1.base_mode=29;
				msg1.target_system=(short) uavId;//无人机编号
				//return
				msg1.custom_mode=84148224;
				MAVLinkPacket pack4 = msg1.pack();
				byte[] encodePacket1 = pack4.encodePacket();
				 int i=3;
				  while(i>0)
				  {
					  sessionPlane.write(IoBuffer.wrap(encodePacket1));
					  i--;
				  }
			}
			
		}
		
		
	}

	//手机端给无人机发送起飞指令
	private void processMobileMessageFlying(String revId) {
		System.out.println("下发起飞指令");
		String s = revId.substring(0,revId.length()-4);
		byte type[]= SlpPacket.IntToByte((int)Long.parseLong(s));
		if(type[0]==2)
		{
			//思罗普的起飞指令
			IoSession sessionPlane = IOSessionManager.getSessionPlane(Long.parseLong(s));
			if(sessionPlane!=null)
			{
				SlpMsgHandle msgHandle = new SlpMsgHandle();
				msgHandle.COM_TYPE = 1;
				msgHandle.RES_RELULT = 0;
				SlpPacket pack = msgHandle.pack();
				pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
				pack.REV_DEVICE_ID = Long.parseLong(s);
				byte[] encoding = pack.encoding();
				//反馈给无人机消息成功，写入字节流
				//写入session中,一定要加IoBuffer.wrap
				sessionPlane.write(IoBuffer.wrap(encoding));
			}
		}else if(type[0]==3)
		{
			//智能鸟的起飞指令
			IoSession sessionPlane = IOSessionManager.getSessionPlane(Long.parseLong(s));
			if(sessionPlane!=null)
			{
				  int uavId = (type[2] &0xff)*16+(type[3] &0xff);
				  msg_command_long msg3 =new msg_command_long();
		          msg3.target_system =(short) uavId;//无人机编号
		          msg3.command=400;
		          msg3.param1=1;
		          MAVLinkPacket pack3 = msg3.pack();
		          byte[] encodePacket3 = pack3.encodePacket();
		          msg_set_mode msg=new msg_set_mode();
		          msg.base_mode=29;
		          msg.target_system=(short) uavId;//无人机编号
		          msg.custom_mode=67371008;
		          MAVLinkPacket pack = msg.pack();
		          byte[] encodePacket = pack.encodePacket();
		          int i = 3;
		          while(i>0)
		          {
			          sessionPlane.write(IoBuffer.wrap(encodePacket3));
			          try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			          sessionPlane.write(IoBuffer.wrap(encodePacket));
			          i--;
		          }
			}
		}
		
	}
	//无人机发送的消息信息
	private void processMessageStatus(IoSession session, SlpPacket packet) {
		
		//获取飞行状态
		System.out.println("获取飞行");
		SlpMsgStatus unpack = (SlpMsgStatus) packet.unpack();
		String lon = unpack.GPS_LON/(10000000.0)+"";
		String lat = unpack.GPS_LAT/(10000000.0)+"";
		short mode =unpack.BASEMODE;
		long uavId = packet.SND_DEVICE_ID;
		String content = ConstantUtils.MSG_TANS_STATUS+":"+lon+","+lat;
		switch (mode) {
		case 0:
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_INIT;
		case 1: 
			//开机状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_OPEN;
			break;
		case 2: 
			//系统登录成功状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_LOGINSUCCESS;
			break;
		case 3: 
			//待自检状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_CHECK;
			break;
		case 4: 
			//飞机准备就绪状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_CHECKFINISH;
			break;
		case 5: 
			//解锁完成状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_UNLOCKED;
			break;
		case 6: 
			//以起飞，爬升状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_FLYINGUP;
			break;
		case 7: 
			//巡航中状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_FLYING;
			break;
		case 8: 
			//降落状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_LANDING;
			break;
		case 9: 
			//着陆状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_LANDED;
			break;
		case 10: 
			//关机状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_CLOSED;
			break;
		case 11: 
			//有异常状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_EXCEPTION;
			break;
		case 12: 
			//开伞状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_PARACHATEOPENING;
			break;
		case 13: 
			//返航状态
			content =content+":"+ConstantUtils.MSG_TANS_STATUS_RETURN;
			break;

		default:
			break;
		}
		String content2 = content+":"+unpack.AR_SPD+":"+unpack.GR_SPD+":"+lon+":"+lat+":"+unpack.GPS_ELV+":"+unpack.GPS_HDG+":"
				+unpack.HORI_AGL+":"+unpack.VERT_AGL;
		//将数据推送给手机web客户端,并保存在数据库中
		processResTOResult(content,packet,content2);
		Uav uav = new Uav();
		
		//uav.setId(uavId);
		
		//数据库插入。。。。。
		
	}
	
	//将飞机状态信息推送给手机，web端
	private void processResTOResult(String content, SlpPacket packet,String content2) {
		String send = packet.SND_DEVICE_ID+ConstantUtils.Phone_SEND;
		String land = packet.SND_DEVICE_ID+ConstantUtils.Phone_LAND;
		String web = packet.SND_DEVICE_ID+ ConstantUtils.WEB_LOGIN;
		String browse = packet.SND_DEVICE_ID+ConstantUtils.WEB_BROWSE_LOGIN;
		MinaBean msg = new MinaBean();
		msg.setContent(content);
		//将数据推送给手机客户端,
		IoSession sessionMobile1 = IOSessionManager.getSessionMobile(send);
		if(sessionMobile1!=null)
		{
			sessionMobile1.write(msg);
		}
		IoSession sessionMobile2 = IOSessionManager.getSessionMobile(land);
		if(sessionMobile2!=null)
		{
			sessionMobile2.write(msg);
		}
		MinaBean msg2= new MinaBean();
		msg2.setContent(content2);
		//多推送更多的状态消息给web端和browse端
		IoSession sessionMobileweb = IOSessionManager.getSessionMobile(web);
		if(sessionMobileweb!=null)
		{
			sessionMobileweb.write(msg2);
		}
		IoSession sessionMobilebrowse = IOSessionManager.getSessionMobile(browse);
		if(sessionMobilebrowse!=null)
		{
			sessionMobilebrowse.write(msg2);
		}
		
	}

//	private void processMessageHandle(IoSession session, SlpPacket packet) {
//		//下发起飞，返航指令
//		System.out.println("下放起飞");
//		
//		
//	}
	private void processMessageLogin(IoSession session, SlpPacket packet) {
		//处理登录的消息,将无人机的信息存到session中
		long snd =packet.SND_DEVICE_ID;
		byte type[]= SlpPacket.IntToByte((int)snd);
		if(type[0]==2)
		{
			//思罗普的登录消息
			SlpMsgLogin res = (SlpMsgLogin) packet.unpack();
			SlpMsgLoginRes loginRes = new SlpMsgLoginRes();
			if(res.UAV_LOGIN==1)
			{
				//登录消息
				//将无人机端的登录保存
				System.out.println("将无人机账号保存在session中");
				session.setAttribute(ConstantUtils.ATTR_PLANENO,packet.SND_DEVICE_ID);
				IOSessionManager.addSession(session);
				//将消息推送给手机
				String content =ConstantUtils.MSG_PLANE_LOGIN+":"+"Login";
				processResTOResult(content,packet,content);
				
				loginRes.UAV_LOGIN=1;
				loginRes.RES_RELULT=1;
			}
			else if(res.UAV_LOGIN==2)
			{
				//注销消息
				loginRes.UAV_LOGIN=2;
				loginRes.RES_RELULT=1;
			}
			//反馈给无人机登录和注销消息成功，写入字节流
			SlpPacket pack = loginRes.pack();
			pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
			pack.REV_DEVICE_ID = packet.SND_DEVICE_ID;
			
			//设置pack时间
			byte[] encoding = pack.encoding();
			//反馈给无人机消息成功，写入字节流
			//写入session中,一定要加IoBuffer.wrap
			session.write(IoBuffer.wrap(encoding) );
		}else if(type[0]==3)
		{
			int uavId = (type[2] &0xff)*16+(type[3] &0xff);
			//智能鸟的登录
			SlpMsgLogin res = (SlpMsgLogin) packet.unpack();
			if(res.UAV_LOGIN==1)
			{
				//登录消息
				//将无人机端的登录保存
				if(IOSessionManager.getSessionPlane(packet.SND_DEVICE_ID)==null)
				{
					System.out.println("将无人机账号保存在session中");
					session.setAttribute(ConstantUtils.ATTR_PLANENO,packet.SND_DEVICE_ID);
					IOSessionManager.addSession(session);
					//将消息推送给手机
					String content =ConstantUtils.MSG_PLANE_LOGIN+":"+"Login";
					processResTOResult(content,packet,content);
				}
				//反馈消息给无人机登录成功
				msg_new_login_res loginRes = new msg_new_login_res();
				loginRes.HEAD[0]= 0x54;
				loginRes.HEAD[1]=0x45;
				loginRes.HEAD[2]=0x4C;
				loginRes.HEAD[3]=0x55;
				loginRes.HEAD[4]=0x41;
				loginRes.HEAD[5]=0x56;
				loginRes.MSG_TYPE=5;
				loginRes.MSG_LEN =33;
				loginRes.REV_DEVICE_ID = packet.SND_DEVICE_ID;
				loginRes.SND_DEVICE_ID = ConstantUtils.Server_Num;
				loginRes.RES_RELULT = 1;
				loginRes.UAV_LOGIN = 1;
				loginRes.MSG_TIME = 0;
				loginRes.VAL_TIME = 0;
				loginRes.CHECKSUM = 0;
				MAVLinkPacket pack = loginRes.pack();
				pack.sysid = uavId;
				byte[] encodePacket = pack.encodePacket();
				session.write(IoBuffer.wrap(encodePacket) );
			}
			
		}
		

		
	}
	//无人机发出的应答消息，显示无人机可以起飞，上传，返航
	private void processMessageHandleRes(IoSession session, SlpPacket packet) {
		
		//将数据发送给手机，让手机处理
		SlpMsgHandleRes  res = (SlpMsgHandleRes) packet.unpack();
		short restype = res.COM_TYPE;
		short resrelult = res.RES_RELULT;
		switch(restype)
		{
		case ConstantUtils.RES_FLYINF:
			//应答指示无人机起飞
			processResFlying(packet,resrelult);
			break;
//		case ConstantUtils.RES_TRANS:
//			//应答指示无人机上传
//			processResTrans(packet,resrelult);
//			break;
		case ConstantUtils.RES_RETURN:
			//应答指示无人机返航
			processResReturn(packet,resrelult);
			break;
		default:
				break;
		}
		
	}
	
	//应答指示无人机起飞给放飞员
	private void processResFlying(SlpPacket packet,short resrelult) {
		String content =null;
		switch (resrelult) {
		case ConstantUtils.RES_EXCUTE:
			content ="flyingExcute:excute";
			//向手机指示消息，无人机收到起飞指令并执行
			processResResultToSend(content,packet);
			break;
		case ConstantUtils.RES_FAILURE:
			content ="flyingFailure:failure";
			//向手机指示消息，无人机收到起飞指令但无法执行
			processResResultToSend(content,packet);
			break;
		case ConstantUtils.RES_WAIT:
			content ="flyingWait:wait";
			//向手机指示消息，无人机收到起飞指令但等待执行
			processResResultToSend(content,packet);
		default:
			break;
		}
		
	}
	//处理起飞数据
	private void processResResultToSend(String content, SlpPacket packet) {
		String send = packet.SND_DEVICE_ID+ConstantUtils.Phone_SEND;
		MinaBean msg = new MinaBean();
		msg.setContent(content);
		//将数据推送给手机客户端,
		IoSession sessionMobile1 = IOSessionManager.getSessionMobile(send);
		if(sessionMobile1!=null)
		{
			sessionMobile1.write(msg);
		}		
	}

	//处理返回将数据写给手机
	private void processResResult(String content, SlpPacket packet) {
		String send = packet.SND_DEVICE_ID+ConstantUtils.Phone_SEND;
		String land = packet.SND_DEVICE_ID+ConstantUtils.Phone_LAND;
		MinaBean msg = new MinaBean();
		msg.setContent(content);
		//将数据推送给手机客户端,
		IoSession sessionMobile1 = IOSessionManager.getSessionMobile(send);
		//System.out.println(msg.getContent());
		if(sessionMobile1!=null)
		{
			sessionMobile1.write(msg);
		}
		IoSession sessionMobile2 = IOSessionManager.getSessionMobile(land);
		System.out.println(msg.getContent());
		if(sessionMobile2!=null)
		{
			sessionMobile2.write(msg);
		}
	}

	//应答指示无人机上传
	private void processResTrans(SlpPacket packet,short resrelult) {
		String content =null;
		switch (resrelult) {
		case ConstantUtils.RES_EXCUTE:
			//向手机指示消息，无人机收到上传指令并执行
			content ="transexcute";
			processResResult(content,packet);
			break;
		case ConstantUtils.RES_FAILURE:
			content ="transfailure";
			//向手机指示消息，无人机收到上传指令但无法执行
			processResResult(content,packet);
			break;
		case ConstantUtils.RES_WAIT:
			content ="transwait";
			//向手机指示消息，无人机收到上传指令但等待执行
			processResResult(content,packet);
		default:
			break;
		}
		
	}
	//应答无人机返航
	private void processResReturn(SlpPacket packet,short resrelult ) {
		String content =null;
		switch (resrelult) {
		case ConstantUtils.RES_EXCUTE:
			//向手机指示消息，无人机收到返航起飞指令并执行
			content = "returnExcute:excute";
			processResResult(content,packet);
			break;
		case ConstantUtils.RES_FAILURE:
			//向手机指示消息，无人机收到返航指令但无法执行
			content = "returnFailure:failure";
			processResResult(content,packet);
			break;
		case ConstantUtils.RES_WAIT:
			//向手机指示消息，无人机收到返航但等待执行
			content = "returnWait:wait";
			processResResult(content,packet);
		default:
			break;
		}
	}
}
