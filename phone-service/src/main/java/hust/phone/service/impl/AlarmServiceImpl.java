package hust.phone.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hust.phone.mapper.mapper.AlarmMapper;
import hust.phone.mapper.pojo.Alarm;
import hust.phone.service.interFace.AlarmService;


@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    public AlarmMapper alarmMapper;

    @Override
    public List<Alarm> getAllAlarm() {

        List<Alarm> alarmList = alarmMapper.selectALLAlarm();
        return alarmList;
    }

    @Override
    public Alarm selectAlarmById(int id) {
        
            return alarmMapper.selectInfoById(id);
      
    }

	@Override
	public boolean updateAlarmStatus(int id) {
		
		if(alarmMapper.updateByAlarmId(id)==1)
			return true;
		else
			return false;
		
	}

}
