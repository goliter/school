package org.example.jdbc.service.impl;

import org.example.jdbc.dto.TeachingClassDTO;
import org.example.jdbc.entity.Course;
import org.example.jdbc.entity.Schedule;
import org.example.jdbc.entity.TeachingClass;
import org.example.jdbc.repository.*;
import org.example.jdbc.service.TeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeachingClassServiceImpl implements TeachingClassService {
    private final TeachingClassRepository teachingClassRepository;
    private final CourseRepository courseRepository;
    private final ScheduleRepository scheduleRepository;
    private final ElectiveRepository electiveRepository;

    @Autowired
    public TeachingClassServiceImpl(TeachingClassRepository teachingClassRepository, 
                                   CourseRepository courseRepository, 
                                   ScheduleRepository scheduleRepository, 
                                   ElectiveRepository electiveRepository) {
        this.teachingClassRepository = teachingClassRepository;
        this.courseRepository = courseRepository;
        this.scheduleRepository = scheduleRepository;
        this.electiveRepository = electiveRepository;
    }

    @Override
    public List<TeachingClass> getAllTeachingClasses() {
        return (List<TeachingClass>) teachingClassRepository.findAll();
    }

    @Override
    public TeachingClass getTeachingClassById(String classId) {
        return teachingClassRepository.findById(classId).orElse(null);
    }

    @Override
    public List<TeachingClass> getTeachingClassesByCourse(String courseId) {
        return teachingClassRepository.findByCourseId(courseId);
    }

    @Override
    public List<TeachingClassDTO> getTeachingClassesByTeacher(String teacherId) {
        List<TeachingClass> teachingClasses = teachingClassRepository.findByTeacherId(teacherId);
        return teachingClasses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 将TeachingClass实体转换为TeachingClassDTO
    private TeachingClassDTO convertToDTO(TeachingClass teachingClass) {
        TeachingClassDTO dto = new TeachingClassDTO();
        dto.setClassId(teachingClass.getClassId());
        dto.setCourseId(teachingClass.getCourseId());
        dto.setTeacherId(teachingClass.getTeacherId());
        dto.setClassName(teachingClass.getClassName());
        dto.setSemester(teachingClass.getSemester());

        // 获取课程名称
        Course course = courseRepository.findById(teachingClass.getCourseId()).orElse(null);
        if (course != null) {
            dto.setCourseName(course.getCourseName());
        }

        // 获取课表信息
        Schedule schedule = scheduleRepository.findFirstByClassId(teachingClass.getClassId());
        if (schedule != null) {
            dto.setScheduleInfo(schedule.getScheduleInfo());
        }

        // 获取学生人数
        long studentCount = electiveRepository.countByClassId(teachingClass.getClassId());
        dto.setStudentCount((int) studentCount);

        return dto;
    }

    @Override
    public boolean addTeachingClass(TeachingClass teachingClass) {
        try {
            teachingClassRepository.save(teachingClass);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateTeachingClass(TeachingClass teachingClass) {
        try {
            if (teachingClassRepository.existsById(teachingClass.getClassId())) {
                teachingClassRepository.save(teachingClass);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteTeachingClass(String classId) {
        try {
            if (teachingClassRepository.existsById(classId)) {
                teachingClassRepository.deleteById(classId);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}