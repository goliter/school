package org.example.jdbc.service.impl;

import org.example.jdbc.entity.Major;
import org.example.jdbc.repository.MajorRepository;
import org.example.jdbc.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {
    private final MajorRepository majorRepository;

    @Autowired
    public MajorServiceImpl(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    @Override
    public List<Major> getAllMajors() {
        return (List<Major>) majorRepository.findAll();
    }

    @Override
    public Major getMajorById(String majorCode) {
        return majorRepository.findById(majorCode).orElse(null);
    }

    @Override
    public boolean addMajor(Major major) {
        try {
            majorRepository.save(major);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateMajor(Major major) {
        if (majorRepository.existsById(major.getMajorCode())) {
            try {
                majorRepository.save(major);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean deleteMajor(String majorCode) {
        if (majorRepository.existsById(majorCode)) {
            majorRepository.deleteById(majorCode);
            return true;
        }
        return false;
    }
}