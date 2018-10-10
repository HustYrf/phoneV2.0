package hust.phone.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hust.phone.mapper.pojo.Department;
import hust.phone.mapper.pojo.User;
import hust.phone.service.interFace.DepartmentService;
import hust.phone.utils.pojo.PhoneUtils;

@Controller
public class IndexController {
	
	@Autowired
	private DepartmentService departmentServiceImpl;
	
	@RequestMapping("/welcome")
	public String webtest(Model model)
	{
		return "welcome";
	}
	
	@RequestMapping("/userInfo")
	public String userInfo(Model model,HttpServletRequest request) {
		
		List<Department> departments = departmentServiceImpl.getAllDepartment();
		User user= PhoneUtils.getLoginUser(request);
		
		model.addAttribute("user",user);
		model.addAttribute("departments",departments);
		return "personnalInfo";
	}
	
	
	@RequestMapping(value = "/userInfoModify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String userInfoModify(Model model,HttpServletRequest request,User user) {
		
		User oldUser= PhoneUtils.getLoginUser(request);
		
		
		
		
		
		
		return "";
	}

}
