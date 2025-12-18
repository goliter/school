package org.example.jdbc.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.jdbc.entity.Exam;
import org.example.jdbc.entity.Schedule;
import org.example.jdbc.service.ConflictCheckService;
import org.example.jdbc.service.ExamService;
import org.example.jdbc.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 冲突检查服务实现类
 */
@Service
public class ConflictCheckServiceImpl implements ConflictCheckService {

    private final ExamService examService;
    private final ScheduleService scheduleService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ConflictCheckServiceImpl(@Lazy ExamService examService, @Lazy ScheduleService scheduleService, ObjectMapper objectMapper) {
        this.examService = examService;
        this.scheduleService = scheduleService;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean isExamConflict(Exam exam) {
        return isExamWithExamConflict(exam) || isExamWithScheduleConflict(exam);
    }

    @Override
    public boolean isScheduleConflict(Schedule schedule) {
        return isScheduleWithExamConflict(schedule) || isScheduleWithScheduleConflict(schedule);
    }

    @Override
    public boolean isExamWithExamConflict(Exam exam) {
        try {
            // 解析当前考试的日期信息
            JsonNode examDateNode = objectMapper.readTree(exam.getExamDateInfo());
            String examDate = formatDate(examDateNode);
            String examPeriod = examDateNode.path("period").asText();
            String classroomId = exam.getClassroomId();

            // 获取所有考试
            List<Exam> allExams = examService.getAllExams();

            // 遍历所有考试，检查是否有冲突
            for (Exam existingExam : allExams) {
                // 跳过当前考试
                if (existingExam.getExamId().equals(exam.getExamId())) {
                    continue;
                }

                // 解析现有考试的日期信息
                JsonNode existingExamDateNode = objectMapper.readTree(existingExam.getExamDateInfo());
                String existingExamDate = formatDate(existingExamDateNode);
                String existingExamPeriod = existingExamDateNode.path("period").asText();

                // 检查是否在同一时间
                if (examDate.equals(existingExamDate) && examPeriod.equals(existingExamPeriod)) {
                    return true; // 存在冲突
                }
            }

            return false; // 无冲突
        } catch (IOException e) {
            e.printStackTrace();
            return true; // 解析错误时默认认为有冲突
        }
    }

    @Override
    public boolean isExamWithScheduleConflict(Exam exam) {
        try {
            // 解析当前考试的日期信息
            JsonNode examDateNode = objectMapper.readTree(exam.getExamDateInfo());
            String examDate = formatDate(examDateNode);
            String examPeriod = examDateNode.path("period").asText();
            String classroomId = exam.getClassroomId();

            // 获取所有课表
            List<Schedule> allSchedules = scheduleService.getAllSchedules();

            // 遍历所有课表，检查是否有冲突
            for (Schedule schedule : allSchedules) {
                // 检查是否使用同一教室
                if (!schedule.getClassroomId().equals(classroomId)) {
                    continue;
                }

                // 解析现有课表的日期信息
                JsonNode scheduleInfoNode = objectMapper.readTree(schedule.getScheduleInfo());
                JsonNode timesNode = scheduleInfoNode.path("times");

                // 遍历课表的所有时间点
                for (JsonNode timeNode : timesNode) {
                    String scheduleDate = formatDate(timeNode);
                    String schedulePeriod = timeNode.path("period").asText();

                    // 检查是否在同一时间
                    if (examDate.equals(scheduleDate) && examPeriod.equals(schedulePeriod)) {
                        return true; // 存在冲突
                    }
                }
            }

            return false; // 无冲突
        } catch (IOException e) {
            e.printStackTrace();
            return true; // 解析错误时默认认为有冲突
        }
    }

    @Override
    public boolean isScheduleWithExamConflict(Schedule schedule) {
        try {
            // 解析当前课表的日期信息
            JsonNode scheduleInfoNode = objectMapper.readTree(schedule.getScheduleInfo());
            JsonNode timesNode = scheduleInfoNode.path("times");
            String classroomId = schedule.getClassroomId();

            // 获取所有考试
            List<Exam> allExams = examService.getAllExams();

            // 遍历课表的所有时间点
            for (JsonNode timeNode : timesNode) {
                String scheduleDate = formatDate(timeNode);
                String schedulePeriod = timeNode.path("period").asText();

                // 遍历所有考试，检查是否有冲突
                for (Exam exam : allExams) {
                    // 检查是否使用同一教室
                    if (!exam.getClassroomId().equals(classroomId)) {
                        continue;
                    }

                    // 解析现有考试的日期信息
                    JsonNode examDateNode = objectMapper.readTree(exam.getExamDateInfo());
                    String examDate = formatDate(examDateNode);
                    String examPeriod = examDateNode.path("period").asText();

                    // 检查是否在同一时间
                    if (scheduleDate.equals(examDate) && schedulePeriod.equals(examPeriod)) {
                        return true; // 存在冲突
                    }
                }
            }

            return false; // 无冲突
        } catch (IOException e) {
            e.printStackTrace();
            return true; // 解析错误时默认认为有冲突
        }
    }

    @Override
    public boolean isScheduleWithScheduleConflict(Schedule schedule) {
        try {
            // 解析当前课表的日期信息
            JsonNode scheduleInfoNode = objectMapper.readTree(schedule.getScheduleInfo());
            JsonNode timesNode = scheduleInfoNode.path("times");
            String classroomId = schedule.getClassroomId();
            Long scheduleId = schedule.getId();

            // 获取所有课表
            List<Schedule> allSchedules = scheduleService.getAllSchedules();

            // 遍历所有课表，检查是否有冲突
            for (Schedule existingSchedule : allSchedules) {
                // 跳过当前课表
                if (existingSchedule.getId().equals(scheduleId)) {
                    continue;
                }

                // 检查是否使用同一教室
                if (!existingSchedule.getClassroomId().equals(classroomId)) {
                    continue;
                }

                // 解析现有课表的日期信息
                JsonNode existingScheduleInfoNode = objectMapper.readTree(existingSchedule.getScheduleInfo());
                JsonNode existingTimesNode = existingScheduleInfoNode.path("times");

                // 遍历当前课表的所有时间点
                for (JsonNode timeNode : timesNode) {
                    String scheduleDate = formatDate(timeNode);
                    String schedulePeriod = timeNode.path("period").asText();

                    // 遍历现有课表的所有时间点
                    for (JsonNode existingTimeNode : existingTimesNode) {
                        String existingScheduleDate = formatDate(existingTimeNode);
                        String existingSchedulePeriod = existingTimeNode.path("period").asText();

                        // 检查是否在同一时间
                        if (scheduleDate.equals(existingScheduleDate) && schedulePeriod.equals(existingSchedulePeriod)) {
                            return true; // 存在冲突
                        }
                    }
                }
            }

            return false; // 无冲突
        } catch (IOException e) {
            e.printStackTrace();
            return true; // 解析错误时默认认为有冲突
        }
    }

    /**
     * 格式化日期为YYYY-MM-DD格式
     * @param dateNode 包含年、月、日的JSON节点
     * @return 格式化后的日期字符串
     */
    private String formatDate(JsonNode dateNode) {
        int year = dateNode.path("year").asInt();
        int month = dateNode.path("month").asInt();
        int day = dateNode.path("day").asInt();

        LocalDate date = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
}
