<template>
  <div class="teacher-module">
    <div class="module-header">
      <h1>教师模块</h1>
    </div>
    <div class="module-content">
      <div class="welcome-section">
        <h2>欢迎，教师</h2>
        <p>这是教师模块的基础页面，具体功能将在这里实现。</p>
      </div>
      <div class="module-features">
        <div class="feature-item">
          <h3>课程管理</h3>
          <p>管理您负责的课程信息</p>
        </div>
        <div class="feature-item">
          <h3>成绩录入</h3>
          <p>录入和管理学生成绩</p>
        </div>
        <div class="feature-item">
          <h3>教学安排</h3>
          <p>查看和管理您的教学安排</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { currentUser } from "../api";

// 可以在这里添加教师模块相关的逻辑
</script>

<style scoped>
.teacher-module {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.module-header {
  text-align: center;
  margin-bottom: 40px;
}

.module-header h1 {
  color: #333;
  font-size: 2.5rem;
}

.module-content {
  background-color: #f0f4f8;
  border-radius: 8px;
  padding: 30px;
}

.welcome-section {
  text-align: center;
  margin-bottom: 40px;
}

.welcome-section h2 {
  color: #555;
  font-size: 1.8rem;
}

.welcome-section p {
  color: #777;
  font-size: 1.1rem;
}

.module-features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.feature-item {
  background-color: white;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.feature-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.feature-item h3 {
  color: #333;
  margin-bottom: 10px;
}

.feature-item p {
  color: #666;
}
</style>