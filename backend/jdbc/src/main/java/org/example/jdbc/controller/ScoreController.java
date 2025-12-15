package org.example.jdbc.controller;

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
    public ResponseEntity<List<Score>> getAllScores() {
        List<Score> scores = scoreService.getAllScores();
        return new ResponseEntity<>(scores, HttpStatus.OK);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Score>> getScoresByStudentId(@PathVariable String studentId) {
        List<Score> scores = scoreService.getScoresByStudent(studentId);
        return new ResponseEntity<>(scores, HttpStatus.OK);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Score>> getScoresByCourseId(@PathVariable String courseId) {
        List<Score> scores = scoreService.getScoresByCourse(courseId);
        return new ResponseEntity<>(scores, HttpStatus.OK);
    }

    @GetMapping("/{studentId}/{courseId}")
    public ResponseEntity<Score> getScore(@PathVariable String studentId, @PathVariable String courseId) {
        Score score = scoreService.getScore(studentId, courseId);
        return new ResponseEntity<>(score, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Boolean> addScore(@RequestBody Score score) {
        boolean result = scoreService.addScore(score);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Boolean> updateScore(@RequestBody Score score) {
        boolean result = scoreService.updateScore(score);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{studentId}/{courseId}")
    public ResponseEntity<Boolean> deleteScore(@PathVariable String studentId, @PathVariable String courseId) {
        boolean result = scoreService.deleteScore(studentId, courseId);
        return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
    }
}