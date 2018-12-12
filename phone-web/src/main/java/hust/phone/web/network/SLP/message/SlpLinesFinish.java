package hust.phone.web.network.SLP.message;

import hust.phone.web.network.SLP.SlpAbstractMessage;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.SlpPayload;

public class SlpLinesFinish implements SlpAbstractMessage{
	public static final short MSG_TYPE = 8;
	public static final int MSG_LENGTH = 13;
	
	public long ROUTE_ID;
	public int ROUTE_COUNT;
	public long ERR_CODE;
	public int WAYPOINT_NUM;
	public short RES_RELULT;
	
	public SlpLinesFinish() {
	}
	public SlpLinesFinish(SlpPacket slpPacket)
	{
		unpack(slpPacket.payload);
	}
	@Override
	public SlpPacket pack() {
		SlpPacket packet = new SlpPacket(MSG_LENGTH);
		packet.payload.putUnsignedInt(ROUTE_ID);
		packet.payload.putUnsignedShort(ROUTE_COUNT);
		packet.payload.putUnsignedInt(ERR_CODE);
		packet.payload.putUnsignedShort(WAYPOINT_NUM);
		packet.payload.putUnsignedByte(RES_RELULT);
		packet.MSG_TYPE = MSG_TYPE;
		return packet;
	}

	@Override
	public void unpack(SlpPayload payload) {
		payload.resetIndex();
		this.ROUTE_ID = payload.getUnsignedInt();
		this.ROUTE_COUNT = payload.getUnsignedShort();
		this.ERR_CODE = payload.getUnsignedInt();
		this.WAYPOINT_NUM = payload.getUnsignedShort();
		this.RES_RELULT = payload.getUnsignedByte();
		
	}
	
	@Override
	public String toString()
	{
		return "SlpLinesFinish "+"ROUTE_ID"+":"+ROUTE_ID+" "+ "ROUTE_COUNT"+":"+ROUTE_COUNT+ "ERR_CODE"+":"+ERR_CODE+"WAYPOINT_NUM"+":"+WAYPOINT_NUM+"RES_RELULT"+":"+RES_RELULT;
		
	}
	

}
