import { request } from "./http";

// 课程表数据类型定义
export interface Schedule {
  id?: number;
  classId: string;
  classroomId: string;
  week: number;
  weekday: number;
  periods: string;
  startDate: string;
  endDate: string;
}

// 课程表API服务
export const scheduleApi = {
  /**
   * 获取所有课程表
   */
  getAllSchedules: () => {
    return request.get<Schedule[]>('/schedules');
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
    return request.post<Schedule>('/schedules', schedule);
  },

  /**
   * 更新课程表
   * @param schedule 课程表数据
   */
  updateSchedule: (schedule: Schedule) => {
    return request.put<Schedule>('/schedules', schedule);
  },

  /**
   * 删除课程表
   * @param params 删除参数
   */
  deleteSchedule: (params: { classId: string; week: number; weekday: number; periods: string }) => {
    return request.delete('/schedules', { params });
  },
};
