package org.example.jdbc.service.impl;

import org.example.jdbc.entity.Course;
import org.example.jdbc.entity.TeachingClass;
import org.example.jdbc.repository.CourseRepository;
import org.example.jdbc.repository.TeachingClassRepository;
import org.example.jdbc.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final TeachingClassRepository teachingClassRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, TeachingClassRepository teachingClassRepository) {
        this.courseRepository = courseRepository;
        this.teachingClassRepository = teachingClassRepository;
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
    public List<Course> getCoursesByTeacher(String teacherId) {
        // 获取教师所有的教学班级
        List<TeachingClass> teachingClasses = teachingClassRepository.findByTeacherId(teacherId);
        
        // 提取所有课程ID（去重）
        Set<String> courseIds = new HashSet<>();
        for (TeachingClass teachingClass : teachingClasses) {
            courseIds.add(teachingClass.getCourseId());
        }
        
        // 根据课程ID获取课程列表
        List<Course> courses = new ArrayList<>();
        for (String courseId : courseIds) {
            Course course = courseRepository.findById(courseId).orElse(null);
            if (course != null) {
                courses.add(course);
            }
        }
        
        return courses;
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