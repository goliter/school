import { request } from "./http";

// 课程数据类型定义
export interface Course {
  courseId: string;
  majorCode: string;
  courseName: string;
  credits: number;
  courseType: string;
  teachingHours: number;
  description: string;
}

// 课程API服务
export const courseApi = {
  /**
   * 获取所有课程
   */
  getAllCourses: () => {
    return request.get<Course[]>('/courses');
  },

  /**
   * 根据ID获取课程
   * @param courseId 课程ID
   */
  getCourseById: (courseId: string) => {
    return request.get<Course>(`/courses/${courseId}`);
  },

  /**
   * 根据专业代码获取课程
   * @param majorCode 专业代码
   */
  getCoursesByMajor: (majorCode: string) => {
    return request.get<Course[]>(`/courses/major/${majorCode}`);
  },

  /**
   * 添加课程
   * @param course 课程数据
   */
  addCourse: (course: Course) => {
    return request.post<Course>('/courses', course);
  },

  /**
   * 更新课程
   * @param courseId 课程ID
   * @param course 课程数据
   */
  updateCourse: (courseId: string, course: Course) => {
    return request.put<Course>(`/courses/${courseId}`, course);
  },

  /**
   * 删除课程
   * @param courseId 课程ID
   */
  deleteCourse: (courseId: string) => {
    return request.delete(`/courses/${courseId}`);
  },
};
