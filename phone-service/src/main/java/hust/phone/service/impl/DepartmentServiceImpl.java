package hust.phone.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hust.phone.mapper.mapper.DepartmentMapper;
import hust.phone.mapper.pojo.Department;
import hust.phone.service.interFace.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Override
	public List<Department> getAllDepartment() {
		
		List<Department> departments = departmentMapper.getAllDepartment();
		if(departments.size()>0) {
			return departments;
		}else {
			return null;
		}
		
	}

	@Override
	public String getNameById(Integer departmentId) {
		
		return departmentMapper.getDepartmentNameById(departmentId);
	}

}
