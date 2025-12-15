package org.example.jdbc.repository;

import org.example.jdbc.entity.Schedule;
import org.example.jdbc.entity.ScheduleId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleRepository extends CrudRepository<Schedule, ScheduleId> {
    List<Schedule> findByClassId(String classId);
    List<Schedule> findByClassroomId(String classroomId);
}