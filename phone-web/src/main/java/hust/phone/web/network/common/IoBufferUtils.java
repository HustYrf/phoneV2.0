package hust.phone.web.network.common;

import org.apache.mina.core.buffer.IoBuffer;

public class IoBufferUtils {
	
	//将IoBuffer 转换成byte
	public static byte[] ioVufferToByte(Object message)
	{
		if(!(message instanceof IoBuffer))
		{
			return null;
		}
		IoBuffer ioBuffer = (IoBuffer) message;
		byte [] b = new byte[ioBuffer.limit()];
		ioBuffer.get(b);
		return b;
	}
	
	//将byte转化成IoBUffer
	public static IoBuffer byteToIoBUffer(byte[] bt,int length)
	{
		IoBuffer ioBuffer = IoBuffer.allocate(length);
		ioBuffer.put(bt,0,length);
		ioBuffer.flip();
		return ioBuffer;
	}

}
