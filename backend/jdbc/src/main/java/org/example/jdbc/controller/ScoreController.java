package org.example.jdbc.controller;

import org.example.jdbc.entity.ApiResponse;
import org.example.jdbc.entity.Score;
import org.example.jdbc.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Score>>> getAllScores() {
        List<Score> scores = scoreService.getAllScores();
        return ResponseEntity.ok(ApiResponse.success(scores));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Score>>> getScoresByStudentId(@PathVariable String studentId) {
        List<Score> scores = scoreService.getScoresByStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success(scores));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Score>>> getScoresByCourseId(@PathVariable String courseId) {
        List<Score> scores = scoreService.getScoresByCourse(courseId);
        return ResponseEntity.ok(ApiResponse.success(scores));
    }

    @GetMapping("/{studentId}/{courseId}")
    public ResponseEntity<ApiResponse<Score>> getScore(@PathVariable String studentId, @PathVariable String courseId) {
        Score score = scoreService.getScore(studentId, courseId);
        if (score == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "成绩记录不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(score));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Score>> addScore(@RequestBody Score score) {
        boolean result = scoreService.addScore(score);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("成绩添加成功", score));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "成绩添加失败"));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Score>> updateScore(@RequestBody Score score) {
        boolean result = scoreService.updateScore(score);
        if (result) {
            return ResponseEntity.ok(ApiResponse.success("成绩更新成功", score));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "成绩更新失败"));
    }

    @DeleteMapping("/{studentId}/{courseId}")
    public ResponseEntity<ApiResponse<Void>> deleteScore(@PathVariable String studentId, @PathVariable String courseId) {
        boolean result = scoreService.deleteScore(studentId, courseId);
        if (result) {
            return ResponseEntity.ok(ApiResponse.success("成绩删除成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "成绩删除失败"));
    }
}