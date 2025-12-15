package org.example.jdbc.repository;

import org.example.jdbc.entity.Major;
import org.springframework.data.repository.CrudRepository;

public interface MajorRepository extends CrudRepository<Major, String> {
}