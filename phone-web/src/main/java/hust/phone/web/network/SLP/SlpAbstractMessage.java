package hust.phone.web.network.SLP;

public interface SlpAbstractMessage {
	
	public SlpPacket pack();
	public void unpack(SlpPayload payload);
	
}
