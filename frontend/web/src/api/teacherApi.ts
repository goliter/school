import { request } from "./http";

// 教师数据类型定义
export interface Teacher {
  teacherId: string;
  name: string;
  title: string;
  office: string;
  phone: string;
  duty: string;
  majorCode: string;
  userId?: string;
  userAccount?: {
    userId: string;
    // 其他UserAccount字段...
  };
}

// 教师DTO类型定义（用于创建和更新教师）
export interface TeacherDto {
  teacherId: string;
  name: string;
  title: string;
  office: string;
  phone: string;
  duty: string;
  majorCode: string;
  userId: string;
  password: string;
  status: number;
}

// 教师API服务
export const teacherApi = {
  /**
   * 获取所有教师
   */
  getAllTeachers: () => {
    return request.get<Teacher[]>("/teachers");
  },

  /**
   * 根据ID获取教师
   * @param teacherId 教师ID
   */
  getTeacherById: (teacherId: string) => {
    return request.get<Teacher>(`/teachers/${teacherId}`);
  },

  /**
   * 根据专业代码获取教师
   * @param majorCode 专业代码
   */
  getTeachersByMajor: (majorCode: string) => {
    return request.get<Teacher[]>(`/teachers/major/${majorCode}`);
  },

  /**
   * 添加教师
   * @param teacherDto 教师数据（包含用户账户信息）
   */
  addTeacher: (teacherDto: TeacherDto) => {
    return request.post<void>("/teachers", teacherDto);
  },

  /**
   * 更新教师
   * @param teacherId 教师ID
   * @param teacherDto 教师数据（包含用户账户信息）
   */
  updateTeacher: (teacherId: string, teacherDto: TeacherDto) => {
    return request.put<void>(`/teachers/${teacherId}`, teacherDto);
  },

  /**
   * 删除教师
   * @param teacherId 教师ID
   */
  deleteTeacher: (teacherId: string) => {
    return request.delete(`/teachers/${teacherId}`);
  },
};
