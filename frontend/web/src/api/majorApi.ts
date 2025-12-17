import { request } from "./http";

// 专业数据类型定义
export interface Major {
  majorCode: string;
  majorName: string;
  collegeId: string;
  description: string;
}

// 专业API服务
export const majorApi = {
  /**
   * 获取所有专业
   */
  getAllMajors: () => {
    return request.get<Major[]>('/majors');
  },

  /**
   * 根据ID获取专业
   * @param majorCode 专业代码
   */
  getMajorById: (majorCode: string) => {
    return request.get<Major>(`/majors/${majorCode}`);
  },

  /**
   * 添加专业
   * @param major 专业数据
   */
  addMajor: (major: Major) => {
    return request.post<Major>('/majors', major);
  },

  /**
   * 更新专业
   * @param majorCode 专业代码
   * @param major 专业数据
   */
  updateMajor: (majorCode: string, major: Major) => {
    return request.put<Major>(`/majors/${majorCode}`, major);
  },

  /**
   * 删除专业
   * @param majorCode 专业代码
   */
  deleteMajor: (majorCode: string) => {
    return request.delete(`/majors/${majorCode}`);
  },
};
