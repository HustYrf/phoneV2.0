package hust.phone.web.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import hust.phone.service.impl.FlyingPathServiceImpl;
import hust.phone.utils.pojo.PlanePathVo;
import hust.phone.web.network.SLP.SlpPacket;
import hust.phone.web.network.SLP.message.SlpMsgPutLines;
import hust.phone.web.network.SLP.message.SlpMsgSearchLinesDetail;
import hust.phone.web.network.SLP.message.SlpPoint;
import hust.phone.web.network.common.ConstantUtils;
import hust.phone.web.network.common.PointSToWayEncodingUtils;

public class PlaneTest {

	public static void main(String[] args) {
//		FlyingPathServiceImpl fliying =(FlyingPathServiceImpl) SpringBeanFactoryUtils.getBean("flyingPathServiceImpl");
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		FlyingPathServiceImpl fliying = (FlyingPathServiceImpl) ac.getBean("flyingPathServiceImpl");
		List<PlanePathVo> pathList = fliying.getPathToObject(15);
		for(PlanePathVo p:pathList)
		{
			System.out.println(p.getHeight()+" "+p.getLongitude()+" "+p.getLatitude());
			float height = p.getHeight();
			int lon =(int) (p.getLongitude() *10000000);
			int lat =(int) (p.getLatitude() *10000000);
			System.err.println(height+" "+lon+" "+lat);
		}
		
		
		ArrayList<SlpPoint> list = new ArrayList<SlpPoint>();
		int size = pathList.size();
		System.out.println("数据库中航点的个数:"+size);
		SlpMsgPutLines msg  = new SlpMsgPutLines();
		if(size>=ConstantUtils.PATH_CAP_MAX)
		{
			for(int i=1;i<=ConstantUtils.PATH_CAP_MAX;i++)
			{
				SlpPoint point = new SlpPoint();
				point.WAYPOINT_NUM=i;
				point.WP_LNG = (int)((pathList.get(i-1).getLongitude())*10000000);
				point.WP_LAT = (int)((pathList.get(i-1).getLatitude())*10000000);
				point.WP_ALT =(float) pathList.get(i-1).getHeight();
				point.WAYPOINT_TYPE = (short) pathList.get(i-1).getType();
				point.WP_PARAM1 = pathList.get(i-1).getParamone();
				point.WP_PARAM2 = pathList.get(i-1).getParamtwo();
				System.out.println(point.toString());
				list.add(point);
			}
			byte[] pointSToWayEncoding = PointSToWayEncodingUtils.PointSToWayEncoding(list);
			System.out.println("路径整合"+Arrays.toString(pointSToWayEncoding));
			msg.ROUTE_ID = 21;
			msg.ROUTE_COUNT = size;
			msg.ROUTE_MSG_COUNT =ConstantUtils.PATH_CAP_MAX;
			msg.POINTS= new short[msg.ROUTE_MSG_COUNT * ConstantUtils.POINT_LENGTH];
			for(int h= 0;h<pointSToWayEncoding.length;h++)
			{
				msg.POINTS[h] = (short) (pointSToWayEncoding[h]&0xff);
			}
		}else
		{
			for(int i=1;i<=size;i++)
			{
				SlpPoint point = new SlpPoint();
				point.WAYPOINT_NUM=i;
				point.WP_LNG = (int)((pathList.get(i-1).getLongitude())*10000000);
				point.WP_LAT = (int)((pathList.get(i-1).getLatitude())*10000000);
				point.WP_ALT =(float) pathList.get(i-1).getHeight();
				point.WAYPOINT_TYPE = (short) pathList.get(i-1).getType();
				point.WP_PARAM1 = pathList.get(i-1).getParamone();
				point.WP_PARAM2 = pathList.get(i-1).getParamtwo();
				System.out.println(point.toString());
				list.add(point);
			}
			byte[] pointSToWayEncoding = PointSToWayEncodingUtils.PointSToWayEncoding(list);
			System.out.println("路径整合"+Arrays.toString(pointSToWayEncoding));
			msg.ROUTE_ID = 20;
			msg.ROUTE_COUNT = size;
			msg.ROUTE_MSG_COUNT =size;
			msg.POINTS= new short[msg.ROUTE_MSG_COUNT * ConstantUtils.POINT_LENGTH];
			for(int h= 0;h<pointSToWayEncoding.length;h++)
			{
				msg.POINTS[h] = (short) (pointSToWayEncoding[h]&0xff);
			}
		}
		
		SlpPacket pack = msg.pack();
		pack.SND_DEVICE_ID = ConstantUtils.Server_Num;
		pack.REV_DEVICE_ID = 2;
		byte[] encoding = pack.encoding();
		System.out.println("打包路径整合"+Arrays.toString(encoding));
		SlpPacket parse = SlpPacket.parse(encoding);
		SlpMsgPutLines unpack = (SlpMsgPutLines) parse.unpack();
		ArrayList<SlpPoint> list2 = PointSToWayEncodingUtils.WayToPoints((unpack.POINTS));
		for(int i=0;i<list2.size();i++)
		{
			System.out.println(list2.get(i).toString());
		}
	}
}
