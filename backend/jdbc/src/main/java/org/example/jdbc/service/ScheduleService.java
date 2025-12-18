package org.example.jdbc.service;

import org.example.jdbc.dto.ScheduleDTO;
import org.example.jdbc.entity.Schedule;
import java.util.List;

public interface ScheduleService {
    List<Schedule> getAllSchedules();
    List<Schedule> getSchedulesByClass(String classId);
    List<Schedule> getSchedulesByClassroom(String classroomId);
    List<ScheduleDTO> getSchedulesByTeacher(String teacherId);
    List<ScheduleDTO> getSchedulesByStudent(String studentId);
    boolean addSchedule(Schedule schedule);
    boolean updateSchedule(Schedule schedule);
    boolean deleteSchedule(Long id);
}