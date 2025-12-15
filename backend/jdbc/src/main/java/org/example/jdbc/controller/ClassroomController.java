package org.example.jdbc.controller;

import org.example.jdbc.entity.Classroom;
import org.example.jdbc.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping
    public ResponseEntity<List<Classroom>> getAllClassrooms() {
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        return new ResponseEntity<>(classrooms, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classroom> getClassroomById(@PathVariable("id") String classroomId) {
        Classroom classroom = classroomService.getClassroomById(classroomId);
        if (classroom == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(classroom, HttpStatus.OK);
    }

    @GetMapping("/building/{building}")
    public ResponseEntity<List<Classroom>> getClassroomsByBuilding(@PathVariable("building") String building) {
        List<Classroom> classrooms = classroomService.getClassroomsByBuilding(building);
        return new ResponseEntity<>(classrooms, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addClassroom(@RequestBody Classroom classroom) {
        boolean success = classroomService.addClassroom(classroom);
        if (success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateClassroom(@PathVariable("id") String classroomId, @RequestBody Classroom classroom) {
        classroom.setClassroomId(classroomId);
        boolean success = classroomService.updateClassroom(classroom);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable("id") String classroomId) {
        boolean success = classroomService.deleteClassroom(classroomId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}