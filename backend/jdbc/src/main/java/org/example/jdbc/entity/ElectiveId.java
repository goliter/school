package org.example.jdbc.entity;

import java.io.Serializable;

public class ElectiveId implements Serializable {
    private String studentId;
    private String classId;

    public ElectiveId() {
    }

    public ElectiveId(String studentId, String classId) {
        this.studentId = studentId;
        this.classId = classId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElectiveId that = (ElectiveId) o;

        if (!studentId.equals(that.studentId)) return false;
        return classId.equals(that.classId);
    }

    @Override
    public int hashCode() {
        int result = studentId.hashCode();
        result = 31 * result + classId.hashCode();
        return result;
    }
}