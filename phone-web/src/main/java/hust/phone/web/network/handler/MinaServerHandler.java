package hust.phone.web.network.handler;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.common.msg_new_login_res;
import com.MAVLink.common.msg_set_mode;

import hust.phone.mapper.pojo.Uav;
import hust.phone.service.impl.FlyingPathServiceImpl;
import hust.phone.service.impl.UavServiceImpl;
import hust.phone.service.interFace.FlyingPathService;
import hust.phone.utils.pojo.PlanePathVo;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.message.SlpCheckFinish;
import hust.phone.web.network.SLP.message.SlpLinesFinish;
import hust.phone.web.network.SLP.message.SlpMsgHandle;
import hust.phone.web.network.SLP.message.SlpMsgHandleRes;
import hust.phone.web.network.SLP.message.SlpMsgLogin;
import hust.phone.web.network.SLP.message.SlpMsgLoginRes;
import hust.phone.web.network.SLP.message.SlpMsgPutLines;
import hust.phone.web.network.SLP.message.SlpMsgPutTaskNum;
import hust.phone.web.network.SLP.message.SlpMsgSearchLines;
import hust.phone.web.network.SLP.message.SlpMsgStatus;
import hust.phone.web.network.SLP.message.SlpPoint;
import hust.phone.web.network.common.ConstantUtils;
import hust.phone.web.network.common.PointSToWayEncodingUtils;
import hust.phone.web.network.common.WebSocketUtil;
import hust.phone.web.network.filter.websocket.MinaBean;
import hust.phone.web.network.iosession.IOSessionManager;
import hust.phone.web.utils.SpringBeanFactoryUtils;

public class MinaServerHandler extends IoHandlerAdapter {
	
