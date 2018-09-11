package hust.phone.service.interFace;

import java.util.List;

import hust.phone.mapper.pojo.Task;

public interface TaskService {
	
	List<Task> selectAllByTask(Task task);

	Task getTaskByTask(Task task);

	boolean rollbackTaskByTask(Task task,int status);

	boolean setStatusTaskByTask(Task task, int status);

	Task selectOneExeTask(Task task);
	
	int getTaskStatus(Task task);
	

}
