package org.example.jdbc.service;

import org.example.jdbc.entity.College;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CollegeServiceTest {

    @Autowired
    private CollegeService collegeService;

    @Test
    void testAddAndGetCollege() {
        // 创建测试学院
        College college = new College();
        college.setCollegeId("TEST001");
        college.setCollegeName("测试学院");
        college.setAddress("测试地址");
        college.setPhone("1234567890");

        // 添加学院
        boolean addResult = collegeService.addCollege(college);
        assertTrue(addResult, "学院添加失败");

        // 获取学院
        College retrievedCollege = collegeService.getCollegeById("TEST001");
        assertNotNull(retrievedCollege, "学院查询失败");
        assertEquals("测试学院", retrievedCollege.getCollegeName(), "学院名称不匹配");
    }

    @Test
    void testUpdateCollege() {
        // 更新学院信息
        College college = new College();
        college.setCollegeId("TEST001");
        college.setCollegeName("更新后的测试学院");
        college.setAddress("更新后的测试地址");
        college.setPhone("0987654321");

        boolean updateResult = collegeService.updateCollege(college);
        assertTrue(updateResult, "学院更新失败");

        // 验证更新
        College updatedCollege = collegeService.getCollegeById("TEST001");
        assertEquals("更新后的测试学院", updatedCollege.getCollegeName(), "学院名称更新失败");
    }

    @Test
    void testGetAllColleges() {
        List<College> colleges = collegeService.getAllColleges();
        assertNotNull(colleges, "学院列表查询失败");
        // 至少包含我们添加的测试学院
        assertTrue(colleges.size() > 0, "学院列表为空");
    }

    @Test
    void testDeleteCollege() {
        boolean deleteResult = collegeService.deleteCollege("TEST001");
        assertTrue(deleteResult, "学院删除失败");

        // 验证删除
        College deletedCollege = collegeService.getCollegeById("TEST001");
        assertNull(deletedCollege, "学院删除失败");
    }
}