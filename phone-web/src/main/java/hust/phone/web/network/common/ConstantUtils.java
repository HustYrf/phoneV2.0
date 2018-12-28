package hust.phone.web.network.common;

public interface ConstantUtils {
	//消息基本长度
	short BASE_LENGTH = 26;
	String BASE_HEAD="TELUAV";
	short OK =1;
	//客户端类型
	String DEVICE_TYPE = "deviceType";
	short DEVICE_Plane =0;
	short DEVICE_Mobile =1;
	//会话中应答消息种类
	String UAV_RES_FLYINF="resflying";
	String UAV_RES_RETURN ="resreturn";
	//session 存储用户编号
	String ATTR_USRENO ="userNo";
	String ATTR_PLANENO="planeNo";
	String ATTR_REVPLANE ="revplaneNo";
	//无人机MSG_TYPE消息类型
	short MSG_STATUS =1;//状态消息
	short MSG_HANDLE = 2;//指令信息
	short MSG_HANDLERES=3;//指令应答消息
	short MSG_LOGIN =4;//登陆消息
	short MSG_LOGINRES =5;//登录应答
	short MSG_PUTLINES = 6;//下发航线
	short MSG_PUTTASKNUM = 7;//下发任务编号
	short MSG_SEARCHLINES = 0x81;//查询飞行航线信息
	short MSG_SEARCHLINES_DETAIL = 0x82;//查询飞行航线
	short MSG_LINES_FINISH = 8;//无人机写入航线完成
	short MSG_CHECK_FINISH = 9;//无人机自检完成
	
	//手机消息类型
	String Mobile_FlYING ="flying";//起飞
	String Mobile_RETURN = "return";//返航
	String Mobile_Login = "login";//手机登录
	String Mobile_PUT_LINES = "putLines";//下发路径
	String Mobile_PUT_TASKNUM ="putTaskNum";//下发任务编号
	String Mobile_SEARCHLINES = "searchLines";//查询下发路径
	String Mobile_SEARCHLINES_DETAIL="searchLinesDetail";//查询下发路径的具体信息
	String Mobile_CHECK_START ="checkStart";//指示无人机自检开始
	String Mobile_LOCK = "lock";//给无人机加锁
	//自检结果
	String Mobile_CHECK_RESULT ="checkResult";
	short RES_CHECK_FINISH = 1;
	short RES_CHECK_FAILURE = 0;
	String Mobile_CHECK_RESULT_FINISH ="飞机自检成功";
	String Mobile_CHECK_RESULT_FAILURE="飞机自检失败"; 
	//航线结果
	String Mobile_Line_RESULT ="lineResult";
	short RES_Line_FINISH = 1;
	short RES_Line_FAILURE = 0;
	String Mobile_Line_RESULT_FINISH ="飞机上传航线成功";
	String Mobile_Line_RESULT_FAILURE="飞机上传航线失败"; 
	String Mobile_Line_SEARCH ="lineSearch";
	String Mobile_SEARCHLine_RESULT_FINISH ="飞机核对航线成功";
	String Mobile_SEARCHLine_RESULT_FAILURE="飞机核对航线失败"; 
	//路径信息点的大小
	short POINT_LENGTH = 23;
	//路径下路径信息点的最大数目
	short PATH_CAP_MAX = 9;
	
	//用户端类别
	String Phone_SEND = "send";//放飞者
	String Phone_LAND = "land";//降落者
	String WEB_LOGIN = "web";//操作员
	String WEB_BROWSE_LOGIN = "browse";//浏览者
	String WEB_WATCH ="watch";//所有的界面
	String WEB_ALL_WATCH ="all";//查看所有的无人机 --编码为无人机编号watch+@all
	//无人机应答消息类型
	short RES_FLYINF = 1;
	short RES_TRANS = 2;
	short RES_RETURN = 3;
	//无人机应答结果类型
	short RES_EXCUTE = 1;
	short RES_FAILURE = 2;
	short RES_WAIT = 3;
	//无人机发送状态给手机
	String MSG_PLANE_LOGIN = "planeLogin";
	String MSG_TANS_STATUS ="status";
	String MSG_TANS_STATUS_OPEN ="已开机";
	String MSG_TANS_STATUS_LOGINSUCCESS = "系统登录成功";
	String MSG_TANS_STATUS_CHECK ="待自检";
	String MSG_TANS_STATUS_CHECKFINISH = "飞机准备就绪";
	String MSG_TANS_STATUS_UNLOCKED ="解锁完成";
	String MSG_TANS_STATUS_FLYINGUP = "爬升";
	String MSG_TANS_STATUS_FLYING ="巡航中";
	String MSG_TANS_STATUS_LANDING = "降落";
	String MSG_TANS_STATUS_LANDED ="着陆";
	String MSG_TANS_STATUS_CLOSED = "关机";
	String MSG_TANS_STATUS_EXCEPTION ="有异常";
	String MSG_TANS_STATUS_PARACHATEOPENING = "开伞";
	String MSG_TANS_STATUS_RETURN ="返航";;
	//将无人机查询航线结果推送给送机
	String MSG_PLANE_SEARCHRESULT = "planeLine";

	String MSG_TANS_STATUS_INIT = "已连接";

	// 判断客户端类型的CODE
	String ATTR_CLIENT_CODE = "GET";
	//服务器的设备类型
	long Server_Num = 0x01010001;
	
	
}
