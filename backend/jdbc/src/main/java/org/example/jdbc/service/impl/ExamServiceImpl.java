package org.example.jdbc.service.impl;

import org.example.jdbc.entity.Exam;
import org.example.jdbc.entity.TeachingClass;
import org.example.jdbc.repository.ExamRepository;
import org.example.jdbc.repository.TeachingClassRepository;
import org.example.jdbc.service.ConflictCheckService;
import org.example.jdbc.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final ConflictCheckService conflictCheckService;
    private final TeachingClassRepository teachingClassRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository, ConflictCheckService conflictCheckService, TeachingClassRepository teachingClassRepository) {
        this.examRepository = examRepository;
        this.conflictCheckService = conflictCheckService;
        this.teachingClassRepository = teachingClassRepository;
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
            // 检查是否存在冲突
            if (conflictCheckService.isExamConflict(exam)) {
                return false; // 存在冲突，添加失败
            }
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
                // 检查是否存在冲突
                if (conflictCheckService.isExamConflict(exam)) {
                    return false; // 存在冲突，更新失败
                }
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

    @Override
    public List<Exam> getExamsByTeacher(String teacherId) {
        List<TeachingClass> teachingClasses = teachingClassRepository.findByTeacherId(teacherId);
        Set<String> classIds = new HashSet<>();
        for (TeachingClass teachingClass : teachingClasses) {
            classIds.add(teachingClass.getClassId());
        }
        List<Exam> exams = new ArrayList<>();
        for (String classId : classIds) {
            exams.addAll(examRepository.findByClassId(classId));
        }
        return exams;
    }
}