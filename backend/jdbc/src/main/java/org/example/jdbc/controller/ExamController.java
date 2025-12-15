package org.example.jdbc.controller;

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
    public ResponseEntity<List<Exam>> getAllExams() {
        List<Exam> exams = examService.getAllExams();
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable("id") String examId) {
        Exam exam = examService.getExamById(examId);
        if (exam == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(exam, HttpStatus.OK);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Exam>> getExamsByClass(@PathVariable("classId") String classId) {
        List<Exam> exams = examService.getExamsByClass(classId);
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<Exam>> getExamsByClassroom(@PathVariable("classroomId") String classroomId) {
        List<Exam> exams = examService.getExamsByClassroom(classroomId);
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addExam(@RequestBody Exam exam) {
        boolean success = examService.addExam(exam);
        if (success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExam(@PathVariable("id") String examId, @RequestBody Exam exam) {
        exam.setExamId(examId);
        boolean success = examService.updateExam(exam);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable("id") String examId) {
        boolean success = examService.deleteExam(examId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}