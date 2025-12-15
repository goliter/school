package org.example.jdbc.service;

import org.example.jdbc.entity.Course;
import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    Course getCourseById(String courseId);
    List<Course> getCoursesByMajor(String majorCode);
    boolean addCourse(Course course);
    boolean updateCourse(Course course);
    boolean deleteCourse(String courseId);
}