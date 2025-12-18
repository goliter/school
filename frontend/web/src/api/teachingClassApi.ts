import { request } from "./http";

// 教学班级数据类型定义
export interface TeachingClass {
  classId: string;
  courseId: string;
  teacherId: string;
  className: string;
  semester: string;
  courseName?: string;
  scheduleInfo?: string;
  studentCount?: number;
}

// 教学班级API服务
export const teachingClassApi = {
  /**
   * 获取所有教学班级
   */
  getAllTeachingClasses: () => {
    return request.get<TeachingClass[]>("/teaching-classes");
  },

  /**
   * 根据ID获取教学班级
   * @param classId 教学班级ID
   */
  getTeachingClassById: (classId: string) => {
    return request.get<TeachingClass>(`/teaching-classes/${classId}`);
  },

  /**
   * 根据课程ID获取教学班级
   * @param courseId 课程ID
   */
  getTeachingClassesByCourse: (courseId: string) => {
    return request.get<TeachingClass[]>(`/teaching-classes/course/${courseId}`);
  },

  /**
   * 根据教师ID获取教学班级
   * @param teacherId 教师ID
   */
  getTeachingClassesByTeacher: (teacherId: string) => {
    return request.get<TeachingClass[]>(
      `/teaching-classes/teacher/${teacherId}`
    );
  },

  /**
   * 添加教学班级
   * @param teachingClass 教学班级数据
   */
  addTeachingClass: (teachingClass: TeachingClass) => {
    return request.post<TeachingClass>("/teaching-classes", teachingClass);
  },

  /**
   * 更新教学班级
   * @param classId 教学班级ID
   * @param teachingClass 教学班级数据
   */
  updateTeachingClass: (classId: string, teachingClass: TeachingClass) => {
    return request.put<TeachingClass>(
      `/teaching-classes/${classId}`,
      teachingClass
    );
  },

  /**
   * 删除教学班级
   * @param classId 教学班级ID
   */
  deleteTeachingClass: (classId: string) => {
    return request.delete(`/teaching-classes/${classId}`);
  },
};
