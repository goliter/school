import { request } from "./http";

// 用户账号数据类型定义
export interface UserAccount {
  userId: string;
  password: string;
  role: string;
  status: string;
  name: string;
  email: string;
  phone: string;
}

// 用户登录请求类型定义
export interface LoginRequest {
  userId: string;
  password: string;
}

// 用户账号API服务
export const userAccountApi = {
  /**
   * 获取所有用户账号
   */
  getAllUserAccounts: () => {
    return request.get<UserAccount[]>('/user-accounts');
  },

  /**
   * 根据ID获取用户账号
   * @param userId 用户ID
   */
  getUserAccountById: (userId: string) => {
    return request.get<UserAccount>(`/user-accounts/${userId}`);
  },

  /**
   * 添加用户账号
   * @param userAccount 用户账号数据
   */
  addUserAccount: (userAccount: UserAccount) => {
    return request.post<UserAccount>('/user-accounts', userAccount);
  },

  /**
   * 更新用户账号
   * @param userId 用户ID
   * @param userAccount 用户账号数据
   */
  updateUserAccount: (userId: string, userAccount: UserAccount) => {
    return request.put<UserAccount>(`/user-accounts/${userId}`, userAccount);
  },

  /**
   * 删除用户账号
   * @param userId 用户ID
   */
  deleteUserAccount: (userId: string) => {
    return request.delete(`/user-accounts/${userId}`);
  },

  /**
   * 用户登录
   * @param loginRequest 登录请求数据
   */
  login: (loginRequest: LoginRequest) => {
    return request.post<UserAccount>('/user-accounts/login', loginRequest);
  },
};
