package hust.phone.web.network.common;

public interface ConstantUtils {
	
	//客户端类型
	String DEVICE_TYPE = "deviceType";
	short DEVICE_Plane =0;
	short DEVICE_Mobile =1;
	//session 存储用户编号
	String ATTR_USRENO ="userNo";
	String ATTR_PLANENO="planeNo";
	String ATTR_REVPLANE ="revplaneNo";
	//无人机MSG_TYPE消息类型
	short MSG_STATUS =1;//状态消息
	short MSG_HANDLE=2;//指令消息
	short MSG_HANDLERES=3;//指令应答消息
	short MSG_LOGIN =4;//登陆消息
	short MSG_LOGINRES =5;//登录消息应答
	//手机消息类型
	String Mobile_FlYING ="flying";
	String Mobile_RETURN = "return";
	String Mobile_Login = "login";
	//无人机应答消息类型
	short RES_FLYINF = 1;
	short RES_TRANS = 2;
	short RES_RETURN = 3;
	//无人机应答结果类型
	short RES_EXCUTE = 1;
	short RES_FAILURE = 2;
	short RES_WAIT = 3;
	//无人机发送状态给手机
	String MSG_TANS_STATUS ="status";
		
	// 判断客户端类型的CODE
	String ATTR_CLIENT_CODE = "GET";
	//服务器的设备类型
	long Server_Num = 0x01010001;
	
}
