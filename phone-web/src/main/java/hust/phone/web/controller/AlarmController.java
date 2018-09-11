package hust.phone.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hust.phone.mapper.pojo.Alarm;
import hust.phone.mapper.pojo.User;
import hust.phone.service.interFace.AlarmService;
import hust.phone.utils.pojo.JsonView;
import hust.phone.utils.pojo.PhoneUtils;

@Controller
public class AlarmController {
	
	@Autowired
	private AlarmService alarmServiceImpl;

	@RequestMapping("/alermList")
	public String toTaskList(HttpServletRequest request,Model model) {
		
		/*User user = PhoneUtils.getLoginUser(request);
		int userid = user.getId();*/
		
		List<Alarm> alarmlist = alarmServiceImpl.getAllAlarm();
			
		model.addAttribute("alarmlist",alarmlist);
		
		return "alarmList";
	}
	
	//处理告警信息
	@RequestMapping(value = "/ensureAlarm", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String ensureAlarm(Alarm alarm) {
		
		alarmServiceImpl.updateAlarmStatus(alarm.getId());
		return JsonView.render(1, "处理告警地方成功！");
	}
	
}
