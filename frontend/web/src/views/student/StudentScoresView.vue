<template>
  <div class="scores-container">
    <div class="management-header">
      <h2>我的成绩</h2>
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载成绩数据...</p>
    </div>
    <div v-else class="scores-table-container">
      <table class="scores-table">
        <thead>
          <tr>
            <th>考试名</th>
            <th>成绩</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="score in studentScores" :key="score.examId">
            <td>{{ getExamName(score.examId) }}</td>
            <td>{{ score.score }}</td>
          </tr>
        </tbody>
      </table>
      <div v-if="studentScores.length === 0" class="empty-state">
        <p>暂无成绩数据</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { scoreApi, type Score } from "../../api/scoreApi";
import { examApi, type Exam } from "../../api/examApi";
import { studentApi, type Student } from "../../api/studentApi";
import { currentUser } from "../../api/auth";

// 数据
const scores = ref<Score[]>([]);
const exams = ref<Exam[]>([]);
const loading = ref(true);
const currentStudent = ref<Student | null>(null);

// 获取当前登录用户的学生信息
const fetchCurrentStudentInfo = async () => {
  try {
    const userId = currentUser.value?.userId;
    if (!userId) {
      console.error("未找到当前登录用户ID");
      return;
    }

    // 先获取所有学生，然后根据userId筛选
    const allStudents = await studentApi.getAllStudents();
    const student = allStudents.find((s) => s.userId === userId);

    if (student) {
      currentStudent.value = student;
    } else {
      console.error("未找到对应的学生信息");
    }
  } catch (error) {
    console.error("获取当前学生信息失败:", error);
  }
};

// 获取学生成绩
const fetchScores = async () => {
  try {
    if (!currentStudent.value) return;
    const data = await scoreApi.getScoresByStudentId(
      currentStudent.value.studentId
    );
    scores.value = data;
  } catch (error) {
    console.error("获取成绩数据失败:", error);
  }
};

// 获取所有考试信息
const fetchExams = async () => {
  try {
    const data = await examApi.getAllExams();
    exams.value = data;
  } catch (error) {
    console.error("获取考试数据失败:", error);
  }
};

// 根据考试ID获取考试名称
const getExamName = (examId: string): string => {
  const exam = exams.value.find((e) => e.examId === examId);
  return exam ? exam.examName : "未知考试";
};

// 计算学生成绩列表
const studentScores = computed(() => {
  return scores.value;
});

// 初始化数据
onMounted(async () => {
  try {
    loading.value = true;
    // 先获取当前学生信息
    await fetchCurrentStudentInfo();
    // 并行获取成绩和考试数据
    await Promise.all([fetchScores(), fetchExams()]);
  } catch (error) {
    console.error("加载数据失败:", error);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.scores-container {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.management-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.management-header h2 {
  margin: 0;
  color: #333;
  font-size: 1.5rem;
}

.scores-table-container {
  overflow-x: auto;
}

.scores-table {
  width: 100%;
  border-collapse: collapse;
}

.scores-table th,
.scores-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.scores-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.scores-table tr:hover {
  background-color: #f5f5f5;
}

.empty-state {
  text-align: center;
  padding: 50px 0;
  color: #666;
}

/* 加载动画样式 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 0;
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

.loading-container p {
  color: #666;
  font-size: 0.9rem;
}
</style>