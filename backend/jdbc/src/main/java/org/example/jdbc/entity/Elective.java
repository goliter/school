package org.example.jdbc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "elective")
@IdClass(ElectiveId.class)
public class Elective {
    @Id
    @Column(name = "student_id")
    private String studentId;
    @Id
    @Column(name = "class_id")
    private String classId;
    private String evaluation;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }
}