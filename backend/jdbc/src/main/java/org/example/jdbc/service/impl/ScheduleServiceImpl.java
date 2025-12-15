package org.example.jdbc.service.impl;

import org.example.jdbc.entity.Schedule;
import org.example.jdbc.entity.ScheduleId;
import org.example.jdbc.repository.ScheduleRepository;
import org.example.jdbc.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return (List<Schedule>) scheduleRepository.findAll();
    }

    @Override
    public List<Schedule> getSchedulesByClass(String classId) {
        return scheduleRepository.findByClassId(classId);
    }

    @Override
    public List<Schedule> getSchedulesByClassroom(String classroomId) {
        return scheduleRepository.findByClassroomId(classroomId);
    }

    @Override
    public boolean addSchedule(Schedule schedule) {
        try {
            scheduleRepository.save(schedule);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateSchedule(Schedule schedule) {
        try {
            ScheduleId scheduleId = new ScheduleId(
                schedule.getClassId(),
                schedule.getClassroomId(),
                schedule.getWeek(),
                schedule.getWeekday(),
                schedule.getPeriods()
            );
            if (scheduleRepository.existsById(scheduleId)) {
                scheduleRepository.save(schedule);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteSchedule(String classId, int week, int weekday, int periods) {
        try {
            ScheduleId scheduleId = new ScheduleId(classId, null, week, weekday, periods);
            // 由于classroomId是复合主键的一部分，我们需要先查找完整的主键信息
            List<Schedule> schedules = scheduleRepository.findByClassId(classId);
            for (Schedule schedule : schedules) {
                if (schedule.getWeek() == week && schedule.getWeekday() == weekday && schedule.getPeriods() == periods) {
                    scheduleId.setClassroomId(schedule.getClassroomId());
                    scheduleRepository.deleteById(scheduleId);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}