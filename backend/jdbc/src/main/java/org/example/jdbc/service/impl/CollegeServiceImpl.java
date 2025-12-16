package org.example.jdbc.service.impl;

import org.example.jdbc.entity.College;
import org.example.jdbc.repository.CollegeRepository;
import org.example.jdbc.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class CollegeServiceImpl implements CollegeService {
    private static final Logger LOGGER = Logger.getLogger(CollegeServiceImpl.class.getName());
    private final CollegeRepository collegeRepository;

    @Autowired
    public CollegeServiceImpl(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    @Override
    public List<College> getAllColleges() {
        return (List<College>) collegeRepository.findAll();
    }

    @Override
    public College getCollegeById(String collegeId) {
        return collegeRepository.findById(collegeId).orElse(null);
    }

    @Override
    public boolean addCollege(College college) {
        try {
            collegeRepository.save(college);
            return true;
        } catch (Exception e) {
            LOGGER.severe("学院添加失败: " + e.getMessage());
            LOGGER.severe("异常详情: ");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCollege(College college) {
        if (collegeRepository.existsById(college.getCollegeId())) {
            try {
                collegeRepository.save(college);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean deleteCollege(String collegeId) {
        if (collegeRepository.existsById(collegeId)) {
            collegeRepository.deleteById(collegeId);
            return true;
        }
        return false;
    }
}