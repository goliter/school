package org.example.jdbc.repository;

import org.example.jdbc.entity.Elective;
import org.example.jdbc.entity.ElectiveId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ElectiveRepository extends CrudRepository<Elective, ElectiveId> {
    List<Elective> findByStudentId(String studentId);
    List<Elective> findByClassId(String classId);
    long countByClassId(String classId);
}