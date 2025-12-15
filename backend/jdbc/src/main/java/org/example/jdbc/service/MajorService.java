package org.example.jdbc.service;

import org.example.jdbc.entity.Major;
import java.util.List;

public interface MajorService {
    List<Major> getAllMajors();
    Major getMajorById(String majorCode);
    boolean addMajor(Major major);
    boolean updateMajor(Major major);
    boolean deleteMajor(String majorCode);
}