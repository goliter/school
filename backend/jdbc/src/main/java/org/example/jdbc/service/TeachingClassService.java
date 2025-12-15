package org.example.jdbc.service;

import org.example.jdbc.entity.TeachingClass;
import java.util.List;

public interface TeachingClassService {
    List<TeachingClass> getAllTeachingClasses();
    TeachingClass getTeachingClassById(String classId);
    List<TeachingClass> getTeachingClassesByCourse(String courseId);
    List<TeachingClass> getTeachingClassesByTeacher(String teacherId);
    boolean addTeachingClass(TeachingClass teachingClass);
    boolean updateTeachingClass(TeachingClass teachingClass);
    boolean deleteTeachingClass(String classId);
}