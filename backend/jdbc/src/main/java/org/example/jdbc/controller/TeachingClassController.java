package org.example.jdbc.controller;

import org.example.jdbc.entity.TeachingClass;
import org.example.jdbc.service.TeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teaching-classes")
public class TeachingClassController {

    @Autowired
    private TeachingClassService teachingClassService;

    @GetMapping
    public ResponseEntity<List<TeachingClass>> getAllTeachingClasses() {
        List<TeachingClass> teachingClasses = teachingClassService.getAllTeachingClasses();
        return new ResponseEntity<>(teachingClasses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeachingClass> getTeachingClassById(@PathVariable("id") String classId) {
        TeachingClass teachingClass = teachingClassService.getTeachingClassById(classId);
        if (teachingClass == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teachingClass, HttpStatus.OK);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<TeachingClass>> getTeachingClassesByCourse(@PathVariable("courseId") String courseId) {
        List<TeachingClass> teachingClasses = teachingClassService.getTeachingClassesByCourse(courseId);
        return new ResponseEntity<>(teachingClasses, HttpStatus.OK);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TeachingClass>> getTeachingClassesByTeacher(@PathVariable("teacherId") String teacherId) {
        List<TeachingClass> teachingClasses = teachingClassService.getTeachingClassesByTeacher(teacherId);
        return new ResponseEntity<>(teachingClasses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addTeachingClass(@RequestBody TeachingClass teachingClass) {
        boolean success = teachingClassService.addTeachingClass(teachingClass);
        if (success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTeachingClass(@PathVariable("id") String classId, @RequestBody TeachingClass teachingClass) {
        teachingClass.setClassId(classId);
        boolean success = teachingClassService.updateTeachingClass(teachingClass);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeachingClass(@PathVariable("id") String classId) {
        boolean success = teachingClassService.deleteTeachingClass(classId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}