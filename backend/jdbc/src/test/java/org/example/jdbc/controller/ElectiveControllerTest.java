package org.example.jdbc.controller;

import org.example.jdbc.entity.Elective;
import org.example.jdbc.service.ElectiveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ElectiveControllerTest {

    @Autowired
    private ElectiveService electiveService;

    @Test
    void testAddAndGetElective() {
        // 创建测试选课记录
        Elective elective = new Elective();
        elective.setStudentId("STUDENT_TEST");
        elective.setClassId("CLASS_TEST");
        elective.setEvaluation("优秀");

        // 测试添加选课
        boolean addResult = electiveService.addElective(elective);
        assertTrue(addResult, "选课添加失败");

        // 测试获取选课
        Elective retrievedElective = electiveService.getElective("STUDENT_TEST", "CLASS_TEST");
        assertNotNull(retrievedElective, "选课查询失败");
        assertEquals("STUDENT_TEST", retrievedElective.getStudentId(), "学生ID不匹配");
        assertEquals("CLASS_TEST", retrievedElective.getClassId(), "班级ID不匹配");
        assertEquals("优秀", retrievedElective.getEvaluation(), "评价不匹配");
    }

    @Test
    void testGetElectivesByStudentId() {
        // 确保测试选课记录存在
        Elective elective = new Elective();
        elective.setStudentId("STUDENT_LIST_TEST");
        elective.setClassId("CLASS_LIST_TEST");
        elective.setEvaluation("良好");
        electiveService.addElective(elective);

        // 测试按学生ID获取选课列表
        List<Elective> electives = electiveService.getElectivesByStudentId("STUDENT_LIST_TEST");
        assertNotNull(electives, "按学生ID查询选课失败");
        assertTrue(electives.stream().anyMatch(e -> "STUDENT_LIST_TEST".equals(e.getStudentId())), 
                   "未找到添加的测试选课记录");
    }
}
