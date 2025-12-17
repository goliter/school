<script setup lang="ts">
import { ref, onMounted } from "vue";
import { collegeApi, type College } from "../api";

// 创建响应式数据
const colleges = ref<College[]>([]);
const loading = ref(false);
const error = ref("");

// 在组件挂载时获取学院列表
onMounted(async () => {
  try {
    loading.value = true;
    const response = await collegeApi.getAllColleges();
    colleges.value = response || [];
  } catch (err) {
    error.value = err instanceof Error ? err.message : "获取学院列表失败";
    console.error("获取学院列表失败:", err);
  } finally {
    loading.value = false;
  }
});
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
          <router-link to="/login" class="login-btn">登录</router-link>
        </nav>
      </div>
    </header>

    <!-- 主体内容 -->
    <main class="main">
      <!-- 英雄区域 -->
      <section class="hero">
        <div class="hero-content">
          <h2>欢迎使用东北林业大学教务系统</h2>
          <p>为师生提供便捷的教务管理服务平台</p>
        </div>
      </section>

      <!-- 功能介绍 -->
      <section class="features">
        <div class="features-content">
          <div class="feature-item">
            <h3>学生服务</h3>
            <ul>
              <li>课程查询与选修</li>
              <li>成绩查询与管理</li>
              <li>个人信息维护</li>
              <li>课表查询与导出</li>
            </ul>
          </div>
          <div class="feature-item">
            <h3>教师服务</h3>
            <ul>
              <li>课程管理与排课</li>
              <li>成绩录入与统计</li>
              <li>学生信息查询</li>
              <li>教学计划管理</li>
            </ul>
          </div>
          <div class="feature-item">
            <h3>管理员服务</h3>
            <ul>
              <li>用户权限管理</li>
              <li>课程设置与管理</li>
              <li>数据统计与分析</li>
              <li>系统维护与监控</li>
            </ul>
          </div>
        </div>
      </section>

      <!-- 系统特点 -->
      <section class="system-features">
        <div class="system-features-content">
          <h3>系统特点</h3>
          <div class="system-feature-list">
            <div class="system-feature-item">
              <span class="feature-icon">🔒</span>
              <span>安全可靠</span>
            </div>
            <div class="system-feature-item">
              <span class="feature-icon">⚡</span>
              <span>高效稳定</span>
            </div>
            <div class="system-feature-item">
              <span class="feature-icon">📱</span>
              <span>响应式设计</span>
            </div>
            <div class="system-feature-item">
              <span class="feature-icon">🤝</span>
              <span>用户友好</span>
            </div>
          </div>
        </div>
      </section>

      <!-- 学院列表展示（API调用示例） -->
      <section class="colleges-section">
        <div class="colleges-content">
          <h3>学院列表（API调用示例）</h3>
          <div v-if="loading" class="loading">加载中...</div>
          <div v-else-if="error" class="error">{{ error }}</div>
          <div v-else-if="colleges.length === 0" class="no-data">
            暂无学院数据
          </div>
          <div v-else class="colleges-list">
            <div
              class="college-item"
              v-for="college in colleges"
              :key="college.collegeId"
            >
              <h4>{{ college.collegeName }}</h4>
              <p>学院代码: {{ college.collegeId }}</p>
              <p>地址: {{ college.address }}</p>
              <p>电话: {{ college.phone }}</p>
            </div>
          </div>
        </div>
      </section>
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <p>&copy; 2025 东北林业大学 版权所有</p>
        <p>技术支持：信息与计算机工程学院</p>
      </div>
    </footer>
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

.login-btn {
  background-color: #0066cc;
  color: white;
  padding: 0.5rem 1.5rem;
  border-radius: 4px;
  text-decoration: none;
  font-weight: 500;
  transition: background-color 0.3s;
}

.login-btn:hover {
  background-color: #004499;
}

/* 英雄区域 */
.hero {
  background-color: #00509e;
  color: white;
  padding: 4rem 0;
  text-align: center;
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

.hero h2 {
  font-size: 2.5rem;
  margin: 0 0 1rem;
}

.hero p {
  font-size: 1.2rem;
  margin: 0;
  opacity: 0.9;
}

/* 功能介绍 */
.features {
  padding: 4rem 0;
  background-color: white;
}

.features-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.feature-item {
  background-color: #f9f9f9;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.feature-item h3 {
  color: #003366;
  font-size: 1.5rem;
  margin: 0 0 1rem;
  border-bottom: 2px solid #0066cc;
  padding-bottom: 0.5rem;
}

.feature-item ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.feature-item li {
  padding: 0.5rem 0;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
}

.feature-item li::before {
  content: "•";
  color: #0066cc;
  font-weight: bold;
  margin-right: 0.5rem;
}

/* 系统特点 */
.system-features {
  padding: 3rem 0;
  background-color: #f5f5f5;
}

.system-features-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  text-align: center;
}

.system-features-content h3 {
  color: #003366;
  font-size: 2rem;
  margin: 0 0 2rem;
}

.system-feature-list {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 2rem;
}

.system-feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.2rem;
  color: #333;
}

.feature-icon {
  font-size: 2.5rem;
}

/* 学院列表展示 */
.colleges-section {
  padding: 4rem 0;
  background-color: white;
}

.colleges-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  text-align: center;
}

.colleges-content h3 {
  color: #003366;
  font-size: 2rem;
  margin: 0 0 2rem;
}

.loading {
  font-size: 1.2rem;
  color: #0066cc;
  padding: 2rem;
}

.error {
  font-size: 1.2rem;
  color: #dc3545;
  padding: 2rem;
}

.no-data {
  font-size: 1.2rem;
  color: #6c757d;
  padding: 2rem;
}

.colleges-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
  margin-top: 2rem;
}

.college-item {
  background-color: #f9f9f9;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: left;
}

.college-item h4 {
  color: #003366;
  font-size: 1.5rem;
  margin: 0 0 1rem;
}

.college-item p {
  margin: 0.5rem 0;
  color: #333;
}

/* 页脚 */
.footer {
  background-color: #003366;
  color: white;
  padding: 2rem 0;
  margin-top: auto;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  text-align: center;
}

.footer-content p {
  margin: 0.5rem 0;
  opacity: 0.9;
}
</style>
