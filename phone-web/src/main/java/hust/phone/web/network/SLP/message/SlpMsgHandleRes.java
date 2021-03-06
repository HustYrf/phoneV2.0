package hust.phone.web.network.SLP.message;

import hust.phone.web.network.SLP.SlpAbstractMessage;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.SlpPayload;

public class SlpMsgHandleRes implements SlpAbstractMessage {
	public static final int MSG_LENGTH = 2;
	public static final short MSG_TYPE = 3;
	
	public short COM_TYPE;
	public short RES_RELULT;
	
	public SlpMsgHandleRes() {
		// TODO Auto-generated constructor stub
	}
	public SlpMsgHandleRes(SlpPacket slpPacket) {
		unpack(slpPacket.payload);
	}
	public SlpPacket pack() {
		SlpPacket packet = new SlpPacket(MSG_LENGTH);
		packet.payload.putUnsignedByte(COM_TYPE);
		packet.payload.putUnsignedByte(RES_RELULT);
		packet.MSG_TYPE = MSG_TYPE;
		return packet;
	}
	public void unpack(SlpPayload payload) {
		payload.resetIndex();
		this.COM_TYPE = payload.getUnsignedByte();
		this.RES_RELULT = payload.getUnsignedByte();
	}
	
	@Override
	public String toString()
	{
		return "SlpMsgHandleRes "+"COM_TYPE"+":"+COM_TYPE+" "+ "RES_RELULT"+":"+RES_RELULT;
		
	}

}
