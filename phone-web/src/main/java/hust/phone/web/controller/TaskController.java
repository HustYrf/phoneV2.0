package hust.phone.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hust.phone.mapper.pojo.FlyingPath;
import hust.phone.mapper.pojo.Task;
import hust.phone.mapper.pojo.Uav;
import hust.phone.mapper.pojo.User;
import hust.phone.service.interFace.FlyingPathService;
import hust.phone.service.interFace.UserService;
import hust.phone.service.interFace.UavService;
import hust.phone.service.interFace.TaskService;
import hust.phone.utils.JsonUtils;
import hust.phone.utils.pojo.JsonView;
import hust.phone.utils.pojo.PhoneUtils;
import hust.phone.web.controller.vo.FlyingPathVO;
import hust.phone.web.controller.vo.TaskVO;

@Controller
public class TaskController {

	@Autowired
	private TaskService taskServiceImpl;

	@Autowired
	private UserService userService;

	@Autowired
	private FlyingPathService flyingPathService;

	@Autowired
	private UavService uavServiceImpl;

	private int Number = 0; // 未完成工单数目

	// 模拟正在执行的飞行任务
	// private FlyingPathVO exeFlyingPathVO;
	// private int exeindex=0;

	// 工作单跳转
	@RequestMapping("/toTask")
	public String toTaskList() {

		return "task";
	}

	// 返回飞机地点 模拟用
	@RequestMapping(value = "/getlocation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getlocation(Uav uav) {

		// exeindex = exeindex + 2; //下一个点
		// if (exeindex > exeFlyingPathVO.getPathdata().size()) {
		// return ""; //如果超出了范围则返回空
		// }
		// 在这里应该获取飞机位置
		// Uav uav2 = planeServiceImpl.getPlaneByPlane(uav);
		// plane2.set这里暂时不写逻辑，，
		// List<Double> location = exeFlyingPathVO.getPathdata().get(exeindex);

		// return JsonUtils.objectToJson(location);
		return "";
	}

	@RequestMapping("/myindex")
	public String index(HttpServletRequest request, Model model) {
		Number = userService.getTaskNumByUser(PhoneUtils.getLoginUser(request)); // 进入主页面的时候初始化
		model.addAttribute("tasknum", Number);
		return "home";
	}

