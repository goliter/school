package org.example.jdbc.controller;

import org.example.jdbc.dto.StudentDto;
import org.example.jdbc.entity.ApiResponse;
import org.example.jdbc.entity.Student;
import org.example.jdbc.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success(students));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudentById(@PathVariable("id") String studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "学生不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(student));
    }

    @GetMapping("/major/{majorCode}")
    public ResponseEntity<ApiResponse<List<Student>>> getStudentsByMajor(@PathVariable("majorCode") String majorCode) {
        List<Student> students = studentService.getStudentsByMajor(majorCode);
        return ResponseEntity.ok(ApiResponse.success(students));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addStudent(@RequestBody StudentDto studentDto) {
        boolean success = studentService.addStudent(studentDto);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("学生添加成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "学生添加失败"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateStudent(@PathVariable("id") String studentId, @RequestBody StudentDto studentDto) {
        studentDto.setStudentId(studentId);
        boolean success = studentService.updateStudent(studentDto);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("学生更新成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "学生更新失败"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable("id") String studentId) {
        boolean success = studentService.deleteStudent(studentId);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("学生删除成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "学生删除失败"));
    }
}