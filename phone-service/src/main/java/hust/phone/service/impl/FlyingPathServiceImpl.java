package hust.phone.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hust.phone.mapper.mapper.FlyingPathMapper;
import hust.phone.mapper.pojo.FlyingPath;
import hust.phone.service.interFace.FlyingPathService;
import hust.phone.utils.LineUtil;
import hust.phone.utils.pojo.PlanePathVo;

@Service
public class FlyingPathServiceImpl implements FlyingPathService {

	@Autowired
	private FlyingPathMapper flyingPathMapper;
	
	//返回所有飞行路径的点的集合
	
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

	@Override
	public List<PlanePathVo> getPathToObject(int pathId) {
		FlyingPath flyingPath  = new FlyingPath();
		flyingPath.setId(pathId);
		FlyingPath planePathList = flyingPathMapper.selectByFlyingPathId(flyingPath);
		List<PlanePathVo> plist = LineUtil.textToList(planePathList.getPathdata(), planePathList.getHeightdata(),planePathList.getPointtype(),
				planePathList.getParamone(),planePathList.getParamtwo());
		return plist;
	}
	
	

	/*@Override
	public List<FlyingPath> findAllplanePath() {
		List<FlyingPath> planePaths = flyingPathMapper.findAllplanePath();
		return planePaths;
	}*/

}
