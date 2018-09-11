package hust.phone.mapper.mapper;

import hust.phone.mapper.pojo.Task;
import hust.phone.mapper.pojo.TaskExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskMapper {
    int countByExample(TaskExample example);

    int deleteByExample(TaskExample example);

    int deleteByPrimaryKey(String taskid);

    int insert(Task record);

    int insertSelective(Task record);

    List<Task> selectByExample(TaskExample example);

    Task selectByPrimaryKey(int id);

    int updateByExampleSelective(@Param("record") Task record, @Param("example") TaskExample example);

    int updateByExample(@Param("record") Task record, @Param("example") TaskExample example);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);
    
    //以下为自定义查询方法
    
	String getStatusByTask(Task task);

	List<Task> getTasklistByUserCreator(int userCreator);

	List<Task> selectByTask(Task task);

	int getTaskStatus(Task task);
}