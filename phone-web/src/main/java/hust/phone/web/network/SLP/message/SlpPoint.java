package hust.phone.web.network.SLP.message;


import hust.phone.web.network.SLP.SlpPayload;
import hust.phone.web.network.common.ConstantUtils;

public class SlpPoint {
	private static final short POINT_LEN = ConstantUtils.POINT_LENGTH;
	public short WAYPOINT_TYPE ;
	public int WAYPOINT_NUM;
	public float WP_PARAM1;
	public float WP_PARAM2;
	public int WP_LAT;
	public int WP_LNG;
	public float WP_ALT;
	
	public static byte[] getPointEncoding(SlpPoint point)
	{
		SlpPayload payload = new SlpPayload(POINT_LEN);
		payload.putUnsignedByte(point.WAYPOINT_TYPE);
		payload.putUnsignedShort(point.WAYPOINT_NUM);
		payload.putFloat(point.WP_PARAM1);
		payload.putFloat(point.WP_PARAM2);
		payload.putInt(point.WP_LAT);
		payload.putInt(point.WP_LNG);
		payload.putFloat(point.WP_ALT);
		return payload.getData().array();
	}
	public static SlpPoint getPoint(byte poingcoding[])
	{
		SlpPayload payload = new SlpPayload(POINT_LEN);
		SlpPoint point = new SlpPoint();
		for(int i=0;i<poingcoding.length;i++)
		{
			payload.add(poingcoding[i]);
		}
		point.WAYPOINT_TYPE = payload.getUnsignedByte();
		point.WAYPOINT_NUM = payload.getUnsignedShort();
		point.WP_PARAM1 = payload.getFloat();
		point.WP_PARAM2 = payload.getFloat();
		point.WP_LAT = payload.getInt();
		point.WP_LNG = payload.getInt();
		point.WP_ALT = payload.getFloat();
		return point;
	}
	@Override
	public String toString() {
		
		 return "SlpPoint "+"WAYPOINT_TYPE"+":"+WAYPOINT_TYPE+" "+ "WAYPOINT_NUM"+":"+WAYPOINT_NUM +"WP_PARAM1"+":"+WP_PARAM1+"WP_PARAM2"+":"+WP_PARAM2+"WP_LAT"+":"+WP_LAT+" "+"WP_LNG"+":"+WP_LNG+" "
				+"WP_ALT"+":"+WP_ALT;
	}
}
