package org.example.jdbc.repository;

import org.example.jdbc.entity.Schedule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    List<Schedule> findByClassId(String classId);
    List<Schedule> findByClassroomId(String classroomId);
    Schedule findFirstByClassId(String classId);
}