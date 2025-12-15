package org.example.jdbc.service.impl;

import org.example.jdbc.entity.Elective;
import org.example.jdbc.entity.ElectiveId;
import org.example.jdbc.repository.ElectiveRepository;
import org.example.jdbc.service.ElectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ElectiveServiceImpl implements ElectiveService {
    private final ElectiveRepository electiveRepository;

    @Autowired
    public ElectiveServiceImpl(ElectiveRepository electiveRepository) {
        this.electiveRepository = electiveRepository;
    }

    @Override
    public List<Elective> getAllElectives() {
        return (List<Elective>) electiveRepository.findAll();
    }

    @Override
    public List<Elective> getElectivesByStudentId(String studentId) {
        return electiveRepository.findByStudentId(studentId);
    }

    @Override
    public List<Elective> getElectivesByClassId(String classId) {
        return electiveRepository.findByClassId(classId);
    }

    @Override
    public Elective getElective(String studentId, String classId) {
        ElectiveId electiveId = new ElectiveId(studentId, classId);
        return electiveRepository.findById(electiveId).orElse(null);
    }

    @Override
    public boolean addElective(Elective elective) {
        try {
            electiveRepository.save(elective);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateElective(Elective elective) {
        ElectiveId electiveId = new ElectiveId(elective.getStudentId(), elective.getClassId());
        if (electiveRepository.existsById(electiveId)) {
            try {
                electiveRepository.save(elective);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean deleteElective(String studentId, String classId) {
        ElectiveId electiveId = new ElectiveId(studentId, classId);
        if (electiveRepository.existsById(electiveId)) {
            electiveRepository.deleteById(electiveId);
            return true;
        }
        return false;
    }
}