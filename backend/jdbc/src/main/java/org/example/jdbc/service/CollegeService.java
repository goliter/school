package org.example.jdbc.service;

import org.example.jdbc.entity.College;
import java.util.List;

public interface CollegeService {
    List<College> getAllColleges();
    College getCollegeById(String collegeId);
    boolean addCollege(College college);
    boolean updateCollege(College college);
    boolean deleteCollege(String collegeId);
}