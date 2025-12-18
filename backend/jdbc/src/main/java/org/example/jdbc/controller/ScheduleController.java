package org.example.jdbc.controller;

import org.example.jdbc.dto.ScheduleDTO;
import org.example.jdbc.entity.ApiResponse;
import org.example.jdbc.entity.Schedule;
import org.example.jdbc.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Schedule>>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<ApiResponse<List<Schedule>>> getSchedulesByClass(@PathVariable("classId") String classId) {
        List<Schedule> schedules = scheduleService.getSchedulesByClass(classId);
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<ApiResponse<List<Schedule>>> getSchedulesByClassroom(@PathVariable("classroomId") String classroomId) {
        List<Schedule> schedules = scheduleService.getSchedulesByClassroom(classroomId);
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Schedule>> addSchedule(@RequestBody Schedule schedule) {
        boolean success = scheduleService.addSchedule(schedule);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("课程表添加成功", schedule));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "课程表添加失败"));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Schedule>> updateSchedule(@RequestBody Schedule schedule) {
        boolean success = scheduleService.updateSchedule(schedule);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("课程表更新成功", schedule));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "课程表更新失败"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(@PathVariable Long id) {
        boolean success = scheduleService.deleteSchedule(id);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("课程表删除成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "课程表删除失败"));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<List<ScheduleDTO>>> getSchedulesByTeacher(@PathVariable("teacherId") String teacherId) {
        List<ScheduleDTO> schedules = scheduleService.getSchedulesByTeacher(teacherId);
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<ScheduleDTO>>> getSchedulesByStudent(@PathVariable("studentId") String studentId) {
        List<ScheduleDTO> schedules = scheduleService.getSchedulesByStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }
}