package hust.phone.service.interFace;

import java.util.List;

import hust.phone.mapper.pojo.FlyingPath;

public interface FlyingPathService {

    boolean insertPlanePath(FlyingPath flyingPath);

    FlyingPath selectByPlanepathId(FlyingPath flyingPath);

	/*List<FlyingPath> findAllplanePath();*/
}
