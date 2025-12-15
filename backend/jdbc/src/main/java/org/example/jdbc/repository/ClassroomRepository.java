package org.example.jdbc.repository;

import org.example.jdbc.entity.Classroom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClassroomRepository extends CrudRepository<Classroom, String> {
    List<Classroom> findByBuilding(String building);
}