import { request } from "./http";

// 考试数据类型定义
export interface Exam {
  examId: string;
  classId: string;
  classroomId: string;
  examType: string;
  examDate: string;
  startTime: string;
  endTime: string;
  invigilatorId: string;
}

// 考试API服务
export const examApi = {
  /**
   * 获取所有考试
   */
  getAllExams: () => {
    return request.get<Exam[]>("/exams");
  },

  /**
   * 根据ID获取考试
   * @param examId 考试ID
   */
  getExamById: (examId: string) => {
    return request.get<Exam>(`/exams/${examId}`);
  },

  /**
   * 根据班级ID获取考试
   * @param classId 班级ID
   */
  getExamsByClass: (classId: string) => {
    return request.get<Exam[]>(`/exams/class/${classId}`);
  },

  /**
   * 根据教室ID获取考试
   * @param classroomId 教室ID
   */
  getExamsByClassroom: (classroomId: string) => {
    return request.get<Exam[]>(`/exams/classroom/${classroomId}`);
  },

  /**
   * 添加考试
   * @param exam 考试数据
   */
  addExam: (exam: Exam) => {
    return request.post<Exam>("/exams", exam);
  },

  /**
   * 更新考试
   * @param examId 考试ID
   * @param exam 考试数据
   */
  updateExam: (examId: string, exam: Exam) => {
    return request.put<Exam>(`/exams/${examId}`, exam);
  },

  /**
   * 删除考试
   * @param examId 考试ID
   */
  deleteExam: (examId: string) => {
    return request.delete(`/exams/${examId}`);
  },
};
