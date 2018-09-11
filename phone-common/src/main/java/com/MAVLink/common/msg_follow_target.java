/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE FOLLOW_TARGET PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* current motion information from a designated system
*/
public class msg_follow_target extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_FOLLOW_TARGET = 144;
    public static final int MAVLINK_MSG_ID_FOLLOW_TARGET_CRC = 127;
    public static final int MAVLINK_MSG_LENGTH = 93;
    private static final long serialVersionUID = MAVLINK_MSG_ID_FOLLOW_TARGET;


      
    /**
    * Timestamp in milliseconds since system boot
    */
    public long timestamp;
      
    /**
    * button states or switches of a tracker device
    */
    public long custom_state;
      
    /**
    * Latitude (WGS84), in degrees * 1E7
    */
    public int lat;
      
    /**
    * Longitude (WGS84), in degrees * 1E7
    */
    public int lon;
      
    /**
    * AMSL, in meters
    */
    public float alt;
      
    /**
    * target velocity (0,0,0) for unknown
    */
    public float vel[] = new float[3];
      
    /**
    * linear target acceleration (0,0,0) for unknown
    */
    public float acc[] = new float[3];
      
    /**
    * (1 0 0 0 for unknown)
    */
    public float attitude_q[] = new float[4];
      
    /**
    * (0 0 0 for unknown)
    */
    public float rates[] = new float[3];
      
    /**
    * eph epv
    */
    public float position_cov[] = new float[3];
      
    /**
    * bit positions for tracker reporting capabilities (POS = 0, VEL = 1, ACCEL = 2, ATT + RATES = 3)
    */
    public short est_capabilities;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_FOLLOW_TARGET;
        packet.crc_extra = MAVLINK_MSG_ID_FOLLOW_TARGET_CRC;
              
        packet.payload.putUnsignedLong(timestamp);
              
        packet.payload.putUnsignedLong(custom_state);
              
        packet.payload.putInt(lat);
              
        packet.payload.putInt(lon);
              
        packet.payload.putFloat(alt);
              
        
        for (int i = 0; i < vel.length; i++) {
            packet.payload.putFloat(vel[i]);
        }
                    
              
        
        for (int i = 0; i < acc.length; i++) {
            packet.payload.putFloat(acc[i]);
        }
                    
              
        
        for (int i = 0; i < attitude_q.length; i++) {
            packet.payload.putFloat(attitude_q[i]);
        }
                    
              
        
        for (int i = 0; i < rates.length; i++) {
            packet.payload.putFloat(rates[i]);
        }
                    
              
        
        for (int i = 0; i < position_cov.length; i++) {
            packet.payload.putFloat(position_cov[i]);
        }
                    
              
        packet.payload.putUnsignedByte(est_capabilities);
        
        return packet;
    }

    /**
    * Decode a follow_target message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.timestamp = payload.getUnsignedLong();
              
        this.custom_state = payload.getUnsignedLong();
              
        this.lat = payload.getInt();
              
        this.lon = payload.getInt();
              
        this.alt = payload.getFloat();
              
         
        for (int i = 0; i < this.vel.length; i++) {
            this.vel[i] = payload.getFloat();
        }
                
              
         
        for (int i = 0; i < this.acc.length; i++) {
            this.acc[i] = payload.getFloat();
        }
                
              
         
        for (int i = 0; i < this.attitude_q.length; i++) {
            this.attitude_q[i] = payload.getFloat();
        }
                
              
         
        for (int i = 0; i < this.rates.length; i++) {
            this.rates[i] = payload.getFloat();
        }
                
              
         
        for (int i = 0; i < this.position_cov.length; i++) {
            this.position_cov[i] = payload.getFloat();
        }
                
              
        this.est_capabilities = payload.getUnsignedByte();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_follow_target(){
        msgid = MAVLINK_MSG_ID_FOLLOW_TARGET;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_follow_target(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_FOLLOW_TARGET;
        unpack(mavLinkPacket.payload);
    }

                          
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_FOLLOW_TARGET - sysid:"+sysid+" compid:"+compid+" timestamp:"+timestamp+" custom_state:"+custom_state+" lat:"+lat+" lon:"+lon+" alt:"+alt+" vel:"+vel+" acc:"+acc+" attitude_q:"+attitude_q+" rates:"+rates+" position_cov:"+position_cov+" est_capabilities:"+est_capabilities+"";
    }
}
        