import { request } from "./http";

// 考试日期信息类型定义
export interface ExamDateInfo {
  year: number;
  month: number;
  day: number;
  period: number;
}

// 考试数据类型定义
export interface Exam {
  examId: string;
  examName: string;
  examDateInfo: ExamDateInfo;
  classroomId: string;
  classId: string;
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
  getExamsByClassId: (classId: string) => {
    return request.get<Exam[]>(`/exams/class/${classId}`);
  },

  /**
   * 根据教室ID获取考试
   * @param classroomId 教室ID
   */
  getExamsByClassroomId: (classroomId: string) => {
    return request.get<Exam[]>(`/exams/classroom/${classroomId}`);
  },

  /**
   * 根据教师ID获取考试
   * @param teacherId 教师ID
   */
  getExamsByTeacher: (teacherId: string) => {
    return request.get<Exam[]>(`/exams/teacher/${teacherId}`);
  },

  /**
   * 添加考试
   * @param exam 考试数据
   */
  addExam: (exam: Exam) => {
    // 将examDateInfo对象转换为JSON字符串
    const requestData = {
      ...exam,
      examDateInfo: JSON.stringify(exam.examDateInfo),
    };
    return request.post<Exam>("/exams", requestData);
  },

  /**
   * 更新考试
   * @param examId 考试ID
   * @param exam 考试数据
   */
  updateExam: (examId: string, exam: Exam) => {
    // 将examDateInfo对象转换为JSON字符串
    const requestData = {
      ...exam,
      examDateInfo: JSON.stringify(exam.examDateInfo),
    };
    return request.put<Exam>(`/exams/${examId}`, requestData);
  },
};
