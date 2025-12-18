package org.example.jdbc.controller;

import org.example.jdbc.entity.ApiResponse;
import org.example.jdbc.entity.Exam;
import org.example.jdbc.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Exam>>> getAllExams() {
        List<Exam> exams = examService.getAllExams();
        return ResponseEntity.ok(ApiResponse.success(exams));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Exam>> getExamById(@PathVariable("id") String examId) {
        Exam exam = examService.getExamById(examId);
        if (exam == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "考试不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(exam));
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<ApiResponse<List<Exam>>> getExamsByClass(@PathVariable("classId") String classId) {
        List<Exam> exams = examService.getExamsByClass(classId);
        return ResponseEntity.ok(ApiResponse.success(exams));
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<ApiResponse<List<Exam>>> getExamsByClassroom(@PathVariable("classroomId") String classroomId) {
        List<Exam> exams = examService.getExamsByClassroom(classroomId);
        return ResponseEntity.ok(ApiResponse.success(exams));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<List<Exam>>> getExamsByTeacher(@PathVariable("teacherId") String teacherId) {
        List<Exam> exams = examService.getExamsByTeacher(teacherId);
        return ResponseEntity.ok(ApiResponse.success(exams));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Exam>> addExam(@RequestBody Exam exam) {
        boolean success = examService.addExam(exam);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("考试添加成功", exam));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "考试添加失败"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Exam>> updateExam(@PathVariable("id") String examId, @RequestBody Exam exam) {
        exam.setExamId(examId);
        boolean success = examService.updateExam(exam);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("考试更新成功", exam));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "考试更新失败"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExam(@PathVariable("id") String examId) {
        boolean success = examService.deleteExam(examId);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("考试删除成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "考试删除失败"));
    }
}