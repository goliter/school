package org.example.jdbc.repository;

import org.example.jdbc.entity.Score;
import org.example.jdbc.entity.ScoreId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreRepository extends CrudRepository<Score, ScoreId> {
    List<Score> findByStudentId(String studentId);
    
    @Query("SELECT s FROM Score s WHERE s.examId IN (SELECT e.examId FROM Exam e WHERE e.classId IN (SELECT tc.classId FROM TeachingClass tc WHERE tc.courseId = :courseId))")
    List<Score> findByCourseId(@Param("courseId") String courseId);
    
    @Query("SELECT s FROM Score s WHERE s.studentId = :studentId AND s.examId IN (SELECT e.examId FROM Exam e WHERE e.classId IN (SELECT tc.classId FROM TeachingClass tc WHERE tc.courseId = :courseId))")
    Score findByStudentIdAndCourseId(@Param("studentId") String studentId, @Param("courseId") String courseId);
}