package hust.phone.web.network.SLP.message;

import hust.phone.web.network.SLP.SlpAbstractMessage;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.SlpPayload;

public class SlpMsgLogin implements SlpAbstractMessage {
	public static final int MSG_LENGTH = 2;
	public static final short MSG_TYPE = 4;
	
	public short UAV_LOGIN;
	public short RES_RELULT;
	
	public SlpMsgLogin()
	{
		
	}
	public SlpMsgLogin(SlpPacket slpPacket) {
		unpack(slpPacket.payload);
	}

	public SlpPacket pack() {
		SlpPacket packet = new SlpPacket(MSG_LENGTH);
		packet.payload.putUnsignedByte(UAV_LOGIN);
		packet.payload.putUnsignedByte(RES_RELULT);
		packet.MSG_TYPE = MSG_TYPE;
		return packet;
	}

	public void unpack(SlpPayload payload) {
		payload.resetIndex();
		this.UAV_LOGIN = payload.getUnsignedByte();
		this.RES_RELULT = payload.getUnsignedByte();
	}
	@Override
	public String toString()
	{
		return "SlpMsgLogin "+"UAV_LOGIN"+":"+UAV_LOGIN+" "+ "RES_RELULT"+":"+RES_RELULT;
		
	}

}
