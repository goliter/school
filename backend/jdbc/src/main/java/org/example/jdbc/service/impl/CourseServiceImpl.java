package org.example.jdbc.service.impl;

import org.example.jdbc.entity.Course;
import org.example.jdbc.repository.CourseRepository;
import org.example.jdbc.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return (List<Course>) courseRepository.findAll();
    }

    @Override
    public Course getCourseById(String courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }

    @Override
    public List<Course> getCoursesByMajor(String majorCode) {
        return courseRepository.findByMajorCode(majorCode);
    }

    @Override
    public boolean addCourse(Course course) {
        try {
            courseRepository.save(course);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateCourse(Course course) {
        if (courseRepository.existsById(course.getCourseId())) {
            try {
                courseRepository.save(course);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean deleteCourse(String courseId) {
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId);
            return true;
        }
        return false;
    }
}