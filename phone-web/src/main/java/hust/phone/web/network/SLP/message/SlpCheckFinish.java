package hust.phone.web.network.SLP.message;

import hust.phone.web.network.SLP.SlpAbstractMessage;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.SlpPayload;

public class SlpCheckFinish implements SlpAbstractMessage{
	public static final short MSG_TYPE = 9;
	public static final int MSG_LENGTH = 5;
	
	public long ERR_CODE;
	public short RES_RELULT;
	
	public SlpCheckFinish() {
	}
	public SlpCheckFinish(SlpPacket slpPacket) {
		unpack(slpPacket.payload);
	}

	@Override
	public SlpPacket pack() {
		SlpPacket packet = new SlpPacket(MSG_LENGTH);
		packet.payload.putUnsignedInt(ERR_CODE);
		packet.payload.putUnsignedByte(RES_RELULT);
		packet.MSG_TYPE = MSG_TYPE;
		return packet;
	}

	@Override
	public void unpack(SlpPayload payload) {
		payload.resetIndex();
		this.ERR_CODE = payload.getUnsignedInt();
		this.RES_RELULT = payload.getUnsignedByte();
	}
	
	@Override
	public String toString()
	{
		return "SlpCheckFinish "+"ERR_CODE"+":"+ERR_CODE+" "+ "RES_RELULT"+":"+RES_RELULT;
		
	}

}
