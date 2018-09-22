package hust.phone.web.network.SLP;

import java.io.Serializable;
import java.util.Arrays;

import hust.phone.web.network.SLP.message.SLpCRC;
import hust.phone.web.network.SLP.message.SlpMsgHandle;
import hust.phone.web.network.SLP.message.SlpMsgHandleRes;
import hust.phone.web.network.SLP.message.SlpMsgLogin;
import hust.phone.web.network.SLP.message.SlpMsgLoginRes;
import hust.phone.web.network.SLP.message.SlpMsgStatus;
import hust.phone.web.network.common.ConstantUtils;



public class SlpPacket implements Serializable{
	
	// @Fields serialVersionUID : TODO
	private static final long serialVersionUID = 9111313676930333810L;
	//消息前导字6
	public short HEAD[] = new short[6];
	//消息长度4
	public long MSG_LEN;
	//消息序列号2
	public int MSG_SEQ;
	//接收设备4
	public long REV_DEVICE_ID;
	//发送设备4
	public long SND_DEVICE_ID;
	//消息类型1
	public short MSG_TYPE;
	//消息时间8
	public long MSG_TIME;
	//有效时间8
    public long VAL_TIME;
	//消息体
	public SlpPayload payload;
    //校验码2
    public int CHECKSUM;
    
	public SlpPacket(int payloadLength)
	{
		HEAD = new short[6];
		HEAD[0] = 0x54;
		HEAD[1] = 0x45;
		HEAD[2] = 0x4c;
		HEAD[3] = 0x55;
		HEAD[4] = 0x41;
		HEAD[5] = 0x56;
		this.MSG_LEN = payloadLength+31;
		payload = new SlpPayload(payloadLength);
	
	}

	public  byte[] encoding()
	{
		byte[] buffer = new byte[(int) (MSG_LEN+8)];
		int i = 0;
		for (int k = 0; k < HEAD.length; k++) {
			buffer[i++] = (byte) HEAD[k];
		}
		byte msg_len[] = IntToByte((int) MSG_LEN);
		for(int k = msg_len.length-1; k >=0; k--)
		{
			buffer[i++] = msg_len[k];
		}
		byte seq[] = Uni16ToByte(MSG_SEQ);
		for(int k = seq.length-1; k >=0; k--)
		{
			buffer[i++] = seq[k];
		}
		byte rev[]=IntToByte((int) REV_DEVICE_ID);
		for(int k = rev.length-1; k>=0; k--)
		{
			buffer[i++] = rev[k];
		}
		byte snd[]=IntToByte((int) SND_DEVICE_ID);
		for(int k = snd.length-1; k>=0; k--)
		{
			buffer[i++] = snd[k];
		}
		buffer[i++] = (byte) MSG_TYPE;
		
		final int payloadSize = payload.size();
		for (int j = 0; j < payloadSize; j++) {
			buffer[i++] = payload.payload.get(j);
		}
		byte msgtime[]=long2Bytes(MSG_TIME);
		for(int k = msgtime.length-1;k>=0; k--)
		{
			buffer[i++] = msgtime[k];
		}
		byte valtime[]=long2Bytes(VAL_TIME);
		for(int k = valtime.length-1; k >=0; k--)
		{
			buffer[i++] = valtime[k];
		}
//		byte check = 0;
//		// 生成校验位
//		for (int h = 0; h < i; h++) {
//			check += buffer[h];
//		}
		//byte data []= new byte[buffer.length-8];
		
		byte data[]=Arrays.copyOfRange(buffer, 6, i);
		byte[] check=Uni16ToByte(SLpCRC.calculateCrc(data));
		buffer[i++] = check[1];
		buffer[i]=check[0];
		return buffer;
	}
	
