package hust.phone.web.controller.vo;

import hust.phone.mapper.pojo.Task;

public class TaskVO {

	private Task task;
	private String userAName;
	private String userZName;
	private String userCreatorName;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getUserAName() {
		return userAName;
	}

	public void setUserAName(String userAName) {
		this.userAName = userAName;
	}

	public String getUserZName() {
		return userZName;
	}

	public void setUserZName(String userZName) {
		this.userZName = userZName;
	}

	public String getUserCreatorName() {
		return userCreatorName;
	}

	public void setUserCreatorName(String userCreatorName) {
		this.userCreatorName = userCreatorName;
	}

}
