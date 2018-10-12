package hust.phone.web.network.SLP;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.common.msg_new_login_res;

import hust.phone.web.network.SLP.message.SlpMsgLogin;
import hust.phone.web.network.SLP.message.SlpMsgStatus;
import hust.phone.web.network.common.ConstantUtils;


public class test {
	
	@Test
	public void test1()
	{
		SlpMsgLogin mes=new SlpMsgLogin();
		mes.UAV_LOGIN=1;
		mes.RES_RELULT=1;
		SlpPacket pack = mes.pack();
		pack.MSG_SEQ=1;
		pack.MSG_TIME=1111111;
		pack.SND_DEVICE_ID=1;
		pack.REV_DEVICE_ID=1;
		byte[] encoding = pack.encoding();
		System.out.println(Arrays.toString(encoding));
		long i = 1l;
		byte s1[]= new byte[(int)i];
		
	}
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
    public short CHECKSUM;

	public static void main(String[] args) {
		byte b []= {0x54,0x45,0x4c,0x55,0x41,0x56,0x40,0x00,0x00,0x00,0x0E,0x00,0x00,0x00,0x00,0x00,(byte)0xC1,(byte)0xAA,0x01,0x02,0x01,0x01,0x0B,0x00,0x00,0x00,0x00,(byte)0xFF,(byte)0xBD,0x5E,0x10,0x3F,0x00,0x00,0x00,
    			0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte)0xFB,(byte)0xFF,0x07,0x00,(byte)0x80,(byte)0xDE,0x48,0x12,0x02,0x12,0x00,0x00,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0x17,(byte)0x95};
		System.out.println(b.length);
		System.err.println(Arrays.toString(b));
		SlpPacket parse = SlpPacket.parse(b);
		SlpMsgStatus unpack = (SlpMsgStatus) parse.unpack();
		System.out.println(parse.toString());
		System.out.println(unpack.toString());
		SlpPacket pack = unpack.pack();
		pack.MSG_SEQ=158;
		pack.REV_DEVICE_ID=0;
		pack.SND_DEVICE_ID=33663681;
		pack.MSG_TIME=20180907083929l;
		pack.VAL_TIME=-1;
		byte[] encoding = pack.encoding();
		System.out.println(Arrays.toString(encoding));
		
		
	}
	public static byte getbyte(byte i)
    {
    	 // 高四位
        byte high4 = (byte) (i & 240) ; //240的二进制 11110000
        // 低四位
        byte low4 = (byte) (i & 15); // 15的二进制形式 00001111
        byte j = (byte) ((low4<<4) + (high4>>4));
        return j;
    }
	@Test
	public void test2()
	{
//		 	byte i = (byte) 0xaa;// 二进制表示 01000101
//         
//	        // 高四位
//	        byte high4 = (byte) (i & 240) ; //240的二进制 11110000
//	        // 低四位
//	        byte low4 = (byte) (i & 15); // 15的二进制形式 00001111
//	         
//	        //System.out.println(high4>>4);
//	        //System.out.println(low4<<4);
//	         
//	        byte j = (byte) ((low4<<4) + (high4>>4));
//	         
//	        System.out.println(getbyte(i));// 84的二进制形式 01010100
		byte i=(byte) 254;
		System.out.println(i);
	}
	
	@Test
	public void test3()
	{
		byte b []= {0x54,0x45,0x4C,0x55,0x41,0x56,0x21,0x00,0x00,0x00,(byte)0x9E,0x00,0x00,0x00,0x00,0x00,(byte)0xC1,(byte)0xAA,
				0x01,0x02,0x01,0x01,0x01,0x00,0x00,0x00,0x01,0x0A,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte)0x93,(byte)0xFE,
				(byte)0xEB,(byte)0xEA,0x16,(byte)0xCF,(byte)0xE8,0x58,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte)0x99,0x48,(byte)0xCC,
				(byte)0xBB,0x5A,0x12,0x00,0x00,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,0x61,0x71};
		System.out.println(b.length);
		
		SlpPacket parse = SlpPacket.parse(b);
		SlpMsgStatus unpack = (SlpMsgStatus) parse.unpack();
		SlpPacket pack = unpack.pack();
		pack.MSG_SEQ=158;
		pack.REV_DEVICE_ID=0;
		pack.SND_DEVICE_ID=33663681;
		pack.MSG_TIME=20180907083929l;
		pack.VAL_TIME=-1;
		System.out.println(parse.toString());
		System.out.println(unpack.toString());
	}
	@Test
	public void test4()
	{
		msg_new_login_res loginRes = new msg_new_login_res();
		loginRes.HEAD[0]= 0x54;
		loginRes.HEAD[1]=0x45;
		loginRes.HEAD[2]=0x4C;
		loginRes.HEAD[3]=0x55;
		loginRes.HEAD[4]=0x41;
		loginRes.HEAD[5]=0x56;
		loginRes.MSG_TYPE=5;
		loginRes.MSG_LEN =33;
		loginRes.REV_DEVICE_ID =0x03010001;
		loginRes.SND_DEVICE_ID = 0x03010001;
		loginRes.RES_RELULT = 1;
		loginRes.UAV_LOGIN = 1;
		loginRes.MSG_TIME = 0;
		loginRes.VAL_TIME = 0;
		loginRes.CHECKSUM = 0;
		MAVLinkPacket pack = loginRes.pack();
		pack.sysid = 1;
		byte[] encodePacket = pack.encodePacket();
		System.out.println(Arrays.toString(encodePacket));
		 Parser parser = new Parser();
		//先读出包的长度
		 int plen = encodePacket[1] & 0x00FF;
		 int len = plen +2 +6;
		// System.out.println(len);
		 //byte [] lenbuf = new  byte[len];
		 for(int i=0;i<len-1;i++)
		 {
			 int code = encodePacket[i] & 0x00FF;
			 parser.mavlink_parse_char(code);
		 }
		 MAVLinkPacket m = parser.mavlink_parse_char(encodePacket[len-1]  & 0x00FF);
		 msg_new_login_res login=(msg_new_login_res) m.unpack();
		 long s =login.SND_DEVICE_ID;
		 byte type[]= SlpPacket.IntToByte((int)s);
		 int uavId = (type[2] &0xff)*16+(type[3] &0xff);
		 
		System.out.println(login.toString());
		System.err.println(uavId);
	}
	
	@Test
	public void test5() throws Exception
	{
		Date currentTime = new Date(); 
		//改变输出格式（自己想要的格式） 
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 //得到字符串时间 
		 String s8 = formatter.format(currentTime); 
		 System.out.println(s8);
		Date date = formatter.parse(s8);
		long s = date.getTime();
		System.out.println(s);

		
	}
}
