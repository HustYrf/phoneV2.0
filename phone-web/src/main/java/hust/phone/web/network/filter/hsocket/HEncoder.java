package hust.phone.web.network.filter.hsocket;


import java.util.Arrays;

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
//		byte [] data = (byte[]) message;
//		System.err.println("服务器应答的消息：length=" + data.length+"消息类型:"+data[19]);
//		System.err.println(Arrays.toString(data));
//		System.err.println("--------------------");
		out.write(message);
		out.flush();
	}

	public void dispose(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
