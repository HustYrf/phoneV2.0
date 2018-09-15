package hust.phone.web.network.filter.unify;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import hust.phone.web.network.filter.hsocket.HCoderFactory;
import hust.phone.web.network.filter.websocket.MinaCoderFactory;
import hust.phone.web.network.iosession.IOSessionManager;


/*
 * 定义统一的协议工厂，分为无人机和手机端的
 */
public class UnifyProtocolCodecFactory implements ProtocolCodecFactory {
	//处理无人机的socket
	HCoderFactory hCoderFactory;
	//处理websocket
	MinaCoderFactory websocketServerCoderFactory;
	
	public UnifyProtocolCodecFactory(HCoderFactory hCoderFactory,MinaCoderFactory websocketServerCoderFactory)
	{			
		this.hCoderFactory = hCoderFactory;
		this.websocketServerCoderFactory = websocketServerCoderFactory;
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		if(IOSessionManager.isSessionMobile(session))
		{
			return websocketServerCoderFactory.getEncoder(session);
		}
		else {
			return hCoderFactory.getEncoder(session);
		}
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		if(IOSessionManager.isSessionMobile(session))
		{
			return websocketServerCoderFactory.getDecoder(session);
		}
		else {
			return hCoderFactory.getDecoder(session);
		}
	}



}
