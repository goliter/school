package org.example.jdbc.controller;

import org.example.jdbc.dto.TeacherDto;
import org.example.jdbc.entity.ApiResponse;
import org.example.jdbc.entity.Teacher;
import org.example.jdbc.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Teacher>>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(ApiResponse.success("获取教师列表成功", teachers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Teacher>> getTeacherById(@PathVariable("id") String teacherId) {
        Teacher teacher = teacherService.getTeacherById(teacherId);
        if (teacher == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "教师不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success("获取教师成功", teacher));
    }

    @GetMapping("/major/{majorCode}")
    public ResponseEntity<ApiResponse<List<Teacher>>> getTeachersByMajor(@PathVariable("majorCode") String majorCode) {
        List<Teacher> teachers = teacherService.getTeachersByMajor(majorCode);
        return ResponseEntity.ok(ApiResponse.success("获取专业教师列表成功", teachers));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addTeacher(@RequestBody TeacherDto teacherDto) {
        boolean success = teacherService.addTeacher(teacherDto);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("教师添加成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "教师添加失败"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateTeacher(@PathVariable("id") String teacherId, @RequestBody TeacherDto teacherDto) {
        teacherDto.setTeacherId(teacherId);
        boolean success = teacherService.updateTeacher(teacherDto);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("教师更新成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "教师更新失败"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeacher(@PathVariable("id") String teacherId) {
        boolean success = teacherService.deleteTeacher(teacherId);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("教师删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "教师删除失败"));
    }
}