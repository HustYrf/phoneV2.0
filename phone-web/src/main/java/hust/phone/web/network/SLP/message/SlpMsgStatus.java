package hust.phone.web.network.SLP.message;

import hust.phone.web.network.SLP.SlpAbstractMessage;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.SlpPayload;

public class SlpMsgStatus implements SlpAbstractMessage {
	
	public static final int MSG_LENGTH = 37;
	public static final short MSG_TYPE = 1;
	public short BASEMODE;
	public long CUSTMODE;
	public long MISSION_ID;
	public short SYS_STATUS;
	public short BATT_REMN = 255;
	public float AR_SPD = -1.0f;
	public float GR_SPD = -1.0f;
	public int GPS_LAT;
	public int GPS_LON;
	public float GPS_ELV;
	public int GPS_HDG = 0xffff;
	public short HORI_AGL;
	public short VERT_AGL = -12;
	
	public SlpMsgStatus() {
		// TODO Auto-generated constructor stub
	}
	public SlpMsgStatus(SlpPacket slpPacket)
	{
		unpack(slpPacket.payload);
	}
	public void unpack(SlpPayload payload) {
		payload.resetIndex();
		this.BASEMODE = payload.getUnsignedByte();
		this.CUSTMODE = payload.getUnsignedInt();
		this.MISSION_ID = payload.getUnsignedInt();
		this.SYS_STATUS = payload.getUnsignedByte();
		this.BATT_REMN = payload.getUnsignedByte();
		this.AR_SPD = payload.getFloat();
		this.GR_SPD = payload.getFloat();
		this.GPS_LAT = payload.getInt();
		this.GPS_LON = payload.getInt();
		this.GPS_ELV = payload.getFloat();
		this.GPS_HDG = payload.getUnsignedShort();
		this.HORI_AGL = payload.getShort();
		this.VERT_AGL = payload.getShort();
	}
	
	public SlpPacket pack()
	{
		SlpPacket packet = new SlpPacket(MSG_LENGTH);
		packet.MSG_TYPE = MSG_TYPE;
		packet.payload.putUnsignedByte(BASEMODE);
		packet.payload.putUnsignedInt(CUSTMODE);
		packet.payload.putUnsignedInt(MISSION_ID);
		packet.payload.putUnsignedByte(SYS_STATUS);
		packet.payload.putUnsignedByte(BATT_REMN);
		packet.payload.putFloat(AR_SPD);
		packet.payload.putFloat(GR_SPD);
		packet.payload.putInt(GPS_LAT);
		packet.payload.putInt(GPS_LON);
		packet.payload.putFloat(GPS_ELV);
		packet.payload.putUnsignedShort(GPS_HDG);
		packet.payload.putShort(HORI_AGL);
		packet.payload.putShort(VERT_AGL);
		return packet;
	}
	
	@Override
	public String toString() {
		return "SlpMsgStatus "+"BASEMODE"+":"+BASEMODE+" "+ "CUSTMODE"+":"+CUSTMODE +" "+"SYS_STATUS"+":"+SYS_STATUS+" "+"BATT_REMN"+":"+BATT_REMN+" "
				+"AR_SPD"+":"+AR_SPD+" "+"GR_SPD"+":"+GR_SPD+" "+"GPS_LAT"+":"+GPS_LAT+" "+"GPS_LON"+":"+GPS_LON+" "+"GPS_ELV"+":"+GPS_ELV+" "
				+"GPS_HDG"+":"+GPS_HDG+" "+"HORI_AGL"+":"+HORI_AGL+" "+"VERT_AGL"+":"+VERT_AGL;
	}

}
