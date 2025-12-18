package org.example.jdbc.controller;

import org.example.jdbc.entity.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable("id") String courseId) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "课程不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(course));
    }

    @GetMapping("/major/{majorCode}")
    public ResponseEntity<ApiResponse<List<Course>>> getCoursesByMajor(@PathVariable("majorCode") String majorCode) {
        List<Course> courses = courseService.getCoursesByMajor(majorCode);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<List<Course>>> getCoursesByTeacher(@PathVariable("teacherId") String teacherId) {
        List<Course> courses = courseService.getCoursesByTeacher(teacherId);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Course>> addCourse(@RequestBody Course course) {
        boolean success = courseService.addCourse(course);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("课程添加成功", course));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "课程添加失败"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> updateCourse(@PathVariable("id") String courseId, @RequestBody Course course) {
        course.setCourseId(courseId);
        boolean success = courseService.updateCourse(course);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("课程更新成功", course));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "课程更新失败"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable("id") String courseId) {
        boolean success = courseService.deleteCourse(courseId);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("课程删除成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "课程删除失败"));
    }
}