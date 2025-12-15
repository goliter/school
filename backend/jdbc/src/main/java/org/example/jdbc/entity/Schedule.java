package org.example.jdbc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "schedule")
@IdClass(ScheduleId.class)
public class Schedule {
    @Id
    @Column(name = "class_id")
    private String classId;
    @Id
    @Column(name = "classroom_id")
    private String classroomId;
    @Id
    @Column(name = "week")
    private Integer week;
    @Id
    @Column(name = "weekday")
    private Integer weekday;
    @Id
    @Column(name = "periods")
    private Integer periods;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }
}