	public static final Logger logger = LoggerFactory.getLogger(MinaServerHandler.class);
	//public static 
	
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
		if(IOSessionManager.mapSessionPlane.containsValue(session))
		{
			IOSessionManager.removeSession(session);
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		System.out.println(cause.getMessage());
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		//System.out.println(Thread.currentThread().getName());
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
				break;
			case ConstantUtils.MSG_SEARCHLINES:
				//处理无人机发的航线信息
				processMessageSearchLines(session,packet);
				break;
			case ConstantUtils.MSG_SEARCHLINES_DETAIL:
				//处理无人机下发的具体航线
				processMessageSearchLinesDeatil(session,packet);
				break;
			case ConstantUtils.MSG_LINES_FINISH:
				//处理无人机下发航线完成
				processMessageLinesFinish(session,packet);
				break;
			case ConstantUtils.MSG_CHECK_FINISH:
				//处理无人机下发的自检完成
				processMessageCheckFinish(session,packet);
				break;
			
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
					String taskId =minaBean.getContent().split("@")[2];
					processMobileMessageFlying(revId,taskId);
					break;
				case ConstantUtils.Mobile_RETURN:
					//下发返航命令
					processMobileMessageReturn(revId);
					break;
				case ConstantUtils.Mobile_PUT_LINES:
					//下发航线命令
					String lineId =minaBean.getContent().split("@")[2];
					processMobileMessagePutLines(revId,lineId);
					break;
				case ConstantUtils.Mobile_PUT_TASKNUM:
					//下发任务编号命令
					String taskid =minaBean.getContent().split("@")[2];
					processMobileMessagePutTaskNum(revId,taskid);
					break;
				case ConstantUtils.Mobile_SEARCHLINES:
					//下发查询航线命令
					processMobileMessageSearchLines(revId);
					break;
				case ConstantUtils.Mobile_SEARCHLINES_DETAIL:
					//下发查询具体航线命令
					processMobileMessageSearchLinesDetail(revId);
					break;
				case ConstantUtils.WEB_LOGIN:
					//web端登录
					processWebLogin(session,revId);
					break;
				case ConstantUtils.WEB_ALL_WATCH:
					//处理查看所有无人机的登录
					processWebAllLogin(session,revId);
					break;
				case ConstantUtils.WEB_BROWSE_LOGIN:
					//处理浏览者登录
					processWebBrowseLogin(session,revId);
					break;
				case ConstantUtils.Mobile_CHECK_START:
					//下发开始自检命令
					processMobileCheckStart(revId);
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
	//手机下发自检命令
	private void processMobileCheckStart(String revId) {
		System.out.println("下发自检指令");
		String s = revId.substring(0,revId.length()-4);
		IoSession sessionPlane = IOSessionManager.getSessionPlane(Long.parseLong(s));
		if(sessionPlane!=null)
		{
			SlpMsgHandle msgHandle = new SlpMsgHandle();
			msgHandle.COM_TYPE = 8;
			msgHandle.RES_RELULT = 0;
			SlpPacket pack = msgHandle.pack();
			pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
			pack.REV_DEVICE_ID = Long.parseLong(s);
			byte[] encoding = pack.encoding();
			sessionPlane.write(IoBuffer.wrap(encoding));
		}
	}
	//处理无人机下发的自检完成
	private void processMessageCheckFinish(IoSession session, SlpPacket packet) {
		String send = packet.SND_DEVICE_ID+ConstantUtils.Phone_SEND;
		SlpCheckFinish msg=(SlpCheckFinish) packet.unpack();
		String content = ConstantUtils.Mobile_CHECK_RESULT+":";
		if(msg.RES_RELULT==ConstantUtils.RES_CHECK_FINISH)
		{
			//自检成功
			content = content+ConstantUtils.Mobile_CHECK_RESULT_FINISH;
		}else
		{
			//自检失败
			content = content+ConstantUtils.Mobile_CHECK_RESULT_FAILURE;
		}
		MinaBean msgs = new MinaBean();
		msgs.setContent(content);
		//将数据推送给手机客户端,
		IoSession sessionMobile1 = IOSessionManager.getSessionMobile(send);
		if(sessionMobile1!=null)
		{
			sessionMobile1.write(msgs);
		}
		
	}
	//处理无人机下发的航线完成
	private void processMessageLinesFinish(IoSession session, SlpPacket packet) {
		String send = packet.SND_DEVICE_ID+ConstantUtils.Phone_SEND;
		SlpLinesFinish msg = (SlpLinesFinish) packet.unpack();
		int index =msg.WAYPOINT_NUM;
		int flag =msg.RES_RELULT;
		int pathId = (int) msg.ROUTE_ID;
		
		//写入成功
		FlyingPathServiceImpl fliying =(FlyingPathServiceImpl) SpringBeanFactoryUtils.getBean("flyingPathServiceImpl");
		//根据飞行路线查询
//			ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
//			FlyingPathServiceImpl fliying = (FlyingPathServiceImpl) ac.getBean("flyingPathServiceImpl");
		//根据飞行路线查询
		List<PlanePathVo> pathList = fliying.getPathToObject(pathId);
		int size = pathList.size();
		int length;
		int start ;
		int finish;
		if(flag ==0)
		{
			//写入成功
			length = index+1+ConstantUtils.PATH_CAP_MAX-1;
			start = index+1;
			finish = index;
		}
		else{
			//写入失败
			length = index+ConstantUtils.PATH_CAP_MAX-1;
			start = index;
			finish = index-1;
		}
		//将信息传送给手机
		String content = ConstantUtils.MSG_PLANE_SEARCHRESULT+":"+pathId+":"+msg.ROUTE_COUNT+":"+finish;
		
		MinaBean beanmsg = new MinaBean();
		beanmsg.setContent(content);
		//将数据推送给手机客户端,
		IoSession sessionMobile1 = IOSessionManager.getSessionMobile(send);
		if(sessionMobile1!=null)
		{
			//System.out.println(content);
			sessionMobile1.write(beanmsg);
		}
		//开始下发新的航线
		int end = (length<size?length:size);
		if(start<=end)
		{
			//有剩余数据没发，所以继续发送
			//System.err.println("start :"+start+" "+"end:"+end);
			ArrayList<SlpPoint> list = new ArrayList<SlpPoint>();
			SlpMsgPutLines msgLines  = new SlpMsgPutLines();
			for(int i=start;i<=end;i++)
			{
				//System.out.print("point:"+i);
				SlpPoint point = new SlpPoint();
				point.WAYPOINT_NUM=i;
				point.WP_LNG = (int)((pathList.get(i-1).getLongitude())*10000000);
				point.WP_LAT = (int)((pathList.get(i-1).getLatitude())*10000000);
				point.WP_ALT =(float) pathList.get(i-1).getHeight();
				point.WAYPOINT_TYPE = (short) pathList.get(i-1).getType();
				point.WP_PARAM1 = pathList.get(i-1).getParamone();
				point.WP_PARAM2 = pathList.get(i-1).getParamtwo();
				list.add(point);
			}
			byte[] pointSToWayEncoding = PointSToWayEncodingUtils.PointSToWayEncoding(list);
			//System.out.println("路径整合"+Arrays.toString(pointSToWayEncoding));
			//清空list
			list.removeAll(list);
			msgLines.ROUTE_ID = pathId;
			msgLines.ROUTE_COUNT = size;
			//System.out.println("num:"+ (end -index+1));
			msgLines.ROUTE_MSG_COUNT = end -index+1;
			msgLines.POINTS= new short[msgLines.ROUTE_MSG_COUNT * ConstantUtils.POINT_LENGTH];
			for(int h= 0;h<pointSToWayEncoding.length;h++)
			{
				msgLines.POINTS[h] = (short) (pointSToWayEncoding[h]&0xff);
			}
			SlpPacket pack = msgLines.pack();
			pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
			pack.REV_DEVICE_ID =packet.SND_DEVICE_ID;
			byte[] encoding = pack.encoding();
			session.write(IoBuffer.wrap(encoding));	
		}
		
	}
	//处理查看无人机所有
	private void processWebAllLogin(IoSession session, String revId) {
		// TODO Auto-generated method stub
		System.out.println("将查看所有的账号保存在session 中");
		session.setAttribute(ConstantUtils.ATTR_USRENO,revId);
		IOSessionManager.addSession(session);
		
	}
	private void processWebBrowseLogin(IoSession session,String revId) {
		System.out.println("将web浏览者账号保存在session中");
		session.setAttribute(ConstantUtils.ATTR_USRENO,revId);
		IOSessionManager.addSession(session);
		
		
	}
	//手机查询航线具体信息
	private void processMobileMessageSearchLinesDetail(String revId) {
		// TODO Auto-generated method stub
		String s = revId.substring(0, revId.length() - 4);

		IoSession sessionPlane = IOSessionManager.getSessionPlane(Long.parseLong(s));
		if (sessionPlane != null) {
			SlpMsgHandle msgHandle = new SlpMsgHandle();
			msgHandle.COM_TYPE = 5;
			msgHandle.RES_RELULT = 0;
			SlpPacket pack = msgHandle.pack();
			pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
			pack.REV_DEVICE_ID = Long.parseLong(s);
			byte[] encoding = pack.encoding();
			// 反馈给无人机消息成功，写入字节流
			// 写入session中,一定要加IoBuffer.wrap
			sessionPlane.write(IoBuffer.wrap(encoding));
		}
		
	}
	//手机查询航线信息
	private void processMobileMessageSearchLines(String revId) {
		// TODO Auto-generated method stub
		String s = revId.substring(0, revId.length() - 4);
		IoSession sessionPlane = IOSessionManager.getSessionPlane(Long.parseLong(s));
		if (sessionPlane != null) {
			SlpMsgHandle msgHandle = new SlpMsgHandle();
			msgHandle.COM_TYPE = 4;
			msgHandle.RES_RELULT = 0;
			SlpPacket pack = msgHandle.pack();
			pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
			pack.REV_DEVICE_ID = Long.parseLong(s);
			byte[] encoding = pack.encoding();
			// 反馈给无人机消息成功，写入字节流
			// 写入session中,一定要加IoBuffer.wrap
			sessionPlane.write(IoBuffer.wrap(encoding));
		}
		
	}
	//手机下发飞行任务号
	private void processMobileMessagePutTaskNum(String revId,String taskId) {
		String s = revId.substring(0, revId.length() - 4);
		System.out.println("下发的无人机"+s);
		IoSession sessionPlane = IOSessionManager.getSessionPlane(Long.parseLong(s));
		if (sessionPlane != null) {
			SlpMsgPutTaskNum msgPutTaskNum = new SlpMsgPutTaskNum();
			msgPutTaskNum.MISSION_ID = Long.parseLong(taskId);
			msgPutTaskNum.RES_RELULT =0;
			SlpPacket pack = msgPutTaskNum.pack();
			pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
			pack.REV_DEVICE_ID = Long.parseLong(s);
			byte[] encoding = pack.encoding();
			// 反馈给无人机消息成功，写入字节流
			// 写入session中,一定要加IoBuffer.wrap
			sessionPlane.write(IoBuffer.wrap(encoding));
		}
		
	}
	
	//手机下发路径
	private void processMobileMessagePutLines(String revId,String LineId) {
		System.out.println("下发路径");
		String s = revId.substring(0, revId.length() - 4);
		IoSession sessionPlane = IOSessionManager.getSessionPlane(Long.parseLong(s));
		if (sessionPlane != null) {
			//System.out.println("找到无人机"+LineId);
			int pathId=Integer.parseInt(LineId);
			FlyingPathServiceImpl fliying =(FlyingPathServiceImpl) SpringBeanFactoryUtils.getBean("flyingPathServiceImpl");
			//根据飞行路线查询
//			ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
//			FlyingPathServiceImpl fliying = (FlyingPathServiceImpl) ac.getBean("flyingPathServiceImpl");
			//根据飞行路线查询
			List<PlanePathVo> pathList = fliying.getPathToObject(pathId);
			ArrayList<SlpPoint> list = new ArrayList<SlpPoint>();
			int size = pathList.size();
			SlpMsgPutLines msg  = new SlpMsgPutLines();
			if(size>=ConstantUtils.PATH_CAP_MAX)
			{
				for(int i=1;i<=ConstantUtils.PATH_CAP_MAX;i++)
				{
					SlpPoint point = new SlpPoint();
					point.WAYPOINT_NUM=i;
					point.WP_LNG = (int)((pathList.get(i-1).getLongitude())*10000000);
					point.WP_LAT = (int)((pathList.get(i-1).getLatitude())*10000000);
					point.WP_ALT =(float) pathList.get(i-1).getHeight();
					point.WAYPOINT_TYPE = (short) pathList.get(i-1).getType();
					point.WP_PARAM1 = pathList.get(i-1).getParamone();
					point.WP_PARAM2 = pathList.get(i-1).getParamtwo();
					list.add(point);
				}
				byte[] pointSToWayEncoding = PointSToWayEncodingUtils.PointSToWayEncoding(list);
				//System.out.println("路径整合"+Arrays.toString(pointSToWayEncoding));
				msg.ROUTE_ID = pathId;
				msg.ROUTE_COUNT = size;
				msg.ROUTE_MSG_COUNT =ConstantUtils.PATH_CAP_MAX;
				msg.POINTS= new short[msg.ROUTE_MSG_COUNT * ConstantUtils.POINT_LENGTH];
				for(int h= 0;h<pointSToWayEncoding.length;h++)
				{
					msg.POINTS[h] = (short) (pointSToWayEncoding[h]&0xff);
				}
			}else
			{
				for(int i=1;i<=size;i++)
				{
					SlpPoint point = new SlpPoint();
					point.WAYPOINT_NUM=i;
					point.WP_LNG = (int)((pathList.get(i-1).getLongitude())*10000000);
					point.WP_LAT = (int)((pathList.get(i-1).getLatitude())*10000000);
					point.WP_ALT =(float) pathList.get(i-1).getHeight();
					point.WAYPOINT_TYPE = (short) pathList.get(i-1).getType();
					point.WP_PARAM1 = pathList.get(i-1).getParamone();
					point.WP_PARAM2 = pathList.get(i-1).getParamtwo();
					list.add(point);
				}
				byte[] pointSToWayEncoding = PointSToWayEncodingUtils.PointSToWayEncoding(list);
				//System.out.println("路径整合"+Arrays.toString(pointSToWayEncoding));
				msg.ROUTE_ID = pathId;
				msg.ROUTE_COUNT = size;
				msg.ROUTE_MSG_COUNT =size;
				msg.POINTS= new short[msg.ROUTE_MSG_COUNT * ConstantUtils.POINT_LENGTH];
				for(int h= 0;h<pointSToWayEncoding.length;h++)
				{
					msg.POINTS[h] = (short) (pointSToWayEncoding[h]&0xff);
				}
			}
			
			SlpPacket pack = msg.pack();
			pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
			pack.REV_DEVICE_ID = Long.parseLong(s);
			byte[] encoding = pack.encoding();
			//System.out.println("打包路径整合"+Arrays.toString(encoding));
			// 反馈给无人机消息成功，写入字节流
			// 写入session中,一定要加IoBuffer.wrap
			
			SlpPacket parse = SlpPacket.parse(encoding);
			//System.out.println(parse.toString());
			SlpMsgPutLines unpack = (SlpMsgPutLines) parse.unpack();
			//System.out.println(unpack.toString());
			
			sessionPlane.write(IoBuffer.wrap(encoding));	
			
			}
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
//		//思罗普的返航指令
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
		
		
	}

	//手机端给无人机发送起飞指令
	private void processMobileMessageFlying(String revId,String taskId) {
		
		String s = revId.substring(0,revId.length()-4);
		IoSession sessionPlane = IOSessionManager.getSessionPlane(Long.parseLong(s));
		if (sessionPlane != null) {
			//先传任务号
			System.out.println("下发任务号");
			SlpMsgPutTaskNum msgPutTaskNum = new SlpMsgPutTaskNum();
			msgPutTaskNum.MISSION_ID = Long.parseLong(taskId);
			msgPutTaskNum.RES_RELULT =0;
			SlpPacket pack = msgPutTaskNum.pack();
			pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
			pack.REV_DEVICE_ID = Long.parseLong(s);
			byte[] encoding = pack.encoding();
			// 反馈给无人机消息成功，写入字节流
			// 写入session中,一定要加IoBuffer.wrap
			sessionPlane.write(IoBuffer.wrap(encoding));
			
			System.out.println("下发起飞指令");
			SlpMsgHandle msgHandle = new SlpMsgHandle();
			msgHandle.COM_TYPE = 1;
			msgHandle.RES_RELULT = 0;
			SlpPacket pack1 = msgHandle.pack();
			pack1.SND_DEVICE_ID = ConstantUtils.Server_Num;
			pack1.REV_DEVICE_ID = Long.parseLong(s);
			byte[] encoding1 = pack1.encoding();
			//反馈给无人机消息成功，写入字节流
			//写入session中,一定要加IoBuffer.wrap
			sessionPlane.write(IoBuffer.wrap(encoding1));
		}
		
	}
	//无人机发送的消息信息
	private void processMessageStatus(IoSession session, SlpPacket packet) {
		
		//获取飞行状态
		byte type[]= SlpPacket.IntToByte((int)packet.SND_DEVICE_ID);
		if(type[0]==3)
		{
			//智能鸟解析
			System.out.println("智能鸟获取飞行");
			
		}else if(type[0]==2){
			//思洛普解析
			System.out.println("思洛普获取飞行");
		}
		System.out.println("获取飞行");
		SlpMsgStatus unpack = (SlpMsgStatus) packet.unpack();
		String lon = unpack.GPS_LON/(10000000.0)+"";
		String lat = unpack.GPS_LAT/(10000000.0)+"";
//		String lon = unpack.GPS_LON+"";
//		String lat = unpack.GPS_LAT+"";
		short mode =unpack.BASEMODE;
		long uavDeciveId = packet.SND_DEVICE_ID;
		float GPS_HDG =unpack.GPS_HDG;
		
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
//		if(unpack.GPS_HDG<0)
//		{
//			GPS_HDG = unpack.GPS_HDG +360;
//		}
//		if(type[0]==2)
//		{
//			//思洛普
//			GPS_HDG = unpack.GPS_HDG/100;
//			unpack.GPS_ELV = unpack.GPS_ELV/1000;
//			unpack.AR_SPD = unpack.AR_SPD*1000;
			
		//}
		content = content+":"+GPS_HDG;
		String content2 = content+":"+uavDeciveId+":"+unpack.AR_SPD+":"+unpack.GR_SPD+":"+lon+":"+lat+":"+unpack.GPS_ELV+":"+
				+unpack.HORI_AGL+":"+unpack.VERT_AGL;
		//将数据推送给手机web客户端,
		processResTOMobileResult(content,packet);
		//将数据推送给web客户端
		processResTOResult(content2,packet);
		
	}
	//将信息推送给手机
	private void processResTOMobileResult(String content, SlpPacket packet) {
		String send = packet.SND_DEVICE_ID+ConstantUtils.Phone_SEND;
		String land = packet.SND_DEVICE_ID+ConstantUtils.Phone_LAND;
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
		
	}
	
	//将飞机状态信息推送web端
	private void processResTOResult(String content2,SlpPacket packet) {
		String web = packet.SND_DEVICE_ID+ ConstantUtils.WEB_LOGIN;
		String browse = packet.SND_DEVICE_ID+ConstantUtils.WEB_BROWSE_LOGIN;
		String watch = packet.SND_DEVICE_ID+ConstantUtils.WEB_WATCH;
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
		//将信息推送给观看的多个消息
		IoSession sessionMobilewatch = IOSessionManager.getSessionMobile(watch);
		if(sessionMobilewatch!=null)
		{
			sessionMobilewatch.write(msg2);
		}
//		for(String key:IOSessionManager.mapSessionMobile.keySet())
//		{
//			System.err.println(key);
//			System.out.print(key.equals(packet.SND_DEVICE_ID+"watch"));
//			if(key.equals((packet.SND_DEVICE_ID+"watch")))
//			{
//				System.out.println("推送给web端");
//				IoSession sessionAll = IOSessionManager.getSessionMobile(key);
//				if(sessionAll!=null)
//				{
//					
//					sessionAll.write(msg2);
//				}
//			}
//		}
		
	}
	//处理无人机发出的处理查询具体航线的信息
	private void processMessageSearchLinesDeatil(IoSession session, SlpPacket packet) {
		String send = packet.SND_DEVICE_ID+ConstantUtils.Phone_SEND;
		IoSession sessionMobile1 = IOSessionManager.getSessionMobile(send);
		if(sessionMobile1!=null)
		{
			SlpMsgPutLines unpack = (SlpMsgPutLines) packet.unpack();
			long routeId =unpack.ROUTE_ID;
			//航点数
			int countNum = unpack.ROUTE_MSG_COUNT;
			//得到无人机传来的飞行的点
			ArrayList<SlpPoint> list2 = PointSToWayEncodingUtils.WayToPoints((unpack.POINTS));
			int start =list2.get(0).WAYPOINT_NUM;
			//查询无人机在数据库里面的数据
			FlyingPathServiceImpl flying =(FlyingPathServiceImpl) SpringBeanFactoryUtils.getBean("flyingPathServiceImpl");
			List<PlanePathVo> flyingList = flying.getPathToObject((int)routeId);
			int flag=0 ;
			String content = ConstantUtils.Mobile_Line_SEARCH+":";
			for(int i=start;i<start+countNum;i++)
			{
				PlanePathVo planePathVo = flyingList.get(i);
				float height = (float) planePathVo.getHeight();
				int latitude = (int) (planePathVo.getLatitude()*10000000);
				int longitude = (int) (planePathVo.getLongitude()*10000000);
				int type = planePathVo.getType();
				float paramone = planePathVo.getParamone();
				float paramtwo = planePathVo.getParamtwo();
				SlpPoint point = list2.get(i-start);
				//比较两个对象的值
				if(point.WP_ALT!=height || point.WAYPOINT_TYPE!= type || point.WP_LAT !=latitude || point.WP_LNG !=longitude || point.WP_PARAM1 !=
						paramone || point.WP_PARAM2 !=paramtwo)
				{
					//航线不匹配
					flag =1;
					break;
					
				}
				
			}
			
			if(flag ==0)
			{
				//核对的航线匹配
				content = content+ConstantUtils.Mobile_SEARCHLine_RESULT_FINISH;
			}else
			{
				content = content+ConstantUtils.Mobile_SEARCHLine_RESULT_FAILURE;
			}
			MinaBean msgs = new MinaBean();
			msgs.setContent(content);
			//将数据推送给手机客户端,
			sessionMobile1.write(msgs);
		}
	}
	
	//无人机发出的处理查询航线的信息
	private void processMessageSearchLines(IoSession session, SlpPacket packet) {
		SlpMsgSearchLines unpack = (SlpMsgSearchLines) packet.unpack();
		//将查询的结果推送给手机
		String send = packet.SND_DEVICE_ID+ConstantUtils.Phone_SEND;
		String content = ConstantUtils.MSG_PLANE_SEARCHRESULT+":"+unpack.ROUTE_ID+":"+unpack.ROUTE_COUNT+":"+unpack.ROUTE_STOCK_COUNT;
		MinaBean msg = new MinaBean();
		msg.setContent(content);
		//将数据推送给手机客户端,
		IoSession sessionMobile1 = IOSessionManager.getSessionMobile(send);
		if(sessionMobile1!=null)
		{
			sessionMobile1.write(msg);
		}
		
	}
	private void processMessageLogin(IoSession session, SlpPacket packet) {
		//处理登录的消息,将无人机的信息存到session中
		long snd =packet.SND_DEVICE_ID;
		SlpMsgLogin res = (SlpMsgLogin) packet.unpack();
		SlpMsgLoginRes loginRes = new SlpMsgLoginRes();
		if(res.UAV_LOGIN==1)
		{
			//登录消息
			//将无人机端的登录保存
			if((IOSessionManager.getSessionPlane(packet.SND_DEVICE_ID)==null))
			{
				System.out.println("将无人机账号保存在session中"+snd);
				session.setAttribute(ConstantUtils.ATTR_PLANENO,packet.SND_DEVICE_ID);
				IOSessionManager.addSession(session);
			
				//将消息推送给手机
				String content =ConstantUtils.MSG_PLANE_LOGIN+":"+"Login";
				processResTOMobileResult(content,packet);
			}
			
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
