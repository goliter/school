import { request } from "./http";

// 成绩数据类型定义
export interface Score {
  studentId: string;
  courseId: string;
  score: number;
  grade: string;
  remark: string;
}

// 成绩API服务
export const scoreApi = {
  /**
   * 获取所有成绩
   */
  getAllScores: () => {
    return request.get<Score[]>('/scores');
  },

  /**
   * 根据学生ID获取成绩
   * @param studentId 学生ID
   */
  getScoresByStudentId: (studentId: string) => {
    return request.get<Score[]>(`/scores/student/${studentId}`);
  },

  /**
   * 根据课程ID获取成绩
   * @param courseId 课程ID
   */
  getScoresByCourseId: (courseId: string) => {
    return request.get<Score[]>(`/scores/course/${courseId}`);
  },

  /**
   * 获取特定学生的特定课程成绩
   * @param studentId 学生ID
   * @param courseId 课程ID
   */
  getScore: (studentId: string, courseId: string) => {
    return request.get<Score>(`/scores/${studentId}/${courseId}`);
  },

  /**
   * 添加成绩
   * @param score 成绩数据
   */
  addScore: (score: Score) => {
    return request.post<Score>('/scores', score);
  },

  /**
   * 更新成绩
   * @param score 成绩数据
   */
  updateScore: (score: Score) => {
    return request.put<Score>('/scores', score);
  },

  /**
   * 删除成绩
   * @param studentId 学生ID
   * @param courseId 课程ID
   */
  deleteScore: (studentId: string, courseId: string) => {
    return request.delete(`/scores/${studentId}/${courseId}`);
  },
};
