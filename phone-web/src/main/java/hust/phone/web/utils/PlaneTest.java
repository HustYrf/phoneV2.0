package hust.phone.web.utils;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import hust.phone.service.impl.FlyingPathServiceImpl;
import hust.phone.utils.pojo.PlanePathVo;

public class PlaneTest {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		FlyingPathServiceImpl fliying = (FlyingPathServiceImpl) ac.getBean("flyingPathServiceImpl");
		List<PlanePathVo> pathList = fliying.getPathToObject(8);
		System.out.println(pathList.size());
		
	}
}
