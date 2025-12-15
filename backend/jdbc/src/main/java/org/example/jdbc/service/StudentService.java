package org.example.jdbc.service;

import org.example.jdbc.entity.Student;
import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(String studentId);
    List<Student> getStudentsByMajor(String majorCode);
    boolean addStudent(Student student);
    boolean updateStudent(Student student);
    boolean deleteStudent(String studentId);
}