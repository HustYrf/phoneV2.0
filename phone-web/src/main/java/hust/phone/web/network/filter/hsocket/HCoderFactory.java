package hust.phone.web.network.filter.hsocket;


import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


//自定义编码和解析
public class HCoderFactory implements ProtocolCodecFactory {
	
	  private  HEncoder encoder;  
	  private  HDecoder decoder;
//	
//	 public HCoderFactory() {  
//	        //this(Charset.defaultCharset());  
//	        this(Charset.forName("UTF-8"));  
//	  
//	    }  

	public HCoderFactory(HEncoder encoder,HDecoder decoder) {
	     this.encoder =encoder;  
	     this.decoder =decoder;  
	}

	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		return encoder;
	}

}
