package hust.phone.web.network.SLP.message;

import hust.phone.web.network.SLP.SlpAbstractMessage;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.SlpPayload;

public class SlpMsgPutTaskNum implements SlpAbstractMessage{
	public static final int MSG_LENGTH = 5;
	public static final short MSG_TYPE = 7;
	
	public long MISSION_ID ;
	public short RES_RELULT;

	public SlpMsgPutTaskNum()
	{
		
	}
	public SlpMsgPutTaskNum(SlpPacket slpPacket)
	{
		unpack(slpPacket.payload);
	}
	

	@Override
	public SlpPacket pack() {
		SlpPacket packet = new SlpPacket(MSG_LENGTH);
		packet.payload.putUnsignedInt(MISSION_ID);
		packet.payload.putUnsignedByte(RES_RELULT);
		packet.MSG_TYPE = MSG_TYPE;
		return packet;
	}

	@Override
	public void unpack(SlpPayload payload) {
		payload.resetIndex();
		this.MISSION_ID = payload.getUnsignedInt();
		this.RES_RELULT = payload.getUnsignedByte();
		
		
	}
	@Override
	public String toString()
	{
		return "SlpMsgPutTaskNum "+"MISSION_ID"+":"+MISSION_ID+" "+ "RES_RELULT"+":"+RES_RELULT;
		
	}

}
