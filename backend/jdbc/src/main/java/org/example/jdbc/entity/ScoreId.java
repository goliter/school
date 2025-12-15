package org.example.jdbc.entity;

import java.io.Serializable;

public class ScoreId implements Serializable {
    private String studentId;
    private String examId;

    public ScoreId() {
    }

    public ScoreId(String studentId, String examId) {
        this.studentId = studentId;
        this.examId = examId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoreId scoreId = (ScoreId) o;

        if (!studentId.equals(scoreId.studentId)) return false;
        return examId.equals(scoreId.examId);
    }

    @Override
    public int hashCode() {
        int result = studentId.hashCode();
        result = 31 * result + examId.hashCode();
        return result;
    }
}