import { request } from "./http";

// 单个课表时间信息类型定义
export interface ScheduleTime {
  year: number;
  month: number;
  day: number;
  period: string;
}

// 课表信息类型定义
export interface ScheduleInfo {
  times: ScheduleTime[];
}

// 课程表数据类型定义
export interface Schedule {
  id?: number;
  classId: string;
  classroomId: string;
  scheduleInfo: ScheduleInfo;
}

// 课程表API服务
export const scheduleApi = {
  /**
   * 获取所有课程表
   */
  getAllSchedules: () => {
    return request.get<Schedule[]>("/schedules");
  },

  /**
   * 根据班级ID获取课程表
   * @param classId 班级ID
   */
  getSchedulesByClass: (classId: string) => {
    return request.get<Schedule[]>(`/schedules/class/${classId}`);
  },

  /**
   * 根据教室ID获取课程表
   * @param classroomId 教室ID
   */
  getSchedulesByClassroom: (classroomId: string) => {
    return request.get<Schedule[]>(`/schedules/classroom/${classroomId}`);
  },

  /**
   * 添加课程表
   * @param schedule 课程表数据
   */
  addSchedule: (schedule: Schedule) => {
    const scheduleData = {
      ...schedule,
      scheduleInfo: JSON.stringify(schedule.scheduleInfo),
    };
    return request.post<Schedule>("/schedules", scheduleData);
  },

  /**
   * 更新课程表
   * @param schedule 课程表数据
   */
  updateSchedule: (schedule: Schedule) => {
    const scheduleData = {
      ...schedule,
      scheduleInfo: JSON.stringify(schedule.scheduleInfo),
    };
    return request.put<Schedule>("/schedules", scheduleData);
  },

  /**
   * 删除整个课程表
   * @param id 课程表ID
   */
  deleteSchedule: (id: number) => {
    return request.delete(`/schedules/${id}`);
  },

  /**
   * 获取当前教师的课程安排
   */
  getTeacherSchedules: (teacherId: string) => {
    return request.get<any[]>(`/schedules/teacher/${teacherId}`);
  },

  /**
   * 获取当前学生的课程安排
   */
  getStudentSchedules: (studentId: string) => {
    return request.get<any[]>(`/schedules/student/${studentId}`);
  },
};
