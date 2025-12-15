package org.example.jdbc.repository;

import org.example.jdbc.entity.College;
import org.springframework.data.repository.CrudRepository;

public interface CollegeRepository extends CrudRepository<College, String> {
}