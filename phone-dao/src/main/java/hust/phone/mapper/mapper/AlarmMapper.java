package hust.phone.mapper.mapper;

import hust.phone.mapper.pojo.Alarm;
import org.apache.ibatis.annotations.Param;

import java.util.List;



public interface AlarmMapper {

	List<Alarm> selectALLAlarm();
	List<Alarm> selectAllAlarmByCreateTimeDesc();
	int alarmCount(Alarm alarm);
	
    Alarm selectInfoById(Integer id);
	int updateByAlarmId(Integer id);
    int insertAlarmSelective(Alarm alarm);
	int updateDesByAlarmId(Integer id, String description);
	List<Alarm> getAlarmsByTaskId(@Param("taskId") Integer taskId);
}
