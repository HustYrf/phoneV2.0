package hust.phone.web.network.SLP.message;

import hust.phone.web.network.SLP.SlpAbstractMessage;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.SlpPayload;

public class SlpMsgStatus implements SlpAbstractMessage {
	
	public static final int MSG_LENGTH = 33;
	public static final short MSG_TYPE = 1;
	public short BASEMODE;
	public long CUSTMODE;
	public short SYS_STATUS;
	public short BATT_REMN;
	public float AR_SPD;
	public float GR_SPD;
	public long GPS_LAT;
	public long GPS_LON;
	public float GPS_ELV;
	public int GPS_HDG;
	public int HORI_AGL;
	public int VERT_AGL;
	
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
		this.SYS_STATUS = payload.getUnsignedByte();
		this.BATT_REMN = payload.getUnsignedByte();
		this.AR_SPD = payload.getFloat();
		this.GR_SPD = payload.getFloat();
		this.GPS_LAT = payload.getUnsignedInt();
		this.GPS_LON = payload.getUnsignedInt();
		this.GPS_ELV = payload.getFloat();
		this.GPS_HDG = payload.getUnsignedShort();
		this.HORI_AGL = payload.getUnsignedShort();
		this.VERT_AGL = payload.getUnsignedShort();
	}
	
	public SlpPacket pack()
	{
		SlpPacket packet = new SlpPacket(MSG_LENGTH);
		packet.MSG_TYPE = MSG_TYPE;
		packet.payload.putUnsignedByte(BASEMODE);
		packet.payload.putUnsignedInt(CUSTMODE);
		packet.payload.putUnsignedByte(SYS_STATUS);
		packet.payload.putUnsignedByte(BATT_REMN);
		packet.payload.putFloat(AR_SPD);
		packet.payload.putFloat(GR_SPD);
		packet.payload.putUnsignedInt(GPS_LAT);
		packet.payload.putUnsignedInt(GPS_LON);
		packet.payload.putFloat(GPS_ELV);
		packet.payload.putUnsignedShort(GPS_HDG);
		packet.payload.putUnsignedShort(HORI_AGL);
		packet.payload.putUnsignedShort(VERT_AGL);
		return packet;
	}
	
	@Override
	public String toString() {
		return "SlpMsgStatus "+"BASEMODE"+":"+BASEMODE+" "+ "CUSTMODE"+":"+CUSTMODE +" "+"SYS_STATUS"+":"+SYS_STATUS+" "+"BATT_REMN"+":"+BATT_REMN+" "
				+"AR_SPD"+":"+AR_SPD+" "+"GR_SPD"+":"+GR_SPD+" "+"GPS_LAT"+":"+GPS_LAT+" "+"GPS_LON"+":"+GPS_LON+" "+"GPS_ELV"+":"+GPS_ELV+" "
				+"GPS_HDG"+":"+GPS_HDG+" "+"HORI_AGL"+":"+HORI_AGL+" "+"VERT_AGL"+":"+VERT_AGL;
	}

}