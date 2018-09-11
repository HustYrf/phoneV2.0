package hust.phone.service.interFace;

import java.util.List;

import hust.phone.mapper.pojo.Alarm;



public interface AlarmService {

    List<Alarm> getAllAlarm();

    Alarm selectAlarmById(int id);

    boolean updateAlarmStatus(int id);
}
