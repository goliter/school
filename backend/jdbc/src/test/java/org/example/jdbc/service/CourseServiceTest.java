package org.example.jdbc.service;

import org.example.jdbc.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Test
    void testAddAndGetCourse() {
        // 创建测试课程
        Course course = new Course();
        course.setCourseId("CS101");
        course.setCourseName("计算机科学导论");
        course.setCredit(3.0);
        course.setCourseType("必修课");
        course.setMajorCode("CS");

        // 添加课程
        boolean addResult = courseService.addCourse(course);
        assertTrue(addResult, "课程添加失败");

        // 获取课程
        Course retrievedCourse = courseService.getCourseById("CS101");
        assertNotNull(retrievedCourse, "课程查询失败");
        assertEquals("计算机科学导论", retrievedCourse.getCourseName(), "课程名称不匹配");
    }

    @Test
    void testUpdateCourse() {
        // 更新课程信息
        Course course = new Course();
        course.setCourseId("CS101");
        course.setCourseName("更新后的计算机科学导论");
        course.setCredit(4.0);
        course.setCourseType("核心课");
        course.setMajorCode("CS");

        boolean updateResult = courseService.updateCourse(course);
        assertTrue(updateResult, "课程更新失败");

        // 验证更新
        Course updatedCourse = courseService.getCourseById("CS101");
        assertEquals("更新后的计算机科学导论", updatedCourse.getCourseName(), "课程名称更新失败");
        assertEquals(4.0, updatedCourse.getCredit(), "学分更新失败");
    }

    @Test
    void testGetCoursesByMajor() {
        // 按专业查询课程
        List<Course> courses = courseService.getCoursesByMajor("CS");
        assertNotNull(courses, "按专业查询课程失败");
        // 至少包含我们添加的测试课程
        assertTrue(courses.size() > 0, "该专业下没有课程");
    }

    @Test
    void testGetAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        assertNotNull(courses, "课程列表查询失败");
        assertTrue(courses.size() > 0, "课程列表为空");
    }

    @Test
    void testDeleteCourse() {
        boolean deleteResult = courseService.deleteCourse("CS101");
        assertTrue(deleteResult, "课程删除失败");

        // 验证删除
        Course deletedCourse = courseService.getCourseById("CS101");
        assertNull(deletedCourse, "课程删除失败");
    }
}