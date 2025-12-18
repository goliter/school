import { request } from "./http";

// 成绩数据类型定义
export interface Score {
  studentId: string;
  examId: string;
  score: number;
}

// 成绩API服务
export const scoreApi = {
  /**
   * 获取所有成绩
   */
  getAllScores: () => {
    return request.get<Score[]>("/scores");
  },

  /**
   * 根据学生ID获取成绩
   * @param studentId 学生ID
   */
  getScoresByStudentId: (studentId: string) => {
    return request.get<Score[]>(`/scores/student/${studentId}`);
  },

  /**
   * 根据考试ID获取成绩
   * @param examId 考试ID
   */
  getScoresByExamId: (examId: string) => {
    return request.get<Score[]>(`/scores/exam/${examId}`);
  },

  /**
   * 获取特定学生的特定考试成绩
   * @param studentId 学生ID
   * @param examId 考试ID
   */
  getScore: (studentId: string, examId: string) => {
    return request.get<Score>(`/scores/${studentId}/${examId}`);
  },

  /**
   * 添加成绩
   * @param score 成绩数据
   */
  addScore: (score: Score) => {
    return request.post<Score>("/scores", score);
  },

  /**
   * 更新成绩
   * @param score 成绩数据
   */
  updateScore: (score: Score) => {
    return request.put<Score>("/scores", score);
  },

  /**
   * 删除成绩
   * @param studentId 学生ID
   * @param examId 考试ID
   */
  deleteScore: (studentId: string, examId: string) => {
    return request.delete(`/scores/${studentId}/${examId}`);
  },
};
