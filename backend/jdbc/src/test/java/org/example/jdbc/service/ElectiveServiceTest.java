package org.example.jdbc.service;

import org.example.jdbc.entity.Elective;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ElectiveServiceTest {

    @Autowired
    private ElectiveService electiveService;

    @Test
    void testAddAndGetElective() {
        // 创建测试选修记录
        Elective elective = new Elective();
        elective.setStudentId("STU001");
        elective.setClassId("CLASS001");
        elective.setEvaluation("优秀");

        // 添加选修记录
        boolean addResult = electiveService.addElective(elective);
        assertTrue(addResult, "选修记录添加失败");

        // 获取选修记录
        Elective retrievedElective = electiveService.getElective("STU001", "CLASS001");
        assertNotNull(retrievedElective, "选修记录查询失败");
        assertEquals("优秀", retrievedElective.getEvaluation(), "评价不匹配");
    }

    @Test
    void testUpdateElective() {
        // 更新选修记录
        Elective elective = new Elective();
        elective.setStudentId("STU001");
        elective.setClassId("CLASS001");
        elective.setEvaluation("良好");

        boolean updateResult = electiveService.updateElective(elective);
        assertTrue(updateResult, "选修记录更新失败");

        // 验证更新
        Elective updatedElective = electiveService.getElective("STU001", "CLASS001");
        assertEquals("良好", updatedElective.getEvaluation(), "评价更新失败");
    }

    @Test
    void testGetElectivesByStudentId() {
        // 按学生ID查询选修记录
        List<Elective> electives = electiveService.getElectivesByStudentId("STU001");
        assertNotNull(electives, "按学生ID查询选修记录失败");
        // 至少包含我们添加的测试记录
        assertTrue(electives.size() > 0, "该学生没有选修记录");
    }

    @Test
    void testGetElectivesByClassId() {
        // 按班级ID查询选修记录
        List<Elective> electives = electiveService.getElectivesByClassId("CLASS001");
        assertNotNull(electives, "按班级ID查询选修记录失败");
        // 至少包含我们添加的测试记录
        assertTrue(electives.size() > 0, "该班级没有选修记录");
    }

    @Test
    void testDeleteElective() {
        boolean deleteResult = electiveService.deleteElective("STU001", "CLASS001");
        assertTrue(deleteResult, "选修记录删除失败");

        // 验证删除
        Elective deletedElective = electiveService.getElective("STU001", "CLASS001");
        assertNull(deletedElective, "选修记录删除失败");
    }
}