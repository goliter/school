package org.example.jdbc.controller;

import org.example.jdbc.entity.Elective;
import org.example.jdbc.service.ElectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/electives")
public class ElectiveController {

    @Autowired
    private ElectiveService electiveService;

    @GetMapping
    public ResponseEntity<List<Elective>> getAllElectives() {
        List<Elective> electives = electiveService.getAllElectives();
        return new ResponseEntity<>(electives, HttpStatus.OK);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Elective>> getElectivesByStudentId(@PathVariable String studentId) {
        List<Elective> electives = electiveService.getElectivesByStudentId(studentId);
        return new ResponseEntity<>(electives, HttpStatus.OK);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Elective>> getElectivesByClassId(@PathVariable String classId) {
        List<Elective> electives = electiveService.getElectivesByClassId(classId);
        return new ResponseEntity<>(electives, HttpStatus.OK);
    }

    @GetMapping("/{studentId}/{classId}")
    public ResponseEntity<Elective> getElective(@PathVariable String studentId, @PathVariable String classId) {
        Elective elective = electiveService.getElective(studentId, classId);
        return new ResponseEntity<>(elective, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Boolean> addElective(@RequestBody Elective elective) {
        boolean result = electiveService.addElective(elective);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Boolean> updateElective(@RequestBody Elective elective) {
        boolean result = electiveService.updateElective(elective);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{studentId}/{classId}")
    public ResponseEntity<Boolean> deleteElective(@PathVariable String studentId, @PathVariable String classId) {
        boolean result = electiveService.deleteElective(studentId, classId);
        return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
    }
}