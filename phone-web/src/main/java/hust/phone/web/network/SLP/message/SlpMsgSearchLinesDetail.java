package hust.phone.web.network.SLP.message;

import hust.phone.web.network.SLP.SlpAbstractMessage;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.SlpPayload;

public class SlpMsgSearchLinesDetail implements SlpAbstractMessage{
	public static final short MSG_TYPE = 0x82;
	public static final short POINT_LEN = 15;
	
    public long ROUTE_ID;
    public int ROUTE_COUNT;
    public int ROUTE_MSG_COUNT;
    public short POINTS[];
    public short RES_RELULT;
    
    public SlpMsgSearchLinesDetail() {
		// TODO Auto-generated constructor stub
	}
	public SlpMsgSearchLinesDetail(SlpPacket slpPacket) {
		unpack(slpPacket.payload);
	}

	@Override
	public SlpPacket pack() {
		SlpPacket packet = new SlpPacket(ROUTE_MSG_COUNT * POINT_LEN+9);
		packet.MSG_TYPE = MSG_TYPE;
		packet.payload.putUnsignedInt(ROUTE_ID);
		packet.payload.putUnsignedShort(ROUTE_COUNT);
		packet.payload.putUnsignedShort(ROUTE_MSG_COUNT);
		
		for(int i = 0;i<ROUTE_MSG_COUNT *POINT_LEN ;i++)
		{
			packet.payload.putUnsignedByte(POINTS[i]);
		}
		packet.payload.putUnsignedByte(RES_RELULT);
		return packet;
	}

	@Override
	public void unpack(SlpPayload payload) {
		payload.resetIndex();
		this.ROUTE_ID = payload.getUnsignedInt();
		this.ROUTE_COUNT = payload.getUnsignedShort();
		this.ROUTE_MSG_COUNT = payload.getUnsignedShort();
		this.POINTS= new short[this.ROUTE_MSG_COUNT * 15];
		for(int i= 0;i<this.ROUTE_COUNT* POINT_LEN ;i++)
		{
			this.POINTS[i] = payload.getUnsignedByte();
		}
		this.RES_RELULT = payload.getUnsignedByte();
	}

}
