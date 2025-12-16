package org.example.jdbc.controller;

import org.example.jdbc.entity.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<Elective>>> getAllElectives() {
        List<Elective> electives = electiveService.getAllElectives();
        return ResponseEntity.ok(ApiResponse.success(electives));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Elective>>> getElectivesByStudentId(@PathVariable String studentId) {
        List<Elective> electives = electiveService.getElectivesByStudentId(studentId);
        return ResponseEntity.ok(ApiResponse.success(electives));
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<ApiResponse<List<Elective>>> getElectivesByClassId(@PathVariable String classId) {
        List<Elective> electives = electiveService.getElectivesByClassId(classId);
        return ResponseEntity.ok(ApiResponse.success(electives));
    }

    @GetMapping("/{studentId}/{classId}")
    public ResponseEntity<ApiResponse<Elective>> getElective(@PathVariable String studentId, @PathVariable String classId) {
        Elective elective = electiveService.getElective(studentId, classId);
        if (elective == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "选课记录不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(elective));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Elective>> addElective(@RequestBody Elective elective) {
        boolean result = electiveService.addElective(elective);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("选课记录添加成功", elective));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "选课记录添加失败"));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Elective>> updateElective(@RequestBody Elective elective) {
        boolean result = electiveService.updateElective(elective);
        if (result) {
            return ResponseEntity.ok(ApiResponse.success("选课记录更新成功", elective));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "选课记录更新失败"));
    }

    @DeleteMapping("/{studentId}/{classId}")
    public ResponseEntity<ApiResponse<Void>> deleteElective(@PathVariable String studentId, @PathVariable String classId) {
        boolean result = electiveService.deleteElective(studentId, classId);
        if (result) {
            return ResponseEntity.ok(ApiResponse.success("选课记录删除成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "选课记录删除失败"));
    }
}