package hust.phone.web.network.filter.hsocket;


import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

//编码工具类将字节流直接发送出去
public class HEncoder implements ProtocolEncoder {
	
	//private  Charset charset;  
	public HEncoder() {
	}
//	public HEncoder(Charset charSet) {
//		 this.charset = charSet;  
//	}

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		System.out.println("socket编码开始");
		//直接将数据发送出去
	///	byte [] data = (byte[]) message;
		
		out.write(message);
		out.flush();
	}

	public void dispose(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
