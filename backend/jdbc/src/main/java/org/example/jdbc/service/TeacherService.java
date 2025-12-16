package org.example.jdbc.service;

import org.example.jdbc.dto.TeacherDto;
import org.example.jdbc.entity.Teacher;
import java.util.List;

public interface TeacherService {
    List<Teacher> getAllTeachers();
    Teacher getTeacherById(String teacherId);
    List<Teacher> getTeachersByMajor(String majorCode);
    boolean addTeacher(TeacherDto teacherDto);
    boolean updateTeacher(TeacherDto teacherDto);
    boolean deleteTeacher(String teacherId);
}