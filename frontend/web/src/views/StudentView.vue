<template>
  <div class="student-container">
    <!-- 侧边导航栏 -->
    <aside class="student-sidebar">
      <div class="sidebar-header">
        <h2>学生系统</h2>
      </div>
      <nav class="sidebar-nav">
        <ul>
          <li>
            <router-link to="/student/personal-info" class="nav-link">
              <span class="nav-icon">👤</span>
              <span class="nav-text">个人信息</span>
            </router-link>
          </li>
          <li>
            <router-link to="/student/courses" class="nav-link">
              <span class="nav-icon">📖</span>
              <span class="nav-text">课程管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/student/exams" class="nav-link">
              <span class="nav-icon">📝</span>
              <span class="nav-text">考试管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/student/schedule" class="nav-link">
              <span class="nav-icon">⏰</span>
              <span class="nav-text">个人课表</span>
            </router-link>
          </li>
          <li>
            <router-link to="/student/elective" class="nav-link">
              <span class="nav-icon">🔍</span>
              <span class="nav-text">选课管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/student/scores" class="nav-link">
              <span class="nav-icon">📊</span>
              <span class="nav-text">成绩查询</span>
            </router-link>
          </li>
        </ul>
      </nav>
    </aside>

    <!-- 主内容区域 -->
    <main class="student-main">
      <div class="main-header">
        <h1>{{ currentPageTitle }}</h1>
        <div class="header-actions">
          <router-link to="/" class="back-home-btn">返回首页</router-link>
          <div class="user-info">
            <span>欢迎，学生</span>
          </div>
        </div>
      </div>
      <div class="main-content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useRoute } from "vue-router";

// 根据当前路由计算页面标题
const route = useRoute();
const currentPageTitle = computed(() => {
    const titles: Record<string, string> = {
      studentPersonalInfo: "个人信息",
      studentCourses: "课程管理",
      studentExams: "考试管理",
      studentSchedule: "个人课表",
      studentElective: "选课管理",
      studentScores: "成绩查询"
    };
  return titles[route.name as string] || "学生系统";
});
</script>

<style scoped>
.student-container {
  display: flex;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 侧边导航栏 */
.student-sidebar {
  width: 250px;
  background-color: #2c3e50;
  color: white;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #34495e;
}

.sidebar-header h2 {
  margin: 0;
  font-size: 1.5rem;
}

.sidebar-nav {
  flex: 1;
  padding: 20px 0;
}

.sidebar-nav ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.sidebar-nav li {
  margin-bottom: 5px;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: white;
  text-decoration: none;
  transition: background-color 0.3s ease;
}

.nav-link:hover {
  background-color: #34495e;
}

.nav-link.router-link-active {
  background-color: #3498db;
}

.nav-icon {
  margin-right: 10px;
  font-size: 1.2rem;
}

.nav-text {
  font-size: 1rem;
}

/* 主内容区域 */
.student-main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.main-header {
  padding: 20px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 头部操作区域样式 */
.header-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

/* 返回首页按钮样式 */
.back-home-btn {
  background-color: #3498db;
  color: white;
  padding: 8px 16px;
  border-radius: 4px;
  text-decoration: none;
  font-size: 0.9rem;
  transition: background-color 0.3s ease;
}

.back-home-btn:hover {
  background-color: #2980b9;
}

.main-header h1 {
  margin: 0;
  font-size: 1.8rem;
  color: #333;
}

.user-info {
  font-size: 1rem;
  color: #666;
}

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}
</style>