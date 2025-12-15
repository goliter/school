package org.example.jdbc.service.impl;

import org.example.jdbc.entity.Teacher;
import org.example.jdbc.repository.TeacherRepository;
import org.example.jdbc.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return (List<Teacher>) teacherRepository.findAll();
    }

    @Override
    public Teacher getTeacherById(String teacherId) {
        return teacherRepository.findById(teacherId).orElse(null);
    }

    @Override
    public List<Teacher> getTeachersByMajor(String majorCode) {
        return teacherRepository.findByMajorCode(majorCode);
    }

    @Override
    public boolean addTeacher(Teacher teacher) {
        try {
            teacherRepository.save(teacher);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateTeacher(Teacher teacher) {
        if (teacherRepository.existsById(teacher.getTeacherId())) {
            try {
                teacherRepository.save(teacher);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean deleteTeacher(String teacherId) {
        if (teacherRepository.existsById(teacherId)) {
            teacherRepository.deleteById(teacherId);
            return true;
        }
        return false;
    }
}