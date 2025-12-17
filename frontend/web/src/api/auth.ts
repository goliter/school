import { ref, computed } from "vue";
import type { UserAccount } from "./userAccountApi";

// 定义用户认证状态接口
interface AuthState {
  user: UserAccount | null;
  token: string | null;
  isLoggedIn: boolean;
}

// 从localStorage读取初始状态
const loadAuthState = (): AuthState => {
  const storedUser = localStorage.getItem("user");
  const storedToken = localStorage.getItem("token");

  return {
    user: storedUser ? JSON.parse(storedUser) : null,
    token: storedToken,
    isLoggedIn: !!storedUser,
  };
};

// 认证状态
const authState = ref<AuthState>(loadAuthState());

// 计算属性：是否已登录
const isLoggedIn = computed(() => authState.value.isLoggedIn);

// 计算属性：当前用户
const currentUser = computed(() => authState.value.user);

// 计算属性：当前用户角色
const currentUserRole = computed(() => authState.value.user?.role || "");

// 保存认证状态到localStorage
const saveAuthState = (state: AuthState) => {
  if (state.user) {
    localStorage.setItem("user", JSON.stringify(state.user));
  } else {
    localStorage.removeItem("user");
  }

  if (state.token) {
    localStorage.setItem("token", state.token);
  } else {
    localStorage.removeItem("token");
  }
};

// 登录方法
export const login = (user: any, token?: string) => {
  // 确保user不是undefined或null
  if (!user) {
    console.error("登录失败: 用户数据无效");
    return;
  }

  try {
    // 移除可能存在的递归引用
    const sanitizedUser = JSON.parse(JSON.stringify(user));

    // 如果存在嵌套的userAccount或student，只保留顶层数据
    if (sanitizedUser.student && sanitizedUser.student.userAccount) {
      delete sanitizedUser.student.userAccount;
    }
    if (sanitizedUser.userAccount && sanitizedUser.userAccount.student) {
      delete sanitizedUser.userAccount.student;
    }

    authState.value = {
      user: sanitizedUser as UserAccount,
      token: token || null,
      isLoggedIn: true,
    };
    saveAuthState(authState.value);
  } catch (error) {
    console.error("登录失败: 数据处理错误", error);
    throw error;
  }
};

// 登出方法
export const logout = () => {
  authState.value = {
    user: null,
    token: null,
    isLoggedIn: false,
  };
  saveAuthState(authState.value);
};

// 检查登录状态
export const checkLoginStatus = () => {
  return authState.value.isLoggedIn;
};

// 获取当前用户
export const getCurrentUser = () => {
  return authState.value.user;
};

// 获取认证令牌
export const getToken = () => {
  return authState.value.token;
};

// 单独导出所有功能
export { isLoggedIn, currentUser, currentUserRole };

export const auth = {
  isLoggedIn,
  currentUser,
  currentUserRole,
  login,
  logout,
  checkLoginStatus,
  getCurrentUser,
  getToken,
};
