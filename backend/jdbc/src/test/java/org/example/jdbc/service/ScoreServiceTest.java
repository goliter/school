package org.example.jdbc.service;

import org.example.jdbc.entity.Score;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ScoreServiceTest {

    @Autowired
    private ScoreService scoreService;

    @Test
    void testAddAndGetScore() {
        // 创建测试分数记录
        Score score = new Score();
        score.setStudentId("STU001");
        score.setExamId("EXAM001");
        score.setScore(85.5);

        // 添加分数记录
        boolean addResult = scoreService.addScore(score);
        assertTrue(addResult, "分数记录添加失败");

        // 获取分数记录
        Score retrievedScore = scoreService.getScore("STU001", "EXAM001");
        assertNotNull(retrievedScore, "分数记录查询失败");
        assertEquals(85.5, retrievedScore.getScore(), 0.01, "分数不匹配");
    }

    @Test
    void testUpdateScore() {
        // 更新分数记录
        Score score = new Score();
        score.setStudentId("STU001");
        score.setExamId("EXAM001");
        score.setScore(90.0);

        boolean updateResult = scoreService.updateScore(score);
        assertTrue(updateResult, "分数记录更新失败");

        // 验证更新
        Score updatedScore = scoreService.getScore("STU001", "EXAM001");
        assertEquals(90.0, updatedScore.getScore(), 0.01, "分数更新失败");
    }

    @Test
    void testGetScoresByStudent() {
        // 按学生ID查询分数记录
        List<Score> scores = scoreService.getScoresByStudent("STU001");
        assertNotNull(scores, "按学生ID查询分数记录失败");
        // 至少包含我们添加的测试记录
        assertTrue(scores.size() > 0, "该学生没有分数记录");
    }

    @Test
    void testGetAllScores() {
        List<Score> scores = scoreService.getAllScores();
        assertNotNull(scores, "分数列表查询失败");
        // 至少包含我们添加的测试记录
        assertTrue(scores.size() > 0, "分数列表为空");
    }
}