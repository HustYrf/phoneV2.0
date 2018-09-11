package hust.phone.mapper.mapper;


import java.util.List;

import hust.phone.mapper.pojo.FlyingPath;

public interface FlyingPathMapper {
	
	FlyingPath selectByFlyingPathVO(FlyingPath flyingPath);

	 //void insertFlyingPath();

	int insertFlyingPath(FlyingPath flyingPath);

	FlyingPath selectByFlyingPathId(FlyingPath flyingPath);

	int flyingPathCount(FlyingPath flyingPath);

	List<FlyingPath> findAllFlyingPath();

	int deleteFlyingPath(FlyingPath flyingPath); 
}
