package org.example.jdbc.service;

import org.example.jdbc.entity.Teacher;
import java.util.List;

public interface TeacherService {
    List<Teacher> getAllTeachers();
    Teacher getTeacherById(String teacherId);
    List<Teacher> getTeachersByMajor(String majorCode);
    boolean addTeacher(Teacher teacher);
    boolean updateTeacher(Teacher teacher);
    boolean deleteTeacher(String teacherId);
}