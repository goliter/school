package org.example.jdbc.controller;

import org.example.jdbc.dto.TeachingClassDTO;
import org.example.jdbc.entity.ApiResponse;
import org.example.jdbc.entity.TeachingClass;
import org.example.jdbc.service.TeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teaching-classes")
public class TeachingClassController {

    @Autowired
    private TeachingClassService teachingClassService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeachingClass>>> getAllTeachingClasses() {
        List<TeachingClass> teachingClasses = teachingClassService.getAllTeachingClasses();
        return ResponseEntity.ok(ApiResponse.success("获取教学班列表成功", teachingClasses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeachingClass>> getTeachingClassById(@PathVariable("id") String classId) {
        TeachingClass teachingClass = teachingClassService.getTeachingClassById(classId);
        if (teachingClass == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "教学班不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success("获取教学班成功", teachingClass));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<TeachingClass>>> getTeachingClassesByCourse(@PathVariable("courseId") String courseId) {
        List<TeachingClass> teachingClasses = teachingClassService.getTeachingClassesByCourse(courseId);
        return ResponseEntity.ok(ApiResponse.success("获取课程教学班列表成功", teachingClasses));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<List<TeachingClassDTO>>> getTeachingClassesByTeacher(@PathVariable("teacherId") String teacherId) {
        List<TeachingClassDTO> teachingClasses = teachingClassService.getTeachingClassesByTeacher(teacherId);
        return ResponseEntity.ok(ApiResponse.success("获取教师教学班列表成功", teachingClasses));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TeachingClass>> addTeachingClass(@RequestBody TeachingClass teachingClass) {
        boolean success = teachingClassService.addTeachingClass(teachingClass);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("教学班添加成功", teachingClass));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "教学班添加失败"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeachingClass>> updateTeachingClass(@PathVariable("id") String classId, @RequestBody TeachingClass teachingClass) {
        teachingClass.setClassId(classId);
        boolean success = teachingClassService.updateTeachingClass(teachingClass);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("教学班更新成功", teachingClass));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "教学班更新失败"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeachingClass(@PathVariable("id") String classId) {
        boolean success = teachingClassService.deleteTeachingClass(classId);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("教学班删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "教学班删除失败"));
    }
}