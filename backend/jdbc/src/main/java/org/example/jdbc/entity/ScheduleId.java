package org.example.jdbc.entity;

import java.io.Serializable;

public class ScheduleId implements Serializable {
    private String classId;
    private String classroomId;
    private Integer week;
    private Integer weekday;
    private Integer periods;

    public ScheduleId() {
    }

    public ScheduleId(String classId, String classroomId, Integer week, Integer weekday, Integer periods) {
        this.classId = classId;
        this.classroomId = classroomId;
        this.week = week;
        this.weekday = weekday;
        this.periods = periods;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduleId that = (ScheduleId) o;

        if (!classId.equals(that.classId)) return false;
        if (!classroomId.equals(that.classroomId)) return false;
        if (!week.equals(that.week)) return false;
        if (!weekday.equals(that.weekday)) return false;
        return periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        int result = classId.hashCode();
        result = 31 * result + classroomId.hashCode();
        result = 31 * result + week.hashCode();
        result = 31 * result + weekday.hashCode();
        result = 31 * result + periods.hashCode();
        return result;
    }
}