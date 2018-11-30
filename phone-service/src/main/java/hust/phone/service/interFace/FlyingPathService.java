package hust.phone.service.interFace;

import java.util.List;

import hust.phone.mapper.pojo.FlyingPath;
import hust.phone.utils.pojo.PlanePathVo;

public interface FlyingPathService {

    boolean insertPlanePath(FlyingPath flyingPath);

    FlyingPath selectByPlanepathId(FlyingPath flyingPath);
    List<PlanePathVo> getPathToObject(int pathId);

	/*List<FlyingPath> findAllplanePath();*/
}
