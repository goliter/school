package org.example.jdbc.repository;

import org.example.jdbc.entity.Course;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, String> {
    List<Course> findByMajorCode(String majorCode);
}