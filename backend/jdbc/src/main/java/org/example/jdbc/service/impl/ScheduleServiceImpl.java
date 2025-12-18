package org.example.jdbc.service.impl;

import org.example.jdbc.dto.ScheduleDTO;
import org.example.jdbc.entity.Course;
import org.example.jdbc.entity.Classroom;
import org.example.jdbc.entity.Schedule;
import org.example.jdbc.entity.Teacher;
import org.example.jdbc.entity.TeachingClass;
import org.example.jdbc.entity.Elective;
import org.example.jdbc.repository.CourseRepository;
import org.example.jdbc.repository.ClassroomRepository;
import org.example.jdbc.repository.ScheduleRepository;
import org.example.jdbc.repository.TeacherRepository;
import org.example.jdbc.repository.TeachingClassRepository;
import org.example.jdbc.repository.ElectiveRepository;
import org.example.jdbc.service.ConflictCheckService;
import org.example.jdbc.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ConflictCheckService conflictCheckService;
    private final TeachingClassRepository teachingClassRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;
    private final ElectiveRepository electiveRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, ConflictCheckService conflictCheckService, 
                             TeachingClassRepository teachingClassRepository, CourseRepository courseRepository, 
                             TeacherRepository teacherRepository, ClassroomRepository classroomRepository, 
                             ElectiveRepository electiveRepository) {
        this.scheduleRepository = scheduleRepository;
        this.conflictCheckService = conflictCheckService;
        this.teachingClassRepository = teachingClassRepository;
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.classroomRepository = classroomRepository;
        this.electiveRepository = electiveRepository;
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return (List<Schedule>) scheduleRepository.findAll();
    }

    @Override
    public List<Schedule> getSchedulesByClass(String classId) {
        return scheduleRepository.findByClassId(classId);
    }

    @Override
    public List<Schedule> getSchedulesByClassroom(String classroomId) {
        return scheduleRepository.findByClassroomId(classroomId);
    }

    @Override
    public boolean addSchedule(Schedule schedule) {
        try {
            // 检查是否存在冲突
            if (conflictCheckService.isScheduleConflict(schedule)) {
                return false; // 存在冲突，添加失败
            }
            scheduleRepository.save(schedule);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateSchedule(Schedule schedule) {
        try {
            if (schedule.getId() != null && scheduleRepository.existsById(schedule.getId())) {
                // 检查是否存在冲突
                if (conflictCheckService.isScheduleConflict(schedule)) {
                    return false; // 存在冲突，更新失败
                }
                scheduleRepository.save(schedule);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteSchedule(Long id) {
        try {
            if (scheduleRepository.existsById(id)) {
                scheduleRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<ScheduleDTO> getSchedulesByTeacher(String teacherId) {
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        // 获取该教师的所有教学班
        List<TeachingClass> teachingClasses = teachingClassRepository.findByTeacherId(teacherId);
        
        // 对于每个教学班，获取对应的课程安排
        for (TeachingClass teachingClass : teachingClasses) {
            String classId = teachingClass.getClassId();
            
            // 获取该教学班的所有课程安排
            List<Schedule> classSchedules = scheduleRepository.findByClassId(classId);
            
            // 获取课程信息
            String courseId = teachingClass.getCourseId();
            Course course = courseRepository.findById(courseId).orElse(null);
            
            // 获取教师信息
            Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
            
            for (Schedule schedule : classSchedules) {
                ScheduleDTO dto = new ScheduleDTO();
                // 设置基础信息
                dto.setId(schedule.getId());
                dto.setClassId(schedule.getClassId());
                dto.setClassroomId(schedule.getClassroomId());
                dto.setScheduleInfo(schedule.getScheduleInfo());
                
                // 设置课程信息
                dto.setCourseId(courseId);
                if (course != null) {
                    dto.setCourseName(course.getCourseName());
                }
                
                // 设置班级信息
                dto.setClassName(teachingClass.getClassName());
                
                // 设置教师信息
                dto.setTeacherId(teacherId);
                if (teacher != null) {
                    dto.setTeacherName(teacher.getName());
                }
                
                // 设置教室信息
                String classroomId = schedule.getClassroomId();
                Classroom classroom = classroomRepository.findById(classroomId).orElse(null);
                if (classroom != null) {
                    dto.setClassroomName(classroom.getBuilding() + classroom.getClassroomId());
                }
                
                scheduleDTOs.add(dto);
            }
        }
        return scheduleDTOs;
    }

    @Override
    public List<ScheduleDTO> getSchedulesByStudent(String studentId) {
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        // 获取该学生选修的所有教学班
        List<Elective> electives = electiveRepository.findByStudentId(studentId);
        // 对于每个选修记录，获取对应的教学班
        for (Elective elective : electives) {
            String classId = elective.getClassId();
            TeachingClass teachingClass = teachingClassRepository.findById(classId).orElse(null);
            if (teachingClass == null) {
                continue;
            }
            
            // 获取该教学班的所有课程安排
            List<Schedule> classSchedules = scheduleRepository.findByClassId(classId);
            
            // 获取课程信息
            String courseId = teachingClass.getCourseId();
            Course course = courseRepository.findById(courseId).orElse(null);
            
            // 获取教师信息
            String teacherId = teachingClass.getTeacherId();
            Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
            
            for (Schedule schedule : classSchedules) {
                ScheduleDTO dto = new ScheduleDTO();
                // 设置基础信息
                dto.setId(schedule.getId());
                dto.setClassId(schedule.getClassId());
                dto.setClassroomId(schedule.getClassroomId());
                dto.setScheduleInfo(schedule.getScheduleInfo());
                
                // 设置课程信息
                dto.setCourseId(courseId);
                if (course != null) {
                    dto.setCourseName(course.getCourseName());
                }
                
                // 设置班级信息
                dto.setClassName(teachingClass.getClassName());
                
                // 设置教师信息
                dto.setTeacherId(teacherId);
                if (teacher != null) {
                    dto.setTeacherName(teacher.getName());
                }
                
                // 设置教室信息
                String classroomId = schedule.getClassroomId();
                Classroom classroom = classroomRepository.findById(classroomId).orElse(null);
                if (classroom != null) {
                    dto.setClassroomName(classroom.getBuilding() + classroom.getClassroomId());
                }
                
                scheduleDTOs.add(dto);
            }
        }
        return scheduleDTOs;
    }
}