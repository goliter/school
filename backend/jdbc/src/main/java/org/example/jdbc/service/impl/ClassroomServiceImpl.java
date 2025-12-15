package org.example.jdbc.service.impl;

import org.example.jdbc.entity.Classroom;
import org.example.jdbc.repository.ClassroomRepository;
import org.example.jdbc.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;

    @Autowired
    public ClassroomServiceImpl(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        return (List<Classroom>) classroomRepository.findAll();
    }

    @Override
    public Classroom getClassroomById(String classroomId) {
        return classroomRepository.findById(classroomId).orElse(null);
    }

    @Override
    public List<Classroom> getClassroomsByBuilding(String building) {
        return classroomRepository.findByBuilding(building);
    }

    @Override
    public boolean addClassroom(Classroom classroom) {
        try {
            classroomRepository.save(classroom);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateClassroom(Classroom classroom) {
        if (classroomRepository.existsById(classroom.getClassroomId())) {
            try {
                classroomRepository.save(classroom);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean deleteClassroom(String classroomId) {
        if (classroomRepository.existsById(classroomId)) {
            classroomRepository.deleteById(classroomId);
            return true;
        }
        return false;
    }
}