	// 确认任务
	// 如果用户角色是放飞者，那么修改该任务的状态为 2
	// 如果用户是接收者，那么修改该任务的状态为 3，同时在exe表中插入一条新的数据
	@RequestMapping(value = "/ensureTask", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String ensureTask(Task task, HttpServletRequest request) {

		User user = PhoneUtils.getLoginUser(request);
		int userid = user.getId();
		Task task2 = taskServiceImpl.getTaskByTask(task);

		String reString = "";
		if (task2.getUserA() == userid) { // 如果是用户在该任务是放飞者

			if (taskServiceImpl.setStatusTaskByTask(task, 3) == true) {
				reString = "放飞员确认任务成功!";
			} else {
				reString = "放飞员确认任务失败!";
			}

		}
		if (task2.getUserZ() == userid) { // 如果是用户在该任务是接收者

			if (taskServiceImpl.setStatusTaskByTask(task, 4) == true) {
				reString = "接收员确认任务成功!";
			} else {
				reString = "接收员确认任务失败！";
			}

		}
		return JsonView.render(1, reString);
	}

	// 申请起飞
	@RequestMapping(value = "/applyTaskoff", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String applyTaskoff(Task task) {

		int oldStatus = taskServiceImpl.getTaskStatus(task);
		if (oldStatus == 6) {
			if (taskServiceImpl.setStatusTaskByTask(task, 7) == true) {
				return JsonView.render(1, "申请成功，等待管理员确认。");
			} else {
				return JsonView.render(1, "申请失败，请重新申请。");
			}
		}
		return JsonView.render(1, "当前状态无法申请起飞！");

	}

	// 撤销起飞
	@RequestMapping(value = "/cancelTaskoff", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String cancelTaskoff(Task task) {

		// 撤销起飞

		int oldStatus = taskServiceImpl.getTaskStatus(task);
		if (oldStatus == 6) {
			if (taskServiceImpl.setStatusTaskByTask(task, 4) == true) {
				return JsonView.render(1, "撤销成功。");
			} else {
				return JsonView.render(1, "撤销失败，请重试。");
			}
		}
		return JsonView.render(1, "当前状态无法撤销起飞！");
	}

	// 未执行前退回任务，如果任一角色退回任务的话那该任务直接完成，并且完成结果为失败
	@RequestMapping(value = "/rollbackTask", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String rollbackTask(Task task, HttpServletRequest request) {

		User user = PhoneUtils.getLoginUser(request);
		int userid = user.getId();
		Task task2 = taskServiceImpl.getTaskByTask(task);

		String reString = "未知错误，请重试";
		int oldStatus = task2.getStatus();

		if (oldStatus == 2 || oldStatus == 3 || oldStatus == 4) {

			if (taskServiceImpl.setStatusTaskByTask(task, 1) == true) {
				if (task2.getUserA() == userid) { // 如果是用户在该任务是放飞者
					reString = "放飞员退回任务成功";
				}
				if (task2.getUserZ() == userid) { // 如果是用户在该任务是接收者
					reString = "接机员退回任务成功";
				}
			} else {
				reString = "退回任务失败！";
			}
		}

		if (oldStatus == 8) {
			if (taskServiceImpl.setStatusTaskByTask(task, 4) == true) {
				reString = "放飞员退回任务成功,返回到执行状态！";
			} else {
				reString = "放飞员退回任务失败！";
			}
		}

		return JsonView.render(1, reString);
	}

	// 根据完成状态查询所有的工作单
	@RequestMapping("/taskList")
	public String taskList(int flag, Model model, HttpServletRequest request) {

		Task task = new Task();
		User user = PhoneUtils.getLoginUser(request);
		int userid = user.getId();

		List<Task> taskList = new ArrayList<Task>();
		// 判断是哪类查询条件
		switch (flag) {
		case 0:
			task.setFinishstatus(0);
			break;
		case 1:
			task.setFinishstatus(1);
			break;
		default:
			task.setFinishstatus(null);
			break;
		}
		// 设置bid，cid都为对应的
		task.setUserA(userid);
		task.setUserZ(userid);

		taskList = taskServiceImpl.selectAllByTask(task);
		List<TaskVO> taskVOList = new ArrayList<TaskVO>();

		for (int i = 0; i < taskList.size(); i++) {
			// 设置 task角色
			Task taskitem = taskList.get(i);
			TaskVO taskVO = new TaskVO();
			taskVO.setTask(taskitem);
			taskVO.setUserCreatorName(userService.getNameByUserId(taskitem.getUsercreator()));
			taskVO.setUserAName(userService.getNameByUserId(taskitem.getUserA()));
			taskVO.setUserZName(userService.getNameByUserId(taskitem.getUserZ()));
			// 如果 role = 1表示放飞员， role=2表示接机员
			int role = (userid == taskitem.getUserA()) ? 1 : 2;
			taskVO.setRole(role);
			taskVOList.add(taskVO);
		}

		model.addAttribute("taskList", taskVOList);

		return "subtasklist";
	}

	// 轮询新的工单数目
	@RequestMapping(value = "getTaskNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getTaskNumber(HttpServletRequest request) {
		// 在这里查询最新的工单数目
		User user = PhoneUtils.getLoginUser(request);
		int newNum = 0;
		while (true) {

			newNum = userService.getTaskNumByUser(user);
			// 数据发生改变 将数据响应客户端
			if (newNum != Number) {
				break;
			} else {
				// 没有新的数据 保持住连接
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Number = newNum;
		return newNum + "";
	}

	// 跳转到无人机操纵界面
	@RequestMapping("/toPlane")
	public String toPlane(Task task, HttpServletRequest request, Model model) {

		User user = PhoneUtils.getLoginUser(request);
		Task task2 = taskServiceImpl.getTaskByTask(task);

		// 如果 role = 1表示放飞员， role=2表示接机员
		int role = user.getId() == task2.getUserA() ? 1 : 2;
		task2.setRole(role);

		// 测试****把任务的状态设为自检中
		// taskServiceImpl.setStatusTaskByTask(task, "4");
		// taskServiceImpl.setStatusTaskByTask(task, "5");

		FlyingPath flyingPath = new FlyingPath();
		flyingPath.setId(task2.getFlyingpathId());
		FlyingPath flyingPath2 = flyingPathService.selectByPlanepathId(flyingPath);

		FlyingPathVO flyingPathVO = new FlyingPathVO(flyingPath2);

		// exeFlyingPathVO = flyingPathVO;
		// exeindex = 0;
		Uav uav = new Uav();
		uav.setId(task2.getUavId());
        Uav uav1 = uavServiceImpl.getPlaneByPlane(uav);
		
        model.addAttribute("uav",uav1);
		model.addAttribute("planepath", JsonUtils.objectToJson(flyingPathVO));
		model.addAttribute("task", task2);
		if (role == 1) {
			return "fightA";
		} else {
			return "fightZ";
		}

	}

	@RequestMapping(value = "/exeTask", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String exeTask(Task task) {

		taskServiceImpl.setStatusTaskByTask(task, 4);

		// 测试****把任务的状态设为自检中
		taskServiceImpl.setStatusTaskByTask(task, 5);

		return JsonView.render(1, "任务开始执行！");

	}

	@RequestMapping("/toPlaneControl")
	public String toPlaneControl(HttpServletRequest request, Model model) {

		Task task = new Task();
		User user = PhoneUtils.getLoginUser(request);
		int userid = user.getId();

		task.setFinishstatus(0);
		task.setUserA(userid);
		task.setUserZ(userid);

		Task task2 = taskServiceImpl.selectOneExeTask(task); // 查找一个正在执行的任务
		if (task2 == null) {
			model.addAttribute("tip", "您暂无正在执行的任务！");
			return "fightA";
		}
		int role = user.getId() == task2.getUserA() ? 1 : 2;
		task2.setRole(role);

		FlyingPath flyingPath = new FlyingPath();
		flyingPath.setId(task2.getId());
		FlyingPath flyingPath2 = flyingPathService.selectByPlanepathId(flyingPath);
		FlyingPathVO flyingPathVo = new FlyingPathVO(flyingPath2);

		// exeFlyingPathVO = flyingPathVo;
		// exeindex = 0;

		model.addAttribute("planepath", JsonUtils.objectToJson(flyingPathVo));
		model.addAttribute("task", task2);

		return "fightA";

	}

	// 紧急返航检查
	@RequestMapping(value = "/emergencybackCheck", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String emergencybackCheck(@RequestParam("taskid") int taskid) {

		Task task = new Task();
		task.setId(taskid);

		int oldStatus = taskServiceImpl.getTaskStatus(task);
		if (oldStatus == 9) {
			return JsonView.render(1, "");
		} else {
			return JsonView.render(0, "不可紧急返航，当前未处于飞行中！");
		}
	}

	// 紧急返航
	@RequestMapping(value = "/emergencyback", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String emergencyback(@RequestParam("taskid") int taskid) {

		Task task = new Task();
		task.setId(taskid);

		if (taskServiceImpl.setStatusTaskByTask(task, -1) == true) {
			return JsonView.render(1, "紧急返航执行成功！");
		} else {
			return JsonView.render(0, "紧急返航执行失败，请重试！");
		}

	}

	// 密码验证，防止误触
	@RequestMapping(value = "/passwordEnsure", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String passwordEnsure(@RequestParam("uavid") int uavid, @RequestParam("pwd") String pwd) {

		Uav uav = new Uav();
		uav.setId(uavid);
		Uav uav2 = uavServiceImpl.getPlaneByPlane(uav);
		if (uav2.getPassword().equals(pwd)) {
			return JsonView.render(1, "密码正确！");
		} else {
			return JsonView.render(0, "密码错误！");
		}
	}

	// 报告失联检查
	@RequestMapping(value = "/reportNotconnetCheck", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String reportNotconnetCheck(@RequestParam("taskid") int taskid) {

		Task task = new Task();
		task.setId(taskid);

		int oldStatus = taskServiceImpl.getTaskStatus(task);
		if (oldStatus == 9) {
			return JsonView.render(1, "");
		} else {
			return JsonView.render(0, "报告失联失败，当前未处于飞行中！");
		}
	}

	// 报告失联
	@RequestMapping(value = "/reportNotconnet", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String reportNotconnet(@RequestParam("taskid") int taskid) {

		Task task = new Task();
		task.setId(taskid);

		int oldStatus = taskServiceImpl.getTaskStatus(task);

		if (oldStatus == 9) {
			if (taskServiceImpl.setStatusTaskByTask(task, -1) == true) {
				return JsonView.render(1, "报告失联成功！");
			} else {
				return JsonView.render(0, "报告失联失败，请重试！");
			}
		} else {
			return JsonView.render(0, "报告失联失败，当前未处于飞行中！");
		}

	}

	// 无人机自检
	@RequestMapping(value = "/checkself", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String checkself(Task task) {

		int oldStatus = taskServiceImpl.getTaskStatus(task);
		if (oldStatus == 5) {
			// taskServiceImpl.setStatusTaskByTask(task, 6); //申请起飞
			if (taskServiceImpl.setStatusTaskByTask(task, 6) == true) {
				return JsonView.render(1, "无人机自检成功，等待申请起飞。");
			} else {
				return JsonView.render(0, "无人机自检失败，请重新确认。");
			}
		}
		return JsonView.render(0, "当前状态无法自检完成！");
	}

	// 更新无人机位置，写入到数据库
	@RequestMapping(value = "/updataPosition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public void takeoff(@RequestParam("uavid") int uavid, @RequestParam("position") String position) {

		Uav uav = new Uav();
		uav.setId(uavid);
		uav.setPosition(position);
		uavServiceImpl.updatePositionByUav(uav);

		// 不用返回数据

	}

	// 无人机起飞检查，确认当前状态是否可以起飞
	@RequestMapping(value = "/takeoffCheck", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String takeoffCheck(@RequestParam("taskid") int taskid) {

		Task task = new Task();
		task.setId(taskid);

		int oldStatus = taskServiceImpl.getTaskStatus(task);
		if (oldStatus == 8) {
			return JsonView.render(1, ""); // 可以起飞
		} else {
			return JsonView.render(0, "当前状态下不可放飞！"); // 不可起飞
		}
	}

	// 无人机起飞
	@RequestMapping(value = "/takeoff", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String takeoff(@RequestParam("taskid") int taskid) {

		Task task = new Task();
		task.setId(taskid);

		int oldStatus = taskServiceImpl.getTaskStatus(task);
		if (oldStatus == 8) {
			if (taskServiceImpl.setStatusTaskByTask(task, 9) == true) {
				// 放飞指令
				// uavServiceImpl.takeoff(task.getUavId());
				return JsonView.render(1, "无人机放飞成功！！");
			} else {
				return JsonView.render(0, "无人机放飞失败!");
			}

		} else {
			return JsonView.render(0, "当前状态下不可放飞！");
		}
	}

	// 无人机实时位置
	@RequestMapping(value = "/showPosition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showPosition(Task task) {

		// 测试****把任务状态设为飞行中
		int oldStatus = taskServiceImpl.getTaskStatus(task);
		if (oldStatus == 8) {
			// 实时位置指令
			uavServiceImpl.showData(task.getUavId());
			return JsonView.render(1, "飞机已经断开连接");
		} else {
			return JsonView.render(0, "无人机未起飞，不可放飞！");
		}
	}

	// 报告完成
	@RequestMapping(value = "/reportFinish", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String reportFinish(Task task) {

		int oldStatus = taskServiceImpl.getTaskStatus(task);
		if (oldStatus == 9) {
			if (taskServiceImpl.setStatusTaskByTask(task, 10) == true) {
				return JsonView.render(1, "飞行任务完成！");
			} else {
				return JsonView.render(0, "飞行任务完成报告失败！");
			}
		} else {
			return JsonView.render(0, "无人机未正常飞行，任务无法完成");
		}

	}

}
