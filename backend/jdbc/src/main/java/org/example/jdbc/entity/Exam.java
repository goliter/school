package org.example.jdbc.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "exam")
public class Exam {
    @Id
    @Column(name = "exam_id")
    private String examId;
    private String examName;
    @Column(columnDefinition = "JSON")
    private String examDateInfo;
    private String classroomId;
    private String classId;

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamDateInfo() {
        return examDateInfo;
    }

    public void setExamDateInfo(String examDateInfo) {
        this.examDateInfo = examDateInfo;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}