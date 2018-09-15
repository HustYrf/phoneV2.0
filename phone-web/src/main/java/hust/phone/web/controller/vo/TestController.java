package hust.phone.web.controller.vo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	
	@RequestMapping("/chat2")
	public String testchatsocket2(Model model)
	{
		String planeid = 1+"";
		model.addAttribute("planeid", planeid);
		return "chat2";
	}
}
