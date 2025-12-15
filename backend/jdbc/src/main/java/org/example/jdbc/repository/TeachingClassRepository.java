package org.example.jdbc.repository;

import org.example.jdbc.entity.TeachingClass;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeachingClassRepository extends CrudRepository<TeachingClass, String> {
    List<TeachingClass> findByCourseId(String courseId);
    List<TeachingClass> findByTeacherId(String teacherId);
}