package org.example.jdbc.controller;

import org.example.jdbc.entity.Major;
import org.example.jdbc.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
public class MajorController {

    @Autowired
    private MajorService majorService;

    @GetMapping
    public ResponseEntity<List<Major>> getAllMajors() {
        List<Major> majors = majorService.getAllMajors();
        return new ResponseEntity<>(majors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Major> getMajorById(@PathVariable("id") String majorCode) {
        Major major = majorService.getMajorById(majorCode);
        if (major == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(major, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addMajor(@RequestBody Major major) {
        boolean success = majorService.addMajor(major);
        if (success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMajor(@PathVariable("id") String majorCode, @RequestBody Major major) {
        major.setMajorCode(majorCode);
        boolean success = majorService.updateMajor(major);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMajor(@PathVariable("id") String majorCode) {
        boolean success = majorService.deleteMajor(majorCode);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}