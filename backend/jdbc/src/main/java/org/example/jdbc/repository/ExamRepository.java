package org.example.jdbc.repository;

import org.example.jdbc.entity.Exam;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExamRepository extends CrudRepository<Exam, String> {
    List<Exam> findByClassId(String classId);
    List<Exam> findByClassroomId(String classroomId);
}