package hust.phone.web.network.SLP.message;

import hust.phone.web.network.SLP.SlpAbstractMessage;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.SlpPayload;

public class SlpMsgPutLines implements SlpAbstractMessage{
	public static final short MSG_TYPE = 6;
	public static final short POINT_LEN = 15;
	public long Route_ID;
	public int ROUTE_COUNT;
	public int ROUTE_MSG_COUNT;
	public short POINTS[];

	public SlpMsgPutLines()
	{
		
	}
	public SlpMsgPutLines(SlpPacket slpPacket) {
		unpack(slpPacket.payload);
	}

	@Override
	public SlpPacket pack() {
		
		SlpPacket packet = new SlpPacket(ROUTE_MSG_COUNT * POINT_LEN+8);
		packet.MSG_TYPE = MSG_TYPE;
		packet.payload.putUnsignedInt(Route_ID);
		packet.payload.putUnsignedShort(ROUTE_COUNT);
		packet.payload.putUnsignedShort(ROUTE_MSG_COUNT);
		
		for(int i = 0;i<ROUTE_MSG_COUNT *POINT_LEN ;i++)
		{
			packet.payload.putUnsignedByte(POINTS[i]);
		}

		return packet;
	}

	@Override
	public void unpack(SlpPayload payload) {
		payload.resetIndex();
		this.Route_ID = payload.getUnsignedInt();
		this.ROUTE_COUNT = payload.getUnsignedShort();
		this.ROUTE_MSG_COUNT = payload.getUnsignedShort();
		this.POINTS= new short[this.ROUTE_MSG_COUNT * 15];
		for(int i= 0;i<this.ROUTE_COUNT* POINT_LEN ;i++)
		{
			this.POINTS[i] = payload.getUnsignedByte();
		}
		
	}
	
	@Override
	public String toString()
	{
		return "SlpMsgPutLines "+"Route_ID"+":"+Route_ID+" "+ "ROUTE_COUNT"+":"+ROUTE_COUNT+"ROUTE_MSG_COUNT"+":"+ROUTE_MSG_COUNT;
		
	}
}
