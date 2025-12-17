package org.example.jdbc.controller;

import org.example.jdbc.entity.ApiResponse;
import org.example.jdbc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeachingClassService teachingClassService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private MajorService majorService;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private ExamService examService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 统计总用户数（学生+教师+管理员）
        long totalUsers = userAccountService.getAllUserAccounts().size();
        stats.put("totalUsers", totalUsers);
        
        // 统计学生数量
        long totalStudents = studentService.getAllStudents().size();
        stats.put("totalStudents", totalStudents);
        
        // 统计教师数量
        long totalTeachers = teacherService.getAllTeachers().size();
        stats.put("totalTeachers", totalTeachers);
        
        // 统计管理员数量
        long totalAdmins = userAccountService.getAllUserAccounts()
                .stream()
                .filter(account -> "admin".equals(account.getRole()))
                .count();
        stats.put("totalAdmins", totalAdmins);
        
        // 统计学院数量
        long totalColleges = collegeService.getAllColleges().size();
        stats.put("totalColleges", totalColleges);
        
        // 统计专业数量
        long totalMajors = majorService.getAllMajors().size();
        stats.put("totalMajors", totalMajors);
        
        // 统计课程数量
        long totalCourses = courseService.getAllCourses().size();
        stats.put("totalCourses", totalCourses);
        
        // 统计教学班数量
        long totalTeachingClasses = teachingClassService.getAllTeachingClasses().size();
        stats.put("totalTeachingClasses", totalTeachingClasses);
        
        // 统计教室数量
        long totalClassrooms = classroomService.getAllClassrooms().size();
        stats.put("totalClassrooms", totalClassrooms);
        
        // 统计考试数量
        long totalExams = examService.getAllExams().size();
        stats.put("totalExams", totalExams);
        
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
