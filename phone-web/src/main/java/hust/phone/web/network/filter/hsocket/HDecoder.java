package hust.phone.web.network.filter.hsocket;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.Messages.MAVLinkPayload;

import hust.phone.web.network.SLP.SlpPacket;



//解码工具
public class HDecoder extends CumulativeProtocolDecoder{

	public HDecoder()
	{
		
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		in.mark();//标记当前的position,以便后继的reset操作能够恢复position位置
		int b = in.get() &0xff;
		if(b!=0xfe)
		{
			int head =5;
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
			// System.out.println(len);
			 in.reset();
			//重置到position位置
			 byte [] encoding = new byte[(int) (len+8)];
			 in.get(encoding, 0, (int)(len+8));
			 SlpPacket packet = SlpPacket.parse(encoding);
			 if(packet!=null)
			 {
				 out.write(packet);
			 }
		}
		else {
			//智能鸟的解析
			short len = (short) (in.getUnsigned()+2+6);
			//System.out.println(len);
			in.reset();
			byte [] encoding = new byte[len];
			in.get(encoding, 0, len);
			//得到数据包的解析，判别数据的类型
			
			 Parser parser = new Parser();
				//先读出包的长度
			 int plen = encoding[1] & 0x00FF;
			 int slen = plen +2 +6;
			 for(int i=0;i<slen-3;i++)
			 {
				 int code = encoding[i] & 0x00FF;
				 parser.mavlink_parse_char(code);
			 }
			 MAVLinkPacket m1 = parser.mavlink_parse_char(encoding[slen-3]  & 0x00FF);
			 if(m1 != null && (m1.msgid==161|| m1.msgid ==162||m1.msgid==163))
			 {
				 MAVLinkPayload pay =m1.payload;
				 byte []p1=pay.payload.array();
				 SlpPacket packet = SlpPacket.parse(p1);
				 if(packet!=null)
				 {
					 out.write(packet);
				 }
			 }
		}
		return false;
	}

	
}
