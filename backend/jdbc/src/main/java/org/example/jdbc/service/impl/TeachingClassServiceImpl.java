package org.example.jdbc.service.impl;

import org.example.jdbc.entity.TeachingClass;
import org.example.jdbc.repository.TeachingClassRepository;
import org.example.jdbc.service.TeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachingClassServiceImpl implements TeachingClassService {
    private final TeachingClassRepository teachingClassRepository;

    @Autowired
    public TeachingClassServiceImpl(TeachingClassRepository teachingClassRepository) {
        this.teachingClassRepository = teachingClassRepository;
    }

    @Override
    public List<TeachingClass> getAllTeachingClasses() {
        return (List<TeachingClass>) teachingClassRepository.findAll();
    }

    @Override
    public TeachingClass getTeachingClassById(String classId) {
        return teachingClassRepository.findById(classId).orElse(null);
    }

    @Override
    public List<TeachingClass> getTeachingClassesByCourse(String courseId) {
        return teachingClassRepository.findByCourseId(courseId);
    }

    @Override
    public List<TeachingClass> getTeachingClassesByTeacher(String teacherId) {
        return teachingClassRepository.findByTeacherId(teacherId);
    }

    @Override
    public boolean addTeachingClass(TeachingClass teachingClass) {
        try {
            teachingClassRepository.save(teachingClass);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateTeachingClass(TeachingClass teachingClass) {
        try {
            if (teachingClassRepository.existsById(teachingClass.getClassId())) {
                teachingClassRepository.save(teachingClass);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteTeachingClass(String classId) {
        try {
            if (teachingClassRepository.existsById(classId)) {
                teachingClassRepository.deleteById(classId);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}