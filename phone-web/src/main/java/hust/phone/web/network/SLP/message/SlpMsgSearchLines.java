package hust.phone.web.network.SLP.message;

import hust.phone.web.network.SLP.SlpAbstractMessage;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.SlpPayload;

public class SlpMsgSearchLines implements SlpAbstractMessage{
	public static final int MSG_LENGTH = 9;
	public static final short MSG_TYPE = 0x81;
	public long ROUTE_ID ;
	public int ROUTE_COUNT;
	public int ROUTE_STOCK_COUNT;
	public short RES_RELULT;
	
	public SlpMsgSearchLines()
	{
		
	}
	public SlpMsgSearchLines(SlpPacket slpPacket)
	{
		unpack(slpPacket.payload);
	}

	@Override
	public SlpPacket pack() {
		SlpPacket packet = new SlpPacket(MSG_LENGTH);
		packet.payload.putUnsignedInt(ROUTE_ID);
		packet.payload.putUnsignedShort(ROUTE_COUNT);
		packet.payload.putUnsignedShort(ROUTE_STOCK_COUNT);
		packet.payload.putUnsignedByte(RES_RELULT);
		packet.MSG_TYPE = MSG_TYPE;
		return packet;
	}

	@Override
	public void unpack(SlpPayload payload) {
		payload.resetIndex();
		this.ROUTE_ID = payload.getUnsignedInt();
		this.ROUTE_COUNT = payload.getUnsignedShort();
		this.ROUTE_STOCK_COUNT = payload.getUnsignedShort();
		this.RES_RELULT = payload.getUnsignedByte();
	}
	@Override
	public String toString() {
		return "SlpMsgSearchLines "+"ROUTE_ID"+":"+ROUTE_ID+" "+ "ROUTE_COUNT"+":"+ROUTE_COUNT +" "+"ROUTE_STOCK_COUNT"+":"+ROUTE_STOCK_COUNT+" "
				+"RES_RELULT"+":"+RES_RELULT;
	}
}
