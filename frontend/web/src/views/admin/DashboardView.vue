<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h2>管理员仪表盘</h2>
      <p>欢迎使用管理系统，以下是系统概览</p>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载数据...</p>
    </div>

    <!-- 数据内容 -->
    <div v-else>
      <div class="dashboard-stats">
        <div class="stat-card">
          <div class="stat-icon">👤</div>
          <div class="stat-content">
            <h3>{{ totalUsers }}</h3>
            <p>总用户数</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">🏫</div>
          <div class="stat-content">
            <h3>{{ totalColleges }}</h3>
            <p>学院数量</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">📖</div>
          <div class="stat-content">
            <h3>{{ totalCourses }}</h3>
            <p>课程数量</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">👥</div>
          <div class="stat-content">
            <h3>{{ totalTeachingClasses }}</h3>
            <p>教学班数量</p>
          </div>
        </div>
      </div>

      <div class="dashboard-actions">
        <div class="action-section">
          <h3>快速操作</h3>
          <div class="action-buttons">
            <router-link to="/admin/users" class="action-button">
              <span class="action-icon">➕</span>
              <span class="action-text">添加用户</span>
            </router-link>
            <router-link to="/admin/courses" class="action-button">
              <span class="action-icon">➕</span>
              <span class="action-text">添加课程</span>
            </router-link>
            <router-link to="/admin/colleges" class="action-button">
              <span class="action-icon">➕</span>
              <span class="action-text">添加学院</span>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { dashboardApi, type DashboardStats } from "../../api/dashboardApi";

// 统计数据
const totalUsers = ref(0);
const totalColleges = ref(0);
const totalCourses = ref(0);
const totalTeachingClasses = ref(0);
// 加载状态
const loading = ref(true);

// 加载仪表盘数据
onMounted(async () => {
  try {
    loading.value = true;
    const response = await dashboardApi.getDashboardStats();
    const stats = response;
    totalUsers.value = stats.totalUsers;
    totalColleges.value = stats.totalColleges;
    totalCourses.value = stats.totalCourses;
    totalTeachingClasses.value = stats.totalTeachingClasses;
  } catch (error) {
    console.error("获取仪表盘数据失败:", error);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.dashboard-container {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.dashboard-header {
  margin-bottom: 30px;
}

.dashboard-header h2 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 1.5rem;
}

.dashboard-header p {
  margin: 0;
  color: #666;
  font-size: 1rem;
}

.dashboard-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  font-size: 2.5rem;
  margin-right: 20px;
  color: #3498db;
}

.stat-content h3 {
  margin: 0;
  font-size: 1.8rem;
  color: #333;
}

.stat-content p {
  margin: 5px 0 0 0;
  color: #666;
  font-size: 0.9rem;
}

.dashboard-actions {
  margin-top: 20px;
}

.action-section h3 {
  margin: 0 0 15px 0;
  color: #333;
  font-size: 1.2rem;
}

.action-buttons {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.action-button {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  background-color: #3498db;
  color: white;
  border-radius: 6px;
  text-decoration: none;
  font-size: 1rem;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.action-button:hover {
  background-color: #2980b9;
  transform: translateY(-2px);
}

.action-icon {
  margin-right: 8px;
  font-size: 1.1rem;
}

.action-text {
  font-weight: 500;
}

/* 加载动画样式 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #666;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(52, 152, 219, 0.2);
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
