package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* Test all field types
*/
public class msg_new_login_res extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_NEW_LOGIN_RES = 162;
    public static final int MAVLINK_MSG_ID_NEW_LOGIN_RES_CRC = 173;
    public static final int MAVLINK_MSG_LENGTH = 41;
    private static final long serialVersionUID = MAVLINK_MSG_ID_NEW_LOGIN_RES;
    
    /**
     * HEAD
     */
    public short HEAD[] = new short[6];
       
     /**
      * MEG_LEN
      */
    public long MSG_LEN; 
    /**
     * MSG_SEQ
     */
     public int MSG_SEQ;
    /**
     * REV_DEVICE_ID
     */
    public long REV_DEVICE_ID;
    /**
     * SND_DEVICE_ID
     */
    public long SND_DEVICE_ID;
    
    /**
    * MSG_TYPE
    */
    public short MSG_TYPE;
    /**
     * UAV_LOGIN
     */
     public short UAV_LOGIN;
       
     /**
     * RES_RELULT
     */
     public short RES_RELULT;
    
    /**
    * MSG_TIME
    */
    public long MSG_TIME;
      
    /**
    * VAL_TIME
    */
    public long VAL_TIME;
      
    /**
    * CHECKSUM
    */
    public int CHECKSUM;
      

      
  
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_NEW_LOGIN_RES;
        packet.crc_extra = MAVLINK_MSG_ID_NEW_LOGIN_RES_CRC;
        for (int i = 0; i < HEAD.length; i++) {
            packet.payload.putUnsignedByte(HEAD[i]);
        }
              
        packet.payload.putUnsignedInt(MSG_LEN);
        packet.payload.putUnsignedShort(MSG_SEQ);
        packet.payload.putUnsignedInt(REV_DEVICE_ID);
        packet.payload.putUnsignedInt(SND_DEVICE_ID);
        packet.payload.putUnsignedByte(MSG_TYPE);
        packet.payload.putUnsignedByte(UAV_LOGIN);
        
        packet.payload.putUnsignedByte(RES_RELULT);
        
        packet.payload.putUnsignedLong(MSG_TIME);
              
        packet.payload.putUnsignedLong(VAL_TIME);
              
        packet.payload.putUnsignedShort(CHECKSUM);
              
        return packet;
    }

    /**
    * Decode a new_login_res message into this class fields
    *
    * @param payload The message to decode
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
        this.UAV_LOGIN = payload.getUnsignedByte();
        this.RES_RELULT = payload.getUnsignedByte();
        this.MSG_TIME = payload.getUnsignedLong();
        this.VAL_TIME = payload.getUnsignedLong();
        this.CHECKSUM = payload.getUnsignedShort();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_new_login_res(){
        msgid = MAVLINK_MSG_ID_NEW_LOGIN_RES;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_new_login_res(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_NEW_LOGIN_RES;
        unpack(mavLinkPacket.payload);
    }

                        
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_NEW_LOGIN_RES - sysid:"+sysid+" compid:"+compid+" MSG_TIME:"+MSG_TIME+" VAL_TIME:"+VAL_TIME+" MSG_LEN:"+MSG_LEN+" REV_DEVICE_ID:"+REV_DEVICE_ID+" SND_DEVICE_ID:"+SND_DEVICE_ID+" CHECKSUM:"+CHECKSUM+" HEAD:"+HEAD+" MSG_TYPE:"+MSG_TYPE+" UAV_LOGIN:"+UAV_LOGIN+" RES_RELULT:"+RES_RELULT+"";
    }
}
        