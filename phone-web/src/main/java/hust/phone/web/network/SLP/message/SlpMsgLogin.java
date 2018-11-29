package hust.phone.web.network.SLP.message;

import hust.phone.web.network.SLP.SlpAbstractMessage;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.SlpPayload;

public class SlpMsgLogin implements SlpAbstractMessage {
	public static final int MSG_LENGTH = 14;
	public static final short MSG_TYPE = 4;
	
	public short UAV_LOGIN;
	public long FMU_KEY[] = new long [3];
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
		for(int i=0;i<FMU_KEY.length;i++)
		{
			packet.payload.putUnsignedInt(FMU_KEY[i]);
		}
		packet.payload.putUnsignedByte(RES_RELULT);
		packet.MSG_TYPE = MSG_TYPE;
		return packet;
	}

	public void unpack(SlpPayload payload) {
		payload.resetIndex();
		this.UAV_LOGIN = payload.getUnsignedByte();
		for(int i=0;i<FMU_KEY.length;i++)
		{
			this.FMU_KEY[i] = payload.getUnsignedInt();
		}
		this.RES_RELULT = payload.getUnsignedByte();
	}
	@Override
	public String toString()
	{
		return "SlpMsgLogin "+"UAV_LOGIN"+":"+UAV_LOGIN+" "+ "RES_RELULT"+":"+RES_RELULT;
		
	}

}
