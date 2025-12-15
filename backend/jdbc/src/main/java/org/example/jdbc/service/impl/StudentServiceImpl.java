package org.example.jdbc.service.impl;

import org.example.jdbc.entity.Student;
import org.example.jdbc.repository.StudentRepository;
import org.example.jdbc.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getAllStudents() {
        return (List<Student>) studentRepository.findAll();
    }

    @Override
    public Student getStudentById(String studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    @Override
    public List<Student> getStudentsByMajor(String majorCode) {
        return studentRepository.findByMajorCode(majorCode);
    }

    @Override
    public boolean addStudent(Student student) {
        try {
            studentRepository.save(student);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateStudent(Student student) {
        if (studentRepository.existsById(student.getStudentId())) {
            try {
                studentRepository.save(student);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean deleteStudent(String studentId) {
        if (studentRepository.existsById(studentId)) {
            studentRepository.deleteById(studentId);
            return true;
        }
        return false;
    }
}