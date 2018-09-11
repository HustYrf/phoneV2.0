package hust.phone.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hust.phone.mapper.mapper.FlyingPathMapper;
import hust.phone.mapper.pojo.FlyingPath;
import hust.phone.service.interFace.FlyingPathService;

@Service
public class FlyingPathServiceImpl implements FlyingPathService {

	@Autowired
	private FlyingPathMapper flyingPathMapper;

	// 插入一条飞行路径
	@Override
	public boolean insertPlanePath(FlyingPath flyingPath) {

		flyingPath.setPathdata("LINESTRING" + flyingPath.getPathdata());
		Date date = new Date();
		flyingPath.setCreatetime(date);
		flyingPath.setUpdatetime(date);

		// 然后在下面进行插入数据
		//if(flyingPathMapper.insertPlanePath(flyingPath)==1)
		
		   //return true;
		//else
			return false;
	}

	@Override
	public FlyingPath selectByPlanepathId(FlyingPath flyingPath) {
		
		FlyingPath path = flyingPathMapper.selectByFlyingPathId(flyingPath);
		return path;
	}

	/*@Override
	public List<FlyingPath> findAllplanePath() {
		List<FlyingPath> planePaths = flyingPathMapper.findAllplanePath();
		return planePaths;
	}*/

}
