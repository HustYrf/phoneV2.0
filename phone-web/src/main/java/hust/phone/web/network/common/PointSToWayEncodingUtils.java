package hust.phone.web.network.common;

import java.util.ArrayList;
import java.util.Arrays;

import hust.phone.web.network.SLP.message.SlpPoint;

public class PointSToWayEncodingUtils {
	
	public static byte[] PointSToWayEncoding(ArrayList<SlpPoint> points)
	{
		byte coding[] = new byte[points.size()*15];
		int k =0;
		for(int i= 0;i<points.size();i++)
		{
			byte[] pointEncoding = SlpPoint.getPointEncoding(points.get(i));
			k= i*15;
			for(int j= 0;j<15;j++)
			{
				coding[k++]=pointEncoding[j];	
			}
		}
		return coding;
	}
	
	public static ArrayList<SlpPoint> WayToPoints(short[] coding)
	{
		byte [] codes = new byte[coding.length];
		for(int i=0;i<coding.length;i++)
		{
			codes[i] = (byte) coding[i];
		}
		
		ArrayList<SlpPoint> list = new ArrayList<SlpPoint>();
		for(int i=0;i<coding.length;i=i+15)
		{
			byte[] copyOfRange = Arrays.copyOfRange(codes, i, i+14);
			list.add(SlpPoint.getPoint(copyOfRange));
		}
		return list;
	}

}
