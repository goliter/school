package org.example.jdbc.controller;

import org.example.jdbc.entity.College;
import org.example.jdbc.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colleges")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @GetMapping
    public ResponseEntity<List<College>> getAllColleges() {
        List<College> colleges = collegeService.getAllColleges();
        return new ResponseEntity<>(colleges, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<College> getCollegeById(@PathVariable("id") String collegeId) {
        College college = collegeService.getCollegeById(collegeId);
        if (college == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(college, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addCollege(@RequestBody College college) {
        boolean success = collegeService.addCollege(college);
        if (success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCollege(@PathVariable("id") String collegeId, @RequestBody College college) {
        college.setCollegeId(collegeId);
        boolean success = collegeService.updateCollege(college);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollege(@PathVariable("id") String collegeId) {
        boolean success = collegeService.deleteCollege(collegeId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}