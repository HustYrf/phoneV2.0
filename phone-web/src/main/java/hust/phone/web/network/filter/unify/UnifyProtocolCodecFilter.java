package hust.phone.web.network.filter.unify;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import hust.phone.web.network.common.ConstantUtils;





public class UnifyProtocolCodecFilter extends ProtocolCodecFilter {

	public UnifyProtocolCodecFilter(UnifyProtocolCodecFactory factory) {
		super(factory);
	}
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		// 初始请求，将请求的客户端类型存入session中
		if(!session.containsAttribute(ConstantUtils.DEVICE_TYPE))
		{
			IoBuffer in = (IoBuffer) message;
			getClientType(in.buf(),session);
		}
		super.messageReceived(nextFilter, session, message);
	}

	private void getClientType(ByteBuffer buf, IoSession session) {
		//获得字节流里面的登录消息，无人机登录和用户登录
		short head = (short)(buf.get(0)&0xff);
		short MSG_TYPE =(short) (buf.get(20)&0xff);
		String requestInfo  = new String(buf.array(),0,buf.limit(),UTF_8);
		if(requestInfo.contains("websocket"))
		{
			//手机端设置session
			System.err.println("手机端设置session");
			session.setAttribute(ConstantUtils.DEVICE_TYPE,ConstantUtils.DEVICE_Mobile);
		}else if(MSG_TYPE==4 || MSG_TYPE == 1|| head ==0xfe){
			//无人机端设置session
			session.setAttribute(ConstantUtils.DEVICE_TYPE,ConstantUtils.DEVICE_Plane);
			System.out.println("无人机设置session");
		}
			
		
	
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		
		//发送消息后的过滤
		super.messageSent(nextFilter, session, writeRequest);
	}
	

}
