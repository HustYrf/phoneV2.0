package hust.phone.web.network.iosession;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import hust.phone.web.network.common.ConstantUtils;


public class IOSessionManager {
	/*
	 * 获取所有用户的session，分为无人机端和手机端的session
	 */
	public static Map<Long,IoSession> mapSessionPlane = new HashMap<Long,IoSession>();
	private static Map<String,IoSession> mapSessionMobile = new HashMap<String,IoSession>();
	
	//添加
	public static void addSession(IoSession session)
	{
		if(!isSessionMobile(session))
		{
			synchronized (mapSessionPlane) {
				IoSession tmpSession = mapSessionPlane.remove(getPlaneNoFromIoSessionToLong(session));
				closeSession(tmpSession);
				mapSessionPlane.put(getPlaneNoFromIoSessionToLong(session),session);
			}
		}else {
			synchronized (mapSessionMobile) {
				IoSession tmpSession = mapSessionMobile.remove(getUserNoFromIoSessionToString(session));
				closeSession(tmpSession);
				mapSessionMobile.put(getUserNoFromIoSessionToString(session),session);
			}
		}
	}
	
	//关闭
	public static void closeSession(IoSession session)
	{
		if(session ==null)
		{
			return ;
		}
		//写入关闭消息
		//session.write(arg0);
		session.close(false);
	}
	//通过设备id关闭
	public static void closeSession(long devId,int type)
	{
		if(type == ConstantUtils.DEVICE_Plane)
		{
			synchronized (mapSessionPlane) {
				IoSession tmpSession = mapSessionPlane.remove(devId);
				closeSession(tmpSession);
			}
		}else {
			synchronized (mapSessionMobile) {
				IoSession tmpSession = mapSessionMobile.remove(devId);
				closeSession(tmpSession);
			}
		}
	}
	
	//得到设备的session
	public static IoSession getSessionPlane(Long sessionId)
	{
		return mapSessionPlane.get(sessionId);
		
	}

	public static IoSession getSessionMobile(String sessionId)
	{
		return mapSessionMobile.get(sessionId);
	}
	
	//移除session
	public static void removeSession(IoSession session)
	{
		if(isSessionMobile(session))
		{
			System.out.println("移除session");
			mapSessionMobile.remove(getUserNoFromIoSessionToString(session));
		}else
		{
			System.out.println("移除session");
			mapSessionPlane.remove(getPlaneNoFromIoSessionToLong(session));
		}
	}
	
	public static String getUserNoFromIoSessionToString(IoSession session)
	{
		if(!session.containsAttribute(ConstantUtils.ATTR_USRENO))
		{
			return "";
		}else
		{
			return session.getAttribute(ConstantUtils.ATTR_USRENO).toString();
		}
	}
	
	public static long getUserNoFromIoSessionToLong(IoSession session)
	{
		if(!session.containsAttribute(ConstantUtils.ATTR_USRENO))
		{
			return 0;
		}else
		{
			return Long.valueOf(session.getAttribute(ConstantUtils.ATTR_USRENO).toString());
		}
	}
	public static long getPlaneNoFromIoSessionToLong(IoSession session)
	{
		if(!session.containsAttribute(ConstantUtils.ATTR_PLANENO))
		{
			return 0;
		}else {
			return Long.valueOf(session.getAttribute(ConstantUtils.ATTR_PLANENO).toString());
		}
	}
	public static boolean isSessionMobile(IoSession session) {
		if (!session.containsAttribute(ConstantUtils.DEVICE_TYPE)) {
			throw new RuntimeException("会话没有设置客户端类型");
		} else if (Short.valueOf(session.getAttribute(ConstantUtils.DEVICE_TYPE).toString()) == ConstantUtils.DEVICE_Mobile) {
			return true;
		} else if (Short.valueOf(session.getAttribute(ConstantUtils.DEVICE_TYPE).toString()) == ConstantUtils.DEVICE_Plane) {
			return false;
		}
		return false;
	}
}
