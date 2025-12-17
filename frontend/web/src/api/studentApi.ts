import { request } from './http';

// 学生数据类型定义
export interface Student {
  studentId: string;
  name: string;
  gender: string;
  birthDate: Date;
  phone: string;
  majorCode: string;
  userId?: string;
  userAccount?: {
    userId: string;
    // 其他UserAccount字段...
  };
}

// 学生DTO类型定义（用于创建和更新学生）
export interface StudentDto {
  studentId: string;
  name: string;
  gender: string;
  birthDate: Date;
  phone: string;
  majorCode: string;
  userId: string;
  password: string;
  role: string;
  status: number;
}

// 学生API服务
export const studentApi = {
  /**
   * 获取所有学生
   */
  getAllStudents: () => {
    return request.get<Student[]>('/students');
  },

  /**
   * 根据ID获取学生
   * @param studentId 学生ID
   */
  getStudentById: (studentId: string) => {
    return request.get<Student>(`/students/${studentId}`);
  },

  /**
   * 根据专业代码获取学生
   * @param majorCode 专业代码
   */
  getStudentsByMajor: (majorCode: string) => {
    return request.get<Student[]>(`/students/major/${majorCode}`);
  },

  /**
   * 添加学生
   * @param studentDto 学生数据（包含用户账户信息）
   */
  addStudent: (studentDto: StudentDto) => {
    return request.post<void>('/students', studentDto);
  },

  /**
   * 更新学生
   * @param studentId 学生ID
   * @param studentDto 学生数据（包含用户账户信息）
   */
  updateStudent: (studentId: string, studentDto: StudentDto) => {
    return request.put<void>(`/students/${studentId}`, studentDto);
  },

  /**
   * 删除学生
   * @param studentId 学生ID
   */
  deleteStudent: (studentId: string) => {
    return request.delete(`/students/${studentId}`);
  }
};