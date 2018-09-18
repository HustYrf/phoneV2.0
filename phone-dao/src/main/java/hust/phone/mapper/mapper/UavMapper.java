package hust.phone.mapper.mapper;

import java.util.Date;
import java.util.List;

import hust.phone.mapper.pojo.Uav;

public interface UavMapper {
	
	List<Uav> selectALLPlane();
	List<Uav> selectPlaneByOption(int id,Date starttime,Date endtime);
	List<Uav> selectByUavStatus(int status);
	Uav getPlaneByPlane(Uav uav);
	int updateByPlane(Uav uav);
	void updatePositionByPlane(Uav uav);
}
