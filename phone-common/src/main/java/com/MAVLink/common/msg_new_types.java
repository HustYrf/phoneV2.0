package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/**
 * Test all field types
 */
public class msg_new_types extends MAVLinkMessage {

	public static final int MAVLINK_MSG_ID_NEW_TYPES = 161;
	//这个是动态变化的，只能去掉校验码
	public static final int MAVLINK_MSG_ID_NEW_TYPES_CRC = 107;
	// public static final int MAVLINK_MSG_LENGTH = 292;
	private static final long serialVersionUID = MAVLINK_MSG_ID_NEW_TYPES;

	public short HEAD[] = new short[6];

	public long MSG_LEN;

	public int MSG_SEQ;

	public long REV_DEVICE_ID;

	public long SND_DEVICE_ID;
	public short MSG_TYPE;

	public long MSG_TIME;

	public long VAL_TIME;

	public short content[];
	
	public int CHECKSUM;

	/**
	 * Generates the payload for a mavlink message for a message of this type
	 * 
	 * @return
	 */
	public MAVLinkPacket pack() {
		MAVLinkPacket packet = new MAVLinkPacket((int) (MSG_LEN + 8));
		// content = new short[(int) (MSG_LEN-31)];
		packet.sysid = 255;
		packet.compid = 190;
		packet.msgid = MAVLINK_MSG_ID_NEW_TYPES;
		packet.crc_extra = MAVLINK_MSG_ID_NEW_TYPES_CRC;

		for (int i = 0; i < HEAD.length; i++) {
			packet.payload.putUnsignedByte(HEAD[i]);
		}
		packet.payload.putUnsignedInt(MSG_LEN);
		packet.payload.putUnsignedShort(MSG_SEQ);
		packet.payload.putUnsignedInt(REV_DEVICE_ID);
		packet.payload.putUnsignedInt(SND_DEVICE_ID);
		packet.payload.putUnsignedByte(MSG_TYPE);
		
		for (int i = 0; i < content.length; i++) {
			packet.payload.putUnsignedByte(content[i]);
		}

		packet.payload.putUnsignedLong(MSG_TIME);

		packet.payload.putUnsignedLong(VAL_TIME);

		packet.payload.putUnsignedShort(CHECKSUM);

		return packet;
	}

	/**
	 * Decode a new_types message into this class fields
	 *
	 * @param payload
	 *            The message to decode
	 */
	public void unpack(MAVLinkPayload payload) {
		payload.resetIndex();

		for (int i = 0; i < this.HEAD.length; i++) {
			this.HEAD[i] = payload.getUnsignedByte();
		}

		this.MSG_LEN = payload.getUnsignedInt();
		this.MSG_SEQ = payload.getUnsignedShort();

		this.REV_DEVICE_ID = payload.getUnsignedInt();

		this.SND_DEVICE_ID = payload.getUnsignedInt();
		this.MSG_TYPE = payload.getUnsignedByte();
		this.content = new short[(int) (this.MSG_LEN - 31)];
		for (int i = 0; i < this.content.length; i++) {
			this.content[i] = payload.getUnsignedByte();
		}
		this.MSG_TIME = payload.getUnsignedLong();

		this.VAL_TIME = payload.getUnsignedLong();

		this.CHECKSUM = payload.getUnsignedShort();

	}

	/**
	 * Constructor for a new message, just initializes the msgid
	 */
	public msg_new_types() {
		msgid = MAVLINK_MSG_ID_NEW_TYPES;
	}

	/**
	 * Constructor for a new message, initializes the message with the payload from
	 * a mavlink packet
	 *
	 */
	public msg_new_types(MAVLinkPacket mavLinkPacket) {
		this.sysid = mavLinkPacket.sysid;
		this.compid = mavLinkPacket.compid;
		this.msgid = MAVLINK_MSG_ID_NEW_TYPES;
		unpack(mavLinkPacket.payload);
	}

	/**
	 * Returns a string with the MSG name and data
	 */
	public String toString() {
		return "MAVLINK_MSG_ID_NEW_TYPES - sysid:" + sysid + " compid:" + compid + " MSG_TIME:" + MSG_TIME
				+ " VAL_TIME:" + VAL_TIME + " MSG_LEN:" + MSG_LEN + " REV_DEVICE_ID:" + REV_DEVICE_ID
				+ " SND_DEVICE_ID:" + SND_DEVICE_ID + " CHECKSUM:" + CHECKSUM + " HEAD:" + HEAD + " MSG_TYPE:"
				+ MSG_TYPE + " content:" + content + "";
	}
}
