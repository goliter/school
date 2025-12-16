package org.example.jdbc.controller;

import org.example.jdbc.entity.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<Major>>> getAllMajors() {
        List<Major> majors = majorService.getAllMajors();
        return ResponseEntity.ok(ApiResponse.success(majors));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Major>> getMajorById(@PathVariable("id") String majorCode) {
        Major major = majorService.getMajorById(majorCode);
        if (major == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "专业不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(major));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Major>> addMajor(@RequestBody Major major) {
        boolean success = majorService.addMajor(major);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("专业添加成功", major));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "专业添加失败"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Major>> updateMajor(@PathVariable("id") String majorCode, @RequestBody Major major) {
        major.setMajorCode(majorCode);
        boolean success = majorService.updateMajor(major);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("专业更新成功", major));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "专业更新失败"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMajor(@PathVariable("id") String majorCode) {
        boolean success = majorService.deleteMajor(majorCode);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("专业删除成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "专业删除失败"));
    }
}