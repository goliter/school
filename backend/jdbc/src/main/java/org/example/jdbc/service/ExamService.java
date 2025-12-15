package org.example.jdbc.service;

import org.example.jdbc.entity.Exam;
import java.util.List;

public interface ExamService {
    List<Exam> getAllExams();
    Exam getExamById(String examId);
    List<Exam> getExamsByClass(String classId);
    List<Exam> getExamsByClassroom(String classroomId);
    boolean addExam(Exam exam);
    boolean updateExam(Exam exam);
    boolean deleteExam(String examId);
}