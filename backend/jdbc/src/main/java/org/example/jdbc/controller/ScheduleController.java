package org.example.jdbc.controller;

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
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Schedule>> getSchedulesByClass(@PathVariable("classId") String classId) {
        List<Schedule> schedules = scheduleService.getSchedulesByClass(classId);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<Schedule>> getSchedulesByClassroom(@PathVariable("classroomId") String classroomId) {
        List<Schedule> schedules = scheduleService.getSchedulesByClassroom(classroomId);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addSchedule(@RequestBody Schedule schedule) {
        boolean success = scheduleService.addSchedule(schedule);
        if (success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity<Void> updateSchedule(@RequestBody Schedule schedule) {
        boolean success = scheduleService.updateSchedule(schedule);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(@RequestParam String classId, @RequestParam int week, 
                                              @RequestParam int weekday, @RequestParam int periods) {
        boolean success = scheduleService.deleteSchedule(classId, week, weekday, periods);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}