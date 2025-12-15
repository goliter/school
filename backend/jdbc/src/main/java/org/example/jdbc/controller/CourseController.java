package org.example.jdbc.controller;

import org.example.jdbc.entity.Course;
import org.example.jdbc.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable("id") String courseId) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping("/major/{majorCode}")
    public ResponseEntity<List<Course>> getCoursesByMajor(@PathVariable("majorCode") String majorCode) {
        List<Course> courses = courseService.getCoursesByMajor(majorCode);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addCourse(@RequestBody Course course) {
        boolean success = courseService.addCourse(course);
        if (success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCourse(@PathVariable("id") String courseId, @RequestBody Course course) {
        course.setCourseId(courseId);
        boolean success = courseService.updateCourse(course);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable("id") String courseId) {
        boolean success = courseService.deleteCourse(courseId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}