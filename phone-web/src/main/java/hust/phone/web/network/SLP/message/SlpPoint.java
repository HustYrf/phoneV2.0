package hust.phone.web.network.SLP.message;


import hust.phone.web.network.SLP.SlpPayload;

public class SlpPoint {
	private static final short POINT_LEN = 15;
	public short WAYPOINT_TYPE ;
	public int WAYPOINT_NUM;
	public Long WP_LAT;
	public long WP_LNG;
	public long WP_ALT;
	
	public static byte[] getPointEncoding(SlpPoint point)
	{
		SlpPayload payload = new SlpPayload(POINT_LEN);
		payload.putUnsignedByte(point.WAYPOINT_TYPE);
		payload.putUnsignedShort(point.WAYPOINT_NUM);
		payload.putUnsignedInt(point.WP_LAT);
		payload.putUnsignedInt(point.WP_LNG);
		payload.putUnsignedInt(point.WP_ALT);
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
		point.WP_LAT = payload.getUnsignedInt();
		point.WP_LNG = payload.getUnsignedInt();
		point.WP_ALT = payload.getUnsignedInt();
		return point;
	}
	@Override
	public String toString() {
		
		 return "SlpPoint "+"WAYPOINT_TYPE"+":"+WAYPOINT_TYPE+" "+ "WAYPOINT_NUM"+":"+WAYPOINT_NUM +" "+"WP_LAT"+":"+WP_LAT+" "+"WP_LNG"+":"+WP_LNG+" "
				+"WP_ALT"+":"+WP_ALT;
	}
}
