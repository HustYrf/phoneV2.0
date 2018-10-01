package hust.phone.web.controller.vo;

import hust.phone.mapper.pojo.Task;

public class TaskVO {

	private Task task;
	private String userAName;
	private String userZName;
	private String userCreatorName;
	private int role;
	private String deviceid;
	private String pathname;
	private String uavname;
	public String getUavname() {
		return uavname;
	}

	public void setUavname(String uavname) {
		this.uavname = uavname;
	}
	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getPathname() {
		return pathname;
	}

	public void setPathname(String pathname) {
		this.pathname = pathname;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

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
