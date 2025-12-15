package org.example.jdbc.service;

import org.example.jdbc.entity.Schedule;
import java.util.List;

public interface ScheduleService {
    List<Schedule> getAllSchedules();
    List<Schedule> getSchedulesByClass(String classId);
    List<Schedule> getSchedulesByClassroom(String classroomId);
    boolean addSchedule(Schedule schedule);
    boolean updateSchedule(Schedule schedule);
    boolean deleteSchedule(String classId, int week, int weekday, int periods);
}