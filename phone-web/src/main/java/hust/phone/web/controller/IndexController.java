package hust.phone.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hust.phone.constant.WebConst;
import hust.phone.mapper.pojo.Department;
import hust.phone.mapper.pojo.User;
import hust.phone.service.impl.UserServiceimpl;
import hust.phone.service.interFace.DepartmentService;
import hust.phone.service.interFace.UserService;
import hust.phone.utils.pojo.JsonView;
import hust.phone.utils.pojo.PhoneUtils;

@Controller
public class IndexController {
	
	@Autowired
	private DepartmentService departmentServiceImpl;
	
	@Autowired
	private UserService userServiceimpl;
	
	@RequestMapping("/welcome")
	public String webtest(Model model)
	{
		return "welcome";
	}
	
	@RequestMapping("/userInfo")
	public String userInfo(Model model,HttpServletRequest request) {
		
		List<Department> departments = departmentServiceImpl.getAllDepartment();
		User user= PhoneUtils.getLoginUser(request);
		user.setDepartmentName(departmentServiceImpl.getNameById(user.getDepartmentId()));
		model.addAttribute("user",user);
		model.addAttribute("departments",departments);
		return "personnalInfo";
	}
	
	
	@RequestMapping(value = "/userInfoModify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String userInfoModify(Model model,HttpServletRequest request,User user) {
		
		User oldUser= PhoneUtils.getLoginUser(request);
		
		user.setId(oldUser.getId());
		if(user.getPassword()==null || user.getPassword()=="") {
			user.setPassword(null);
		}else {
			user.setPassword(PhoneUtils.MD5encode(oldUser.getName()+user.getPassword()));   //对密码进行加密修改
		}
		
		if(userServiceimpl.updateByUser(user) == true) {
			
			User user2 = userServiceimpl.getUserById(user.getId());
			
			//此处暂不支持修改用户头像
			//user3.setIcon(BASE_IMAGE_URL + USER_DIR + user3.getIcon());
			request.getSession().removeAttribute(WebConst.LOGIN_SESSION_KEY);
			request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user2);
			
			return JsonView.render(1, "个人信息修改成功！");
		}else {
			return JsonView.render(0, "个人信息修改失败！");
		}
		
			
	}

}
