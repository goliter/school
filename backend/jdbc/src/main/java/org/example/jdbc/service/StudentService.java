package org.example.jdbc.service;

import org.example.jdbc.dto.StudentDto;
import org.example.jdbc.entity.Student;
import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(String studentId);
    List<Student> getStudentsByMajor(String majorCode);
    boolean addStudent(StudentDto studentDto);
    boolean updateStudent(StudentDto studentDto);
    boolean deleteStudent(String studentId);
}