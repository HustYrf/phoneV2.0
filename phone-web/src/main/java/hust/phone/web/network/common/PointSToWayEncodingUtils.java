package hust.phone.web.network.common;

import java.util.ArrayList;
import java.util.Arrays;

import hust.phone.web.network.SLP.message.SlpPoint;

public class PointSToWayEncodingUtils {
	
	public static byte[] PointSToWayEncoding(ArrayList<SlpPoint> points)
	{
		byte coding[] = new byte[points.size()*ConstantUtils.POINT_LENGTH];
		int k =0;
		for(int i= 0;i<points.size();i++)
		{
			byte[] pointEncoding = SlpPoint.getPointEncoding(points.get(i));
			k= i*ConstantUtils.POINT_LENGTH;
			for(int j= 0;j<ConstantUtils.POINT_LENGTH;j++)
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
		for(int i=0;i<coding.length;i=i+ConstantUtils.POINT_LENGTH)
		{
			byte[] copyOfRange = Arrays.copyOfRange(codes, i, i+ConstantUtils.POINT_LENGTH-1);
			list.add(SlpPoint.getPoint(copyOfRange));
		}
		return list;
	}

}
