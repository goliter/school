package org.example.jdbc.service;

import org.example.jdbc.entity.Elective;
import java.util.List;

public interface ElectiveService {
    List<Elective> getAllElectives();
    List<Elective> getElectivesByStudentId(String studentId);
    List<Elective> getElectivesByClassId(String classId);
    Elective getElective(String studentId, String classId);
    boolean addElective(Elective elective);
    boolean updateElective(Elective elective);
    boolean deleteElective(String studentId, String classId);
}