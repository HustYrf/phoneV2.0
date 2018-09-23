package hust.phone.web.network.filter.hsocket;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import hust.phone.web.network.SLP.SlpPacket;



//解码工具
public class HDecoder extends CumulativeProtocolDecoder{

	public HDecoder()
	{
		
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		in.mark();//标记当前的position,以便后继的reset操作能够恢复position位置
		int head =6;
		 for(int i=0;i<head;i++)
		 {
			 in.get();
		 }
		 byte revlen []=new byte[4];
		 in.get(revlen);
		 byte sndlen[] =new byte[4];
		 sndlen[3]=revlen[0];
		 sndlen[2]=revlen[1];
		 sndlen[1]=revlen[2];
		 sndlen[0]=revlen[3];
		 
		 //获取长度
		 //long len = in.getUnsignedInt(6);
		 long len = SlpPacket.Byte2Int(sndlen)&0x0FFFFFFFFl;
		 System.out.println(len);
		 in.reset();
		//重置到position位置
		 byte [] encoding = new byte[(int) (len+8)];
		 in.get(encoding, 0, (int)(len+8));
		 SlpPacket packet = SlpPacket.parse(encoding);
		 if(packet!=null)
		 {
			 out.write(packet);
		 }
		
		return false;
	}

	
}
