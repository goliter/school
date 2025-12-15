package org.example.jdbc.controller;

import org.example.jdbc.entity.Teacher;
import org.example.jdbc.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable("id") String teacherId) {
        Teacher teacher = teacherService.getTeacherById(teacherId);
        if (teacher == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teacher, HttpStatus.OK);
    }

    @GetMapping("/major/{majorCode}")
    public ResponseEntity<List<Teacher>> getTeachersByMajor(@PathVariable("majorCode") String majorCode) {
        List<Teacher> teachers = teacherService.getTeachersByMajor(majorCode);
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addTeacher(@RequestBody Teacher teacher) {
        boolean success = teacherService.addTeacher(teacher);
        if (success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTeacher(@PathVariable("id") String teacherId, @RequestBody Teacher teacher) {
        teacher.setTeacherId(teacherId);
        boolean success = teacherService.updateTeacher(teacher);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable("id") String teacherId) {
        boolean success = teacherService.deleteTeacher(teacherId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}