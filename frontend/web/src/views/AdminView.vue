<template>
  <div class="admin-container">
    <!-- 侧边导航栏 -->
    <aside class="admin-sidebar">
      <div class="sidebar-header">
        <h2>管理员系统</h2>
      </div>
      <nav class="sidebar-nav">
        <ul>
          <li>
            <router-link to="/admin/dashboard" class="nav-link">
              <span class="nav-icon">📊</span>
              <span class="nav-text">仪表盘</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/users" class="nav-link">
              <span class="nav-icon">👤</span>
              <span class="nav-text">用户管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/colleges" class="nav-link">
              <span class="nav-icon">🏫</span>
              <span class="nav-text">学院管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/majors" class="nav-link">
              <span class="nav-icon">📚</span>
              <span class="nav-text">专业管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/courses" class="nav-link">
              <span class="nav-icon">📖</span>
              <span class="nav-text">课程管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/classrooms" class="nav-link">
              <span class="nav-icon">🏢</span>
              <span class="nav-text">教室管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/teaching-classes" class="nav-link">
              <span class="nav-icon">👥</span>
              <span class="nav-text">教学班管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/exams" class="nav-link">
              <span class="nav-icon">📝</span>
              <span class="nav-text">考试管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/schedules" class="nav-link">
              <span class="nav-icon">⏰</span>
              <span class="nav-text">课表管理</span>
            </router-link>
          </li>
        </ul>
      </nav>
    </aside>

    <!-- 主内容区域 -->
    <main class="admin-main">
      <div class="main-header">
        <h1>{{ currentPageTitle }}</h1>
        <div class="user-info">
          <span>欢迎，管理员</span>
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
    adminDashboard: "仪表盘",
    adminUsers: "用户管理",
    adminColleges: "学院管理",
    adminMajors: "专业管理",
    adminCourses: "课程管理",
    adminClassrooms: "教室管理",
    adminTeachingClasses: "教学班管理",
    adminExams: "考试管理",
    adminSchedules: "课表管理"
  };
  return titles[route.name as string] || "管理员系统";
});
</script>

<style scoped>
.admin-container {
  display: flex;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 侧边导航栏 */
.admin-sidebar {
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
.admin-main {
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