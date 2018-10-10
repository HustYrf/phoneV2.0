package hust.phone.service.interFace;

import java.util.List;

import hust.phone.mapper.pojo.Department;

public interface DepartmentService {

	List<Department> getAllDepartment();

	String getNameById(Integer departmentId);

}
