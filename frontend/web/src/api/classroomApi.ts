import { request } from "./http";

// 教室数据类型定义
export interface Classroom {
  classroomId: string;
  building: string;
  capacity: number;
  nature: string;
}

// 教室API服务
export const classroomApi = {
  /**
   * 获取所有教室
   */
  getAllClassrooms: () => {
    return request.get<Classroom[]>("/classrooms");
  },

  /**
   * 根据ID获取教室
   * @param classroomId 教室ID
   */
  getClassroomById: (classroomId: string) => {
    return request.get<Classroom>(`/classrooms/${classroomId}`);
  },

  /**
   * 根据教学楼获取教室
   * @param building 教学楼
   */
  getClassroomsByBuilding: (building: string) => {
    return request.get<Classroom[]>(`/classrooms/building/${building}`);
  },

  /**
   * 添加教室
   * @param classroom 教室数据
   */
  addClassroom: (classroom: Classroom) => {
    return request.post<Classroom>("/classrooms", classroom);
  },

  /**
   * 更新教室
   * @param classroomId 教室ID
   * @param classroom 教室数据
   */
  updateClassroom: (classroomId: string, classroom: Classroom) => {
    return request.put<Classroom>(`/classrooms/${classroomId}`, classroom);
  },

  /**
   * 删除教室
   * @param classroomId 教室ID
   */
  deleteClassroom: (classroomId: string) => {
    return request.delete(`/classrooms/${classroomId}`);
  },
};
