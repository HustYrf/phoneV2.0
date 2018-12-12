package hust.phone.web.network.filter.hsocket;


import java.util.Arrays;

//import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.common.ConstantUtils;



//解码工具
public class HDecoder extends CumulativeProtocolDecoder{

	public HDecoder()
	{
		
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		//System.out.println(Thread.currentThread().getName());
		if (in.remaining() >=ConstantUtils.BASE_LENGTH) {
			while(true)
			{
				in.mark();
				//读取头
				byte head []= new byte[6];
				in.get(head);
				if((head[0]&0x00ff)==0x54 &&(head[1]&0x00ff)==0x45 && (head[2]&0x00ff)==0x4c
						&&(head[3]&0x00ff)==0x55&&(head[4]&0x00ff)==0x41&&(head[5]&0x00ff)==0x56)
				{
					break;
				}
				//没有读到头，重置，并略过一个字节
				in.reset();
				in.get();
				if(in.remaining()<ConstantUtils.BASE_LENGTH)
				{
					//长度不够继续读
					return false;
				}
			}
			//处理
			//版本号
			short MSG_VER = (short) (in.get()& 0xff);
			byte revlen[] = new byte[2];
			in.get(revlen);
			byte sndlen[] = new byte[2];
			sndlen[1] = revlen[0];
			sndlen[0] = revlen[1];
			// 获取长度
			int len = (SlpPacket.Byte2Uni16(sndlen) & 0x0FFFF)+8;
			in.reset();
			// 重置到position位置
			if (len > in.remaining()) {
				// 消息内容不够，则继续读
				return false;
			} else {
				byte[] encoding = new byte[(int) len];
				in.get(encoding, 0, len);
				SlpPacket packet = SlpPacket.parse(encoding);
				if (packet != null) {
					out.write(packet);
				}
				if (in.remaining() > 0) {
					// 如果读取内容后还粘了包，进行下一次解析
					return true;
				}
			}	
		}
		//处理下个包
		return false;
	}
}
