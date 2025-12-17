import axios from "axios";
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from "axios";
import { getToken } from "./auth";

// API响应类型定义
export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
}

// 创建axios实例
const http: AxiosInstance = axios.create({
  baseURL: "/api", // 对应vite.config.ts中的代理配置
  timeout: 10000, // 请求超时时间
  headers: {
    "Content-Type": "application/json",
  },
});

// 请求拦截器
http.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    // 添加认证token
    const token = getToken();
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
http.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    // 统一处理响应
    const res = response.data;
    // 由于后端ApiResponse没有success字段，根据code判断请求是否成功
    if (res.code >= 200 && res.code < 300) {
      return res;
    } else {
      // 处理业务错误
      console.error("API Error:", res.message);
      return Promise.reject(new Error(res.message || "请求失败"));
    }
  },
  (error) => {
    // 处理网络错误
    console.error("Network Error:", error);
    return Promise.reject(error);
  }
);

// 封装常用请求方法
export const request = {
  get: <T = any>(url: string, config?: AxiosRequestConfig) => {
    return http.get<ApiResponse<T>>(url, config).then((res) => res.data);
  },

  post: <T = any>(url: string, data?: any, config?: AxiosRequestConfig) => {
    return http.post<ApiResponse<T>>(url, data, config).then((res) => res.data);
  },

  put: <T = any>(url: string, data?: any, config?: AxiosRequestConfig) => {
    return http.put<ApiResponse<T>>(url, data, config).then((res) => res.data);
  },

  delete: <T = any>(url: string, config?: AxiosRequestConfig) => {
    return http.delete<ApiResponse<T>>(url, config).then((res) => res.data);
  },
};

export default http;
