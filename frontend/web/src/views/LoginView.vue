<script setup lang="ts">
import { ref } from "vue";

const username = ref("");
const password = ref("");
const userType = ref("student"); // student, teacher or admin

const handleLogin = () => {
  // 这里可以添加登录逻辑
  console.log("Login attempt:", {
    username: username.value,
    password: password.value,
    userType: userType.value,
  });
};
</script>

<template>
  <div class="container">
    <!-- 导航栏 -->
    <header class="header">
      <div class="header-content">
        <div class="logo">
          <h1>东北林业大学教务系统</h1>
        </div>
        <nav class="nav">
          <router-link to="/" class="home-btn">返回首页</router-link>
        </nav>
      </div>
    </header>

    <!-- 登录表单 -->
    <main class="login-main">
      <div class="login-container">
        <div class="login-box">
          <div class="login-header">
            <h2>用户登录</h2>
            <p>请输入您的账号信息</p>
          </div>

          <div class="login-form">
            <!-- 用户类型选择 -->
            <div class="user-type-selector">
              <label>
                <input
                  type="radio"
                  v-model="userType"
                  value="student"
                  name="userType"
                />
                <span>学生登录</span>
              </label>
              <label>
                <input
                  type="radio"
                  v-model="userType"
                  value="teacher"
                  name="userType"
                />
                <span>教师登录</span>
              </label>
              <label>
                <input
                  type="radio"
                  v-model="userType"
                  value="admin"
                  name="userType"
                />
                <span>管理员登录</span>
              </label>
            </div>

            <!-- 登录表单 -->
            <div class="form-group">
              <label for="username">账号</label>
              <input
                type="text"
                id="username"
                v-model="username"
                placeholder="请输入学号/工号/管理员账号"
                required
              />
            </div>

            <div class="form-group">
              <label for="password">密码</label>
              <input
                type="password"
                id="password"
                v-model="password"
                placeholder="请输入密码"
                required
              />
            </div>

            <div class="form-options">
              <label class="remember-me">
                <input type="checkbox" />
                <span>记住密码</span>
              </label>
              <router-link to="/forgot-password" class="forgot-password"
                >忘记密码？</router-link
              >
            </div>

            <button type="button" class="login-button" @click="handleLogin">
              登录
            </button>
          </div>

          <div class="login-footer">
            <p>东北林业大学教务管理中心 © 2025</p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.container {
  font-family: "Microsoft YaHei", sans-serif;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

/* 导航栏 */
.header {
  background-color: #003366;
  color: white;
  padding: 1rem 0;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
}

.logo h1 {
  font-size: 1.8rem;
  margin: 0;
  font-weight: 600;
}

.nav {
  display: flex;
  gap: 1rem;
}

.home-btn {
  background-color: transparent;
  color: white;
  padding: 0.5rem 1.5rem;
  border: 1px solid white;
  border-radius: 4px;
  text-decoration: none;
  font-weight: 500;
  transition: background-color 0.3s;
}

.home-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* 登录主区域 */
.login-main {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

.login-container {
  width: 100%;
  max-width: 1200px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  background-color: white;
  padding: 3rem;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 450px;
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.login-header h2 {
  color: #003366;
  font-size: 2rem;
  margin: 0 0 0.5rem;
}

.login-header p {
  color: #666;
  margin: 0;
}

/* 用户类型选择 */
.user-type-selector {
  display: flex;
  justify-content: center;
  gap: 1.5rem;
  margin-bottom: 2rem;
  padding: 1rem;
  background-color: #f9f9f9;
  border-radius: 4px;
  flex-wrap: wrap;
}

.user-type-selector label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  font-size: 1rem;
  color: #333;
}

.user-type-selector input[type="radio"] {
  width: 1rem;
  height: 1rem;
  accent-color: #0066cc;
}

/* 登录表单 */
.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-weight: 500;
  color: #333;
  font-size: 0.9rem;
}

.form-group input {
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #0066cc;
  box-shadow: 0 0 0 2px rgba(0, 102, 204, 0.1);
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.9rem;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  color: #666;
}

.remember-me input[type="checkbox"] {
  width: 0.9rem;
  height: 0.9rem;
  accent-color: #0066cc;
}

.forgot-password {
  color: #0066cc;
  text-decoration: none;
  transition: color 0.3s;
}

.forgot-password:hover {
  color: #004499;
  text-decoration: underline;
}

.login-button {
  background-color: #0066cc;
  color: white;
  border: none;
  padding: 1rem;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 1rem;
}

.login-button:hover {
  background-color: #0052a3;
}

.login-button:active {
  background-color: #004499;
}

/* 页脚 */
.login-footer {
  text-align: center;
  margin-top: 2rem;
  padding-top: 1.5rem;
  border-top: 1px solid #eee;
}

.login-footer p {
  color: #999;
  font-size: 0.9rem;
  margin: 0;
}
</style>
