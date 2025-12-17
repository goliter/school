import { request } from "./http";

// 仪表盘统计数据类型定义
export interface DashboardStats {
  totalUsers: number;
  totalStudents: number;
  totalTeachers: number;
  totalAdmins: number;
  totalColleges: number;
  totalMajors: number;
  totalCourses: number;
  totalTeachingClasses: number;
  totalClassrooms: number;
  totalExams: number;
}

// 仪表盘API服务
export const dashboardApi = {
  /**
   * 获取仪表盘统计数据
   */
  getDashboardStats: () => {
    return request.get<DashboardStats>("/dashboard/stats");
  },
};
