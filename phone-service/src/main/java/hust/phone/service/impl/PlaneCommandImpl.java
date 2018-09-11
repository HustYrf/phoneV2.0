package hust.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hust.phone.mapper.mapper.UavMapper;
import hust.phone.mapper.pojo.Uav;

@Service
public class PlaneCommandImpl {

	@Autowired
	private UavMapper uavMapper;

	public boolean updateById(Uav uav) {
		
		if(uavMapper.updateByPlane(uav)== 1)
			return true;
		else
			return false;

	}

}
