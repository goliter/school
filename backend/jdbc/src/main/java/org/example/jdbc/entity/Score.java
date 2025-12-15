package org.example.jdbc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "score")
@IdClass(ScoreId.class)
public class Score {
    @Id
    @Column(name = "student_id")
    private String studentId;
    @Id
    @Column(name = "exam_id")
    private String examId;
    private Double score;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}