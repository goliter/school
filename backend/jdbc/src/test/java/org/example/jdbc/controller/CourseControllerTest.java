package org.example.jdbc.controller;

import org.example.jdbc.entity.Course;
import org.example.jdbc.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseControllerTest {

    @Autowired
    private CourseService courseService;

    @Test
    void testAddAndGetCourse() {
        // 创建测试课程
        Course course = new Course();
        course.setCourseId("COURSE_TEST");
        course.setCourseName("简单测试课程");
        course.setCredit(3.0);
        course.setMajorCode("TEST_MAJOR");
        course.setCourseType("专业必修课");

        // 测试添加课程
        boolean addResult = courseService.addCourse(course);
        assertTrue(addResult, "课程添加失败");

        // 测试获取课程
        Course retrievedCourse = courseService.getCourseById("COURSE_TEST");
        assertNotNull(retrievedCourse, "课程查询失败");
        assertEquals("COURSE_TEST", retrievedCourse.getCourseId(), "课程ID不匹配");
        assertEquals("简单测试课程", retrievedCourse.getCourseName(), "课程名称不匹配");
    }

    @Test
    void testGetCoursesByMajor() {
        // 确保测试课程存在
        Course course = new Course();
        course.setCourseId("COURSE_MAJOR_TEST");
        course.setCourseName("按专业测试课程");
        course.setCredit(2.0);
        course.setMajorCode("TEST_MAJOR_2");
        course.setCourseType("专业选修课");
        courseService.addCourse(course);

        // 测试按专业代码获取课程列表
        List<Course> courses = courseService.getCoursesByMajor("TEST_MAJOR_2");
        assertNotNull(courses, "按专业查询课程失败");
        assertTrue(courses.stream().anyMatch(c -> "COURSE_MAJOR_TEST".equals(c.getCourseId())), 
                   "未找到添加的测试课程");
    }
}
