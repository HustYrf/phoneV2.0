package hust.phone.web.network.filter.websocket;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MinaCoderFactory implements ProtocolCodecFactory{
	private MinaEncoder encoder;
	private MinaDecoder decoder;
	public MinaCoderFactory(MinaEncoder encoder,MinaDecoder decoder) {
		// TODO Auto-generated constructor stub
		this.encoder = encoder;
		this.decoder = decoder;
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return encoder;
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return decoder;
	}

}
