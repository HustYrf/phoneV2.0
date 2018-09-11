package hust.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hust.phone.mapper.mapper.UavMapper;
import hust.phone.mapper.pojo.Uav;
import hust.phone.service.interFace.testService;
import hust.phone.utils.HttpClientUtil;
@Service
public class testServiceImpl implements testService{
	
	@Value("${PLANE_URL}")
	private String PLANE_URL;
	@Value("${PROJECT_NAME}")
	private String PROJECT_NAME;
	@Value("${PLANE_TEST}")
	private String PLANE_TEST;
	@Override
	public String testCon() {

//		String url=PLANE_URL+PLANE_PORT+"/test";
		String url=PLANE_URL+PROJECT_NAME+PLANE_TEST;
		String json=HttpClientUtil.doGet(url);
		return json;
	}
	
	@Autowired
	private UavMapper uavMapper;
	
	public void updateTest()
	{
		 Uav uav = new Uav();
		 int lon1 =2222222;
		 int lat1 =3333333;
		 int alt1=2222;
		 int sysid=2;
		 double lon =lon1/(10000000.0);
		 double lat =lat1/(10000000.0);
		 double alt =alt1/1000.0;
		 String fl ="Point("+lon+" "+lat+")";
		 //这部分需要需改
		/* uav.setPlaneId(sysid+"");
		 uav.setHeight(alt+"");
		 uav.setFlongda(fl);
		 uav.updateByPlane(p);*/
	}
	

}