	public static SlpPacket parse(byte[] encoding)
	{
			if((encoding[0]&0x00ff)==0x54 &&(encoding[1]&0x00ff)==0x45 && (encoding[2]&0x00ff)==0x4c
					&&(encoding[3]&0x00ff)==0x55&&(encoding[4]&0x00ff)==0x41&&(encoding[5]&0x00ff)==0x56 )
			{
				byte len []=new byte[4];
				len[3]=encoding[6];
				len[2]=encoding[7];
				len[1]=encoding[8];
				len[0]=encoding[9];
				long c=Byte2Int(len)&0x0FFFFFFFFl;
				//生成packet
				SlpPacket slp = new SlpPacket((int)(c-31));
				byte seq[] =  new byte[2];
				seq[1]=encoding[10];
				seq[0]=encoding[11];
				slp.MSG_SEQ=Byte2Uni16(seq)&0xFFFF;
				byte rev []=new byte[4];
				rev[3]=encoding[12];
				rev[2]=encoding[13];
				rev[1]=encoding[14];
				rev[0]=encoding[15];
				slp.REV_DEVICE_ID=Byte2Int(rev)&0x0FFFFFFFFl;
				byte snd []=new byte[4];
				snd[3]=encoding[16];
				snd[2]=encoding[17];
				snd[1]=encoding[18];
				snd[0]=encoding[19];
				slp.SND_DEVICE_ID=Byte2Int(snd)&0x0FFFFFFFFl;
				slp.MSG_TYPE =(short) (encoding[20]&0xff);
				
				for(int i=21;i<encoding.length-18;i++)
				{
					slp.payload.add(encoding[i]);
				}
				
				byte msgtime []=new byte[8];
				msgtime[7]=encoding[encoding.length-18];
				msgtime[6]=encoding[encoding.length-17];
				msgtime[5]=encoding[encoding.length-16];
				msgtime[4]=encoding[encoding.length-15];
				msgtime[3]=encoding[encoding.length-14];
				msgtime[2]=encoding[encoding.length-13];
				msgtime[1]=encoding[encoding.length-12];
				msgtime[0]=encoding[encoding.length-11];
				slp.MSG_TIME=bytes2Long(msgtime)&0xFFFFFFFFFFFFFFFFl;
				byte valtime []=new byte[8];
				valtime[7]=encoding[encoding.length-10];
				valtime[6]=encoding[encoding.length-9];
				valtime[5]=encoding[encoding.length-8];
				valtime[4]=encoding[encoding.length-7];
				valtime[3]=encoding[encoding.length-6];
				valtime[2]=encoding[encoding.length-5];
				valtime[1]=encoding[encoding.length-4];
				valtime[0]=encoding[encoding.length-3];
				slp.VAL_TIME=bytes2Long(valtime)&0xFFFFFFFFFFFFFFFFl;
				//得到
				byte check[]=new byte[2];
				check[1]=encoding[encoding.length-2];
				check[0]=encoding[encoding.length-1];
				slp.CHECKSUM = Byte2Uni16(check)&0xFFFF;
				
				byte data[]=Arrays.copyOfRange(encoding, 6, encoding.length-2);
				byte[] check1=Uni16ToByte(SLpCRC.calculateCrc(data));
				if(check[0]==check1[0]&& check[1]==check1[1])
				{
					return slp;
				}else
				{
					return null;
				}
			}
			else {
				return null;
			}
		
	}
	public SlpAbstractMessage unpack()
	{
		switch (MSG_TYPE) {
		case ConstantUtils.MSG_STATUS:
			return new SlpMsgStatus(this);
		case ConstantUtils.MSG_HANDLE:
			return new SlpMsgHandle(this);
		case ConstantUtils.MSG_LOGIN:
			return new SlpMsgLogin(this);
		case ConstantUtils.MSG_LOGINRES:
			return new SlpMsgLoginRes(this);
		case ConstantUtils.MSG_HANDLERES:
			return new SlpMsgHandleRes(this);
		default:
			return null;
		}
	}
	@Override
	public String toString()
	{
		return "SlpPacket "+"MSG_LEN"+":"+MSG_LEN+" "+ "MSG_SEQ"+":"+MSG_SEQ+"REV_DEVICE_ID"+":"+REV_DEVICE_ID+" "+ "SND_DEVICE_ID"+":"+SND_DEVICE_ID
				+"MSG_TYPE"+":"+MSG_TYPE+" "+ "MSG_TIME"+":"+MSG_TIME+"VAL_TIME"+":"+VAL_TIME+" "+"CHECKSUM:"+CHECKSUM;
		
	}
	
	//64位
	public static byte[] long2Bytes(long num) {
		byte[] byteNum = new byte[8];
		for (int ix = 0; ix < 8; ++ix) {
			int offset = 64 - (ix + 1) * 8;
			byteNum[ix] = (byte) ((num >> offset) & 0xff);
		}
		return byteNum;
	}
	//64位
	public static long bytes2Long(byte[] byteNum) {
		long num = 0;
		for (int ix = 0; ix < 8; ++ix) {
			num <<= 8;
			num |= (byteNum[ix] & 0xff);
		}
		return num;
	}
	//32位
	public static int Byte2Int(byte[]bytes) {
		return (bytes[0]&0xff)<<24
			| (bytes[1]&0xff)<<16
			| (bytes[2]&0xff)<<8
			| (bytes[3]&0xff);
	}
	//32位
	public static byte[]IntToByte(int num){
		byte[]bytes=new byte[4];
		bytes[0]=(byte) ((num>>24)&0xff);
		bytes[1]=(byte) ((num>>16)&0xff);
		bytes[2]=(byte) ((num>>8)&0xff);
		bytes[3]=(byte) (num&0xff);
		return bytes;
	}
	//16位
	public static int Byte2Uni16(byte[]bytes)
	{
		return (bytes[0]&0xff)<<8	| (bytes[1]&0xff);
	}
	//16位
	public static byte[] Uni16ToByte(int num)
	{
		byte [] bytes = new byte[2];
		bytes[0] = (byte) ((num>>8)&0xff);
		bytes[1]=(byte) (num&0xff);
		return bytes;
		
	}
	
	public static byte[] float2byte(float f) {
		
		// 把float转换为byte[]
		int fbit = Float.floatToIntBits(f);
		
		byte[] b = new byte[4];  
	    for (int i = 0; i < 4; i++) {  
	        b[i] = (byte) (fbit >> (24 - i * 8));  
	    } 
	    
	    // 翻转数组
		int len = b.length;
		// 建立一个与源数组元素类型相同的数组
		byte[] dest = new byte[len];
		// 为了防止修改源数组，将源数组拷贝一份副本
		System.arraycopy(b, 0, dest, 0, len);
		byte temp;
		// 将顺位第i个与倒数第i个交换
		for (int i = 0; i < len / 2; ++i) {
			temp = dest[i];
			dest[i] = dest[len - i - 1];
			dest[len - i - 1] = temp;
		}
	    
	    return dest;
	    
	}
}
