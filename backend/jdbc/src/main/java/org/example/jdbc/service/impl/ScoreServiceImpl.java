package org.example.jdbc.service.impl;

import org.example.jdbc.entity.Score;
import org.example.jdbc.entity.ScoreId;
import org.example.jdbc.repository.ScoreRepository;
import org.example.jdbc.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;

    @Autowired
    public ScoreServiceImpl(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @Override
    public List<Score> getAllScores() {
        return (List<Score>) scoreRepository.findAll();
    }

    @Override
    public List<Score> getScoresByStudent(String studentId) {
        return scoreRepository.findByStudentId(studentId);
    }

    @Override
    public List<Score> getScoresByCourse(String courseId) {
        return scoreRepository.findByCourseId(courseId);
    }

    @Override
    public Score getScore(String studentId, String courseId) {
        return scoreRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public boolean addScore(Score score) {
        try {
            scoreRepository.save(score);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateScore(Score score) {
        try {
            // 直接使用复合主键检查记录是否存在
            ScoreId scoreId = new ScoreId(score.getStudentId(), score.getExamId());
            if (scoreRepository.existsById(scoreId)) {
                scoreRepository.save(score);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteScore(String studentId, String courseId) {
        try {
            Score score = getScore(studentId, courseId);
            if (score != null) {
                scoreRepository.delete(score);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}