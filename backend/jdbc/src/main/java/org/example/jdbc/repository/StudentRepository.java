package org.example.jdbc.repository;

import org.example.jdbc.entity.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student, String> {
    List<Student> findByMajorCode(String majorCode);
}