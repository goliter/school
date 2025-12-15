package org.example.jdbc.service;

import org.example.jdbc.entity.Classroom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ClassroomServiceTest {

    @Autowired
    private ClassroomService classroomService;

    @Test
    void testAddAndGetClassroom() {
        // 创建测试教室
        Classroom classroom = new Classroom();
        classroom.setClassroomId("ROOM101");
        classroom.setBuilding("教学大楼A座");
        classroom.setNature("普通教室");
        classroom.setCapacity(50);

        // 添加教室
        boolean addResult = classroomService.addClassroom(classroom);
        assertTrue(addResult, "教室添加失败");

        // 获取教室
        Classroom retrievedClassroom = classroomService.getClassroomById("ROOM101");
        assertNotNull(retrievedClassroom, "教室查询失败");
        assertEquals("教学大楼A座", retrievedClassroom.getBuilding(), "教学楼不匹配");
        assertEquals(50, retrievedClassroom.getCapacity(), "教室容量不匹配");
    }

    @Test
    void testGetClassroomsByBuilding() {
        // 创建独立的测试教室用于查询
        Classroom testClassroom = new Classroom();
        testClassroom.setClassroomId("TESTROOM1");
        testClassroom.setBuilding("测试教学楼");
        testClassroom.setNature("普通教室");
        testClassroom.setCapacity(40);
        
        // 确保测试教室存在
        classroomService.addClassroom(testClassroom);
        
        // 按教学楼查询教室
        List<Classroom> classrooms = classroomService.getClassroomsByBuilding("测试教学楼");
        assertNotNull(classrooms, "按教学楼查询教室失败");
        
        // 验证查询结果包含我们添加的测试教室
        boolean found = classrooms.stream().anyMatch(c -> "TESTROOM1".equals(c.getClassroomId()));
        assertTrue(found, "未找到添加的测试教室");
    }

    @Test
    void testUpdateClassroom() {
        // 更新教室信息
        Classroom classroom = new Classroom();
        classroom.setClassroomId("ROOM101");
        classroom.setBuilding("教学大楼B座");
        classroom.setNature("多媒体教室");
        classroom.setCapacity(60);

        boolean updateResult = classroomService.updateClassroom(classroom);
        assertTrue(updateResult, "教室更新失败");

        // 验证更新
        Classroom updatedClassroom = classroomService.getClassroomById("ROOM101");
        assertEquals("教学大楼B座", updatedClassroom.getBuilding(), "教学楼更新失败");
        assertEquals("多媒体教室", updatedClassroom.getNature(), "教室性质更新失败");
        assertEquals(60, updatedClassroom.getCapacity(), "教室容量更新失败");
    }
}