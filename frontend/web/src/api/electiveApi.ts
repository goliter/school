import { request } from "./http";

// 选课数据类型定义
export interface Elective {
  studentId: string;
  classId: string;
  electiveTime: string;
  status: string;
}

// 选课API服务
export const electiveApi = {
  /**
   * 获取所有选课记录
   */
  getAllElectives: () => {
    return request.get<Elective[]>('/electives');
  },

  /**
   * 根据学生ID获取选课记录
   * @param studentId 学生ID
   */
  getElectivesByStudentId: (studentId: string) => {
    return request.get<Elective[]>(`/electives/student/${studentId}`);
  },

  /**
   * 根据班级ID获取选课记录
   * @param classId 班级ID
   */
  getElectivesByClassId: (classId: string) => {
    return request.get<Elective[]>(`/electives/class/${classId}`);
  },

  /**
   * 获取特定学生的特定课程选课记录
   * @param studentId 学生ID
   * @param classId 班级ID
   */
  getElective: (studentId: string, classId: string) => {
    return request.get<Elective>(`/electives/${studentId}/${classId}`);
  },

  /**
   * 添加选课记录
   * @param elective 选课数据
   */
  addElective: (elective: Elective) => {
    return request.post<Elective>('/electives', elective);
  },

  /**
   * 更新选课记录
   * @param elective 选课数据
   */
  updateElective: (elective: Elective) => {
    return request.put<Elective>('/electives', elective);
  },

  /**
   * 删除选课记录
   * @param studentId 学生ID
   * @param classId 班级ID
   */
  deleteElective: (studentId: string, classId: string) => {
    return request.delete(`/electives/${studentId}/${classId}`);
  },
};
