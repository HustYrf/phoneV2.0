package hust.phone.service.interFace;

import hust.phone.mapper.pojo.Uav;

public interface UavService {

	Uav getPlaneByPlane(Uav uav);

	void takeoff(int id);
	
	void showData(int id);
	
	void planeLand(int id);
	
	void planeReturn(int id);

	void updatePositionByUav(Uav uav);

	Uav getUavByDeviceId(String deviceId);

}
