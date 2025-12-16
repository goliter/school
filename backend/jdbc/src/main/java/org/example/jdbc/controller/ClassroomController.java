package org.example.jdbc.controller;

import org.example.jdbc.entity.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<Classroom>>> getAllClassrooms() {
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        return ResponseEntity.ok(ApiResponse.success(classrooms));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Classroom>> getClassroomById(@PathVariable("id") String classroomId) {
        Classroom classroom = classroomService.getClassroomById(classroomId);
        if (classroom == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "教室不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(classroom));
    }

    @GetMapping("/building/{building}")
    public ResponseEntity<ApiResponse<List<Classroom>>> getClassroomsByBuilding(@PathVariable("building") String building) {
        List<Classroom> classrooms = classroomService.getClassroomsByBuilding(building);
        return ResponseEntity.ok(ApiResponse.success(classrooms));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Classroom>> addClassroom(@RequestBody Classroom classroom) {
        boolean success = classroomService.addClassroom(classroom);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("教室添加成功", classroom));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "教室添加失败"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Classroom>> updateClassroom(@PathVariable("id") String classroomId, @RequestBody Classroom classroom) {
        classroom.setClassroomId(classroomId);
        boolean success = classroomService.updateClassroom(classroom);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("教室更新成功", classroom));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "教室更新失败"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClassroom(@PathVariable("id") String classroomId) {
        boolean success = classroomService.deleteClassroom(classroomId);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("教室删除成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "教室删除失败"));
    }
}