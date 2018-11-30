package hust.phone.web.network.SLP;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.common.msg_new_login_res;

import hust.phone.web.network.SLP.message.SlpMsgLogin;
import hust.phone.web.network.SLP.message.SlpMsgPutLines;
import hust.phone.web.network.SLP.message.SlpMsgSearchLines;
import hust.phone.web.network.SLP.message.SlpMsgStatus;
import hust.phone.web.network.SLP.message.SlpPoint;
import hust.phone.web.network.common.ConstantUtils;
import hust.phone.web.network.common.PointSToWayEncodingUtils;


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
		SlpMsgStatus msg = new SlpMsgStatus();
		msg.CUSTMODE= 1;
		msg.GPS_ELV=1111;
		msg.GPS_HDG=23;
		SlpPacket pack = msg.pack();
		pack.SND_DEVICE_ID = 1;
		byte[] encoding = pack.encoding();
		SlpPacket parse = SlpPacket.parse(encoding);
		System.out.println(parse.toString());
		SlpMsgStatus msg2=(SlpMsgStatus) parse.unpack();
		System.out.println(msg2.toString());
	}
	
	@Test
	public void test5() throws Exception
	{
		byte b[] = {84, 69, 76, 85, 65, 86, 33, 0, 0, 0, 0, 0, 1, 0, 1, 3, 1, 0, 1, 3, 4, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		SlpPacket parse = SlpPacket.parse(b);
		SlpMsgLogin login = (SlpMsgLogin) parse.unpack();
		System.out.println(login.toString());
	
	}
	@Test
	public void test6()
	{
		SlpMsgSearchLines msg = new SlpMsgSearchLines();
		msg.ROUTE_ID =111;
		msg.ROUTE_COUNT = 10001;
		msg.ROUTE_STOCK_COUNT = 100;
		msg.RES_RELULT = 1;
		System.out.println(msg.toString());
		SlpPacket pack = msg.pack();
		byte[] encoding = pack.encoding();
		System.out.println(Arrays.toString(encoding));
		SlpPacket parse = SlpPacket.parse(encoding);
		System.out.println(parse.toString());
		SlpMsgSearchLines msg2 =(SlpMsgSearchLines) parse.unpack();
		System.out.println(msg2.toString());
	}
	@Test
	public void test7()
	{
		SlpPoint point1 = new SlpPoint();
		point1.WAYPOINT_TYPE = 1;
		point1.WAYPOINT_NUM = 1;
		point1.WP_LAT = 1111111l;
		point1.WP_LNG = 222222222l;
		point1.WP_ALT = 333333l;
		
		SlpPoint point2 = new SlpPoint();
		point2.WAYPOINT_TYPE = 2;
		point2.WAYPOINT_NUM = 2;
		point2.WP_LAT = 111112l;
		point2.WP_LNG = 22222422l;
		point2.WP_ALT = 333332l;
		
		ArrayList<SlpPoint> list = new ArrayList<SlpPoint>();
		list.add(point1);
		list.add(point2);
		
		byte[] pointSToWayEncoding = PointSToWayEncodingUtils.PointSToWayEncoding(list);
		SlpMsgPutLines msg  = new SlpMsgPutLines();
		msg.Route_ID = 23434;
		msg.ROUTE_COUNT = 2;
		msg.ROUTE_MSG_COUNT = 2;
		msg.POINTS= new short[msg.ROUTE_MSG_COUNT * 15];
		for(int i= 0;i<pointSToWayEncoding.length;i++)
		{
			msg.POINTS[i] = (short) (pointSToWayEncoding[i]&0xff);
		}
		SlpPacket pack = msg.pack();
		pack.REV_DEVICE_ID =2222l;
		pack.SND_DEVICE_ID = 33333l;
		byte[] encoding = pack.encoding();
		System.out.println(Arrays.toString(encoding));
		
		SlpPacket parse = SlpPacket.parse(encoding);
		System.out.println(parse.toString());
		SlpMsgPutLines unpack = (SlpMsgPutLines) parse.unpack();
		System.out.println(unpack.toString());
		
		ArrayList<SlpPoint> list2 = PointSToWayEncodingUtils.WayToPoints((unpack.POINTS));
		for(SlpPoint l:list2)
		{
			System.out.println(l.toString());
		}
		
	}
	@Test
	public void test8()
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		System.out.println(list.size());
		list.removeAll(list);
		System.out.println(list.size());
		list.add(1);
		System.out.println(list.size());
	}
}
