import { request } from "./http";

// 学院数据类型定义
export interface College {
  collegeId: string;
  collegeName: string;
  address: string;
  phone: string;
}

// 学院API服务
export const collegeApi = {
  /**
   * 获取所有学院
   */
  getAllColleges: () => {
    return request.get<College[]>("/colleges");
  },

  /**
   * 根据ID获取学院
   * @param collegeId 学院ID
   */
  getCollegeById: (collegeId: string) => {
    return request.get<College>(`/colleges/${collegeId}`);
  },

  /**
   * 添加学院
   * @param college 学院数据
   */
  addCollege: (college: College) => {
    return request.post<College>("/colleges", college);
  },

  /**
   * 更新学院
   * @param collegeId 学院ID
   * @param college 学院数据
   */
  updateCollege: (collegeId: string, college: College) => {
    return request.put<College>(`/colleges/${collegeId}`, college);
  },

  /**
   * 删除学院
   * @param collegeId 学院ID
   */
  deleteCollege: (collegeId: string) => {
    return request.delete(`/colleges/${collegeId}`);
  },
};
