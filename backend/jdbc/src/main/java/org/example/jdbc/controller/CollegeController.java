package org.example.jdbc.controller;

import org.example.jdbc.entity.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<College>>> getAllColleges() {
        List<College> colleges = collegeService.getAllColleges();
        return ResponseEntity.ok(ApiResponse.success(colleges));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<College>> getCollegeById(@PathVariable("id") String collegeId) {
        College college = collegeService.getCollegeById(collegeId);
        if (college == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "学院不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(college));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<College>> addCollege(@RequestBody College college) {
        boolean success = collegeService.addCollege(college);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("学院添加成功", college));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "学院添加失败"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<College>> updateCollege(@PathVariable("id") String collegeId, @RequestBody College college) {
        college.setCollegeId(collegeId);
        boolean success = collegeService.updateCollege(college);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("学院更新成功", college));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "学院更新失败"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCollege(@PathVariable("id") String collegeId) {
        boolean success = collegeService.deleteCollege(collegeId);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("学院删除成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "学院删除失败"));
    }
}