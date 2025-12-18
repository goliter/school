package org.example.jdbc.service;

import org.example.jdbc.entity.Exam;
import org.example.jdbc.entity.Schedule;

/**
 * 冲突检查服务接口
 * 用于检查考试和课表在时间和教室上的冲突
 */
public interface ConflictCheckService {

    /**
     * 检查考试是否与现有考试或课表冲突
     * @param exam 待检查的考试
     * @return true表示存在冲突，false表示无冲突
     */
    boolean isExamConflict(Exam exam);

    /**
     * 检查课表是否与现有考试或课表冲突
     * @param schedule 待检查的课表
     * @return true表示存在冲突，false表示无冲突
     */
    boolean isScheduleConflict(Schedule schedule);

    /**
     * 检查考试与现有考试的冲突
     * @param exam 待检查的考试
     * @return true表示存在冲突，false表示无冲突
     */
    boolean isExamWithExamConflict(Exam exam);

    /**
     * 检查考试与现有课表的冲突
     * @param exam 待检查的考试
     * @return true表示存在冲突，false表示无冲突
     */
    boolean isExamWithScheduleConflict(Exam exam);

    /**
     * 检查课表与现有考试的冲突
     * @param schedule 待检查的课表
     * @return true表示存在冲突，false表示无冲突
     */
    boolean isScheduleWithExamConflict(Schedule schedule);

    /**
     * 检查课表与现有课表的冲突
     * @param schedule 待检查的课表
     * @return true表示存在冲突，false表示无冲突
     */
    boolean isScheduleWithScheduleConflict(Schedule schedule);
}
