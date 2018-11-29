package hust.phone.web.network.SLP.message;

import hust.phone.web.network.SLP.SlpAbstractMessage;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.SlpPayload;

public class SlpMsgSearchLinesDetail implements SlpAbstractMessage{
	public static final short MSG_TYPE = 0x82;

    public long ROUTE_ID;
      
    public int ROUTE_COUNT;
      
    public int ROUTE_MSG_COUNT;
 
    public short WAYPOINT[];
	public SlpMsgSearchLinesDetail(SlpPacket slpPacket) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public SlpPacket pack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unpack(SlpPayload payload) {
		// TODO Auto-generated method stub
		
	}

}
