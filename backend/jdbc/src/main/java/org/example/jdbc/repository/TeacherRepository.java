package org.example.jdbc.repository;

import org.example.jdbc.entity.Teacher;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeacherRepository extends CrudRepository<Teacher, String> {
    List<Teacher> findByMajorCode(String majorCode);
}