package hust.phone.web.network.handler;



import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			SlpPacket packet = (SlpPacket) message;
			//System.out.println(packet);
			//对不同的消息进行处理
			switch(packet.MSG_TYPE)
			{
			case ConstantUtils.MSG_STATUS:
				//获取飞行状态
				processMessageStatus(session,packet);
				break;
			case ConstantUtils.MSG_HANDLE:
				//下发起飞，返航指令
				processMessageHandle(session,packet);
				break;
			case ConstantUtils.MSG_LOGIN:
				//处理登录的消息
				processMessageLogin(session,packet);
				break;
			case ConstantUtils.MSG_HANDLERES:
				//无人机发出的应答消息
				processMessageHandleRes(session,packet);
			}
		}else
		{
			MinaBean minaBean = (MinaBean) message;
			String revId =null;
			String mobile_msgtype=null;
			if(minaBean.getContent().contains("@"))
			{
				 revId = minaBean.getContent().split("@")[0];
				 mobile_msgtype = minaBean.getContent().split("@")[1];
				 System.out.println(revId+mobile_msgtype);
			}
			if (minaBean.isWebAccept()) {
				//建立双工通信

				MinaBean sendMessage = minaBean;
				sendMessage.setContent(WebSocketUtil.getSecWebSocketAccept(minaBean
						.getContent()));
				session.write(sendMessage);
				
			} else {
//				Collection<IoSession> ioSessionSet = session.getService()
//						.getManagedSessions().values();
//				for (IoSession is : ioSessionSet) {
//					is.write(message);
//				}
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
				}
			}
		}
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(session, message);
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.inputClosed(session);
	}
	
	private void processMobileMessageLogin(IoSession session,String revId) {
		//设置手机的账号
		System.out.println("将手机账号保存在session中");
		session.setAttribute(ConstantUtils.ATTR_USRENO,revId);
		IOSessionManager.addSession(session);
	}

	//手机端给无人机发送返回指令
	private void processMobileMessageReturn(String revId) {
	
		System.out.println("下发返回指令");
		String s = revId.substring(0,revId.length()-4);
		IoSession sessionPlane = IOSessionManager.getSessionPlane(Long.parseLong(s));
		if(sessionPlane!=null)
		{
			SlpMsgHandle msgHandle = new SlpMsgHandle();
			msgHandle.COM_TYPE = 3;
			msgHandle.RES_RELULT = 0;
			SlpPacket pack = msgHandle.pack();
			pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
			pack.SND_DEVICE_ID = Long.parseLong(s);
			byte[] encoding = pack.encoding();
			sessionPlane.write(IoBuffer.wrap(encoding));
		}
		
	}

	//手机端给无人机发送起飞指令
	private void processMobileMessageFlying(String revId) {
		System.out.println("下发起飞指令");
		String s = revId.substring(0,revId.length()-4);
		IoSession sessionPlane = IOSessionManager.getSessionPlane(Long.parseLong(s));
		if(sessionPlane!=null)
		{
			SlpMsgHandle msgHandle = new SlpMsgHandle();
			msgHandle.COM_TYPE = 2;
			msgHandle.RES_RELULT = 0;
			SlpPacket pack = msgHandle.pack();
			pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
			pack.SND_DEVICE_ID = Long.parseLong(s);
			byte[] encoding = pack.encoding();
	//		//反馈给无人机消息成功，写入字节流
	//		//写入session中,一定要加IoBuffer.wrap
			sessionPlane.write(IoBuffer.wrap(encoding));
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
		String content = ConstantUtils.MSG_TANS_STATUS+":"+lon+","+lat;
		switch (mode) {
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
		
		System.out.println(content);
		//将数据推送给手机客户端,并保存在数据库中
		processResResult(content,packet);
		
	}
	
	
	private void processMessageHandle(IoSession session, SlpPacket packet) {
		//下发起飞，返航指令
		System.out.println("下放起飞");
		
		
	}
	private void processMessageLogin(IoSession session, SlpPacket packet) {
		//处理登录的消息,将无人机的信息存到session中
		SlpMsgLogin res = (SlpMsgLogin) packet.unpack();
		SlpMsgLoginRes loginRes = new SlpMsgLoginRes();
		if(res.UAV_LOGIN==1)
		{
			//登录消息
			//将无人机端的登录保存
			System.out.println("将无人机账号保存在session中");
			session.setAttribute(ConstantUtils.ATTR_PLANENO,packet.SND_DEVICE_ID);
			IOSessionManager.addSession(session);
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
		
		//设置pack时间
		byte[] encoding = pack.encoding();
		//反馈给无人机消息成功，写入字节流
		//写入session中,一定要加IoBuffer.wrap
		session.write(IoBuffer.wrap(encoding) );

		
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
		case ConstantUtils.RES_TRANS:
			//应答指示无人机上传
			processResTrans(packet,resrelult);
			break;
		case ConstantUtils.RES_RETURN:
			//应答指示无人机返航
			processResReturn(packet,resrelult);
			break;
		}
		
	}
	
	//应答指示无人机起飞
	private void processResFlying(SlpPacket packet,short resrelult) {
		String content =null;
		switch (resrelult) {
		case ConstantUtils.RES_EXCUTE:
			content ="flyingExcute:excute";
			//向手机指示消息，无人机收到起飞指令并执行
			processResResult(content,packet);
			break;
		case ConstantUtils.RES_FAILURE:
			content ="flyingFailure:failure";
			//向手机指示消息，无人机收到起飞指令但无法执行
			processResResult(content,packet);
			break;
		case ConstantUtils.RES_WAIT:
			content ="flyingWait:wait";
			//向手机指示消息，无人机收到起飞指令但等待执行
			processResResult(content,packet);
		default:
			break;
		}
		
	}
	//处理将数据写给手机
	private void processResResult(String content, SlpPacket packet) {
		String send = packet.SND_DEVICE_ID+"send";
		String land = packet.SND_DEVICE_ID+"land";
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
			content ="trans excute";
			processResResult(content,packet);
			break;
		case ConstantUtils.RES_FAILURE:
			content ="trans failure";
			//向手机指示消息，无人机收到上传指令但无法执行
			processResResult(content,packet);
			break;
		case ConstantUtils.RES_WAIT:
			content ="trans wait";
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