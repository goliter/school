package org.example.jdbc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "classroom")
public class Classroom {
    @Id
    @Column(name = "classroom_id")
    private String classroomId;
    private String building;
    private String nature;
    private Integer capacity;

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}