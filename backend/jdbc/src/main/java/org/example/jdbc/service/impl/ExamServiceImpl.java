package org.example.jdbc.service.impl;

import org.example.jdbc.entity.Exam;
import org.example.jdbc.repository.ExamRepository;
import org.example.jdbc.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public List<Exam> getAllExams() {
        return (List<Exam>) examRepository.findAll();
    }

    @Override
    public Exam getExamById(String examId) {
        return examRepository.findById(examId).orElse(null);
    }

    @Override
    public List<Exam> getExamsByClass(String classId) {
        return examRepository.findByClassId(classId);
    }

    @Override
    public List<Exam> getExamsByClassroom(String classroomId) {
        return examRepository.findByClassroomId(classroomId);
    }

    @Override
    public boolean addExam(Exam exam) {
        try {
            examRepository.save(exam);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateExam(Exam exam) {
        try {
            if (examRepository.existsById(exam.getExamId())) {
                examRepository.save(exam);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteExam(String examId) {
        try {
            if (examRepository.existsById(examId)) {
                examRepository.deleteById(examId);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}