package org.example.jdbc.service;

import org.example.jdbc.entity.Score;
import java.util.List;

public interface ScoreService {
    List<Score> getAllScores();
    List<Score> getScoresByStudent(String studentId);
    List<Score> getScoresByCourse(String courseId);
    Score getScore(String studentId, String courseId);
    boolean addScore(Score score);
    boolean updateScore(Score score);
    boolean deleteScore(String studentId, String courseId);
}