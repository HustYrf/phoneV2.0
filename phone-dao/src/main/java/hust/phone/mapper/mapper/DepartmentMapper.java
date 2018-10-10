package hust.phone.mapper.mapper;

import java.util.List;

import hust.phone.mapper.pojo.Department;

public interface DepartmentMapper {
	
    Department getDepartmentById(int id);

	List<Department> getAllDepartment();

	String getDepartmentNameById(Integer departmentId);
}