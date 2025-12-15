package org.example.jdbc.service;

import org.example.jdbc.entity.Classroom;
import java.util.List;

public interface ClassroomService {
    List<Classroom> getAllClassrooms();
    Classroom getClassroomById(String classroomId);
    List<Classroom> getClassroomsByBuilding(String building);
    boolean addClassroom(Classroom classroom);
    boolean updateClassroom(Classroom classroom);
    boolean deleteClassroom(String classroomId);
}