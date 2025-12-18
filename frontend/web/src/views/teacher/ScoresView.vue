<template>
  <div class="scores-container">
    <div class="management-header">
      <h2>成绩管理</h2>
      <button
        class="btn btn-primary"
        @click="openScoreEditModal({ studentId: '', examId: '', score: 0 })"
        style="margin-right: 10px"
      >
        添加成绩
      </button>
    </div>

    <div class="search-section">
      <input
        v-model="searchQuery"
        type="text"
        placeholder="搜索学生姓名或考试名称..."
        class="search-input"
      />
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载成绩数据...</p>
    </div>
    <div v-else class="scores-table-container">
      <table class="scores-table">
        <thead>
          <tr>
            <th>学生姓名</th>
            <th>考试名称</th>
            <th>成绩</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="score in filteredScores"
            :key="score.studentId + '-' + score.examId"
          >
            <td>{{ getStudentName(score.studentId) }}</td>
            <td>{{ getExamName(score.examId) }}</td>
            <td>{{ score.score }}</td>
            <td>
              <button
                class="btn btn-sm btn-info"
                @click="openScoreEditModal(score)"
              >
                编辑
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="filteredScores.length === 0" class="empty-state">
        <p>暂无成绩数据</p>
      </div>
    </div>

    <!-- 成绩编辑模态框 -->
    <div
      v-if="showEditModal"
      class="modal-overlay"
      @click.self="closeScoreEditModal"
    >
      <div class="modal">
        <div class="modal-header">
          <h3>{{ editingScore ? "编辑成绩" : "添加成绩" }}</h3>
          <button class="modal-close" @click="closeScoreEditModal">
            &times;
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label for="studentId">学生</label>
            <select
              v-model="formData.studentId"
              id="studentId"
              class="form-control"
            >
              <option value="">请选择学生</option>
              <option
                v-for="student in students"
                :key="student.studentId"
                :value="student.studentId"
              >
                {{ student.name }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="examId">考试</label>
            <select v-model="formData.examId" id="examId" class="form-control">
              <option value="">请选择考试</option>
              <option
                v-for="exam in exams"
                :key="exam.examId"
                :value="exam.examId"
              >
                {{ exam.examName }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="score">成绩</label>
            <input
              v-model.number="formData.score"
              type="number"
              id="score"
              placeholder="输入成绩"
              class="form-control"
              min="0"
              max="100"
            />
          </div>

          <div class="modal-footer">
            <button class="btn btn-secondary" @click="closeScoreEditModal">
              取消
            </button>
            <button class="btn btn-primary" @click="saveScore">保存</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { scoreApi, type Score } from "../../api/scoreApi";
import { studentApi, type Student } from "../../api/studentApi";
import { examApi, type Exam } from "../../api/examApi";
import { teacherApi, type Teacher } from "../../api/teacherApi";
import { currentUser } from "../../api/auth";

// 数据
const scores = ref<Score[]>([]);
const students = ref<Student[]>([]);
const exams = ref<Exam[]>([]);
const teachers = ref<Teacher[]>([]);
const searchQuery = ref("");
const loading = ref(true);
const currentTeacher = ref<Teacher | null>(null);

// 编辑模态框
const showEditModal = ref(false);
const editingScore = ref<Score | null>(null);
const formData = ref<Score>({
  studentId: "",
  examId: "",
  score: 0,
});

// 获取当前登录用户的教师信息
const fetchCurrentTeacherInfo = async () => {
  try {
    const userId = currentUser.value?.userId;
    if (!userId) {
      console.error("未找到当前登录用户ID");
      return;
    }

    // 先获取所有教师，然后根据userId筛选
    const allTeachers = await teacherApi.getAllTeachers();
    const teacher = allTeachers.find((t: Teacher) => t.userId === userId);

    if (teacher) {
      currentTeacher.value = teacher;
    } else {
      console.error("未找到对应的教师信息");
    }
  } catch (error) {
    console.error("获取当前教师信息失败:", error);
  }
};

// 获取所有成绩
const fetchScores = async () => {
  try {
    const data = await scoreApi.getAllScores();
    scores.value = data;
  } catch (error) {
    console.error("获取成绩数据失败:", error);
  }
};

// 获取所有学生
const fetchStudents = async () => {
  try {
    const data = await studentApi.getAllStudents();
    students.value = data;
  } catch (error) {
    console.error("获取学生数据失败:", error);
  }
};

// 获取所有考试
const fetchExams = async () => {
  try {
    const data = await examApi.getAllExams();
    exams.value = data;
  } catch (error) {
    console.error("获取考试数据失败:", error);
  }
};

// 获取所有教师
const fetchTeachers = async () => {
  try {
    const data = await teacherApi.getAllTeachers();
    teachers.value = data;
  } catch (error) {
    console.error("获取教师数据失败:", error);
  }
};

// 根据学生ID获取学生姓名
const getStudentName = (studentId: string): string => {
  const student = students.value.find((s) => s.studentId === studentId);
  return student ? student.name : "未知学生";
};

// 根据考试ID获取考试名称
const getExamName = (examId: string): string => {
  const exam = exams.value.find((e) => e.examId === examId);
  return exam ? exam.examName : "未知考试";
};

// 搜索筛选
const filteredScores = computed(() => {
  if (!searchQuery.value) {
    return scores.value;
  }
  const query = searchQuery.value.toLowerCase();
  return scores.value.filter((score) => {
    const studentName = getStudentName(score.studentId).toLowerCase();
    const examName = getExamName(score.examId).toLowerCase();
    return studentName.includes(query) || examName.includes(query);
  });
});

// 打开成绩编辑模态框
const openScoreEditModal = (score: Score) => {
  // 判断是否是添加操作（根据studentId和examId是否都为空）
  editingScore.value = score.studentId && score.examId ? score : null;
  formData.value = { ...score };
  showEditModal.value = true;
};

// 关闭成绩编辑模态框
const closeScoreEditModal = () => {
  showEditModal.value = false;
  editingScore.value = null;
  formData.value = {
    studentId: "",
    examId: "",
    score: 0,
  };
};

// 保存成绩
const saveScore = async () => {
  try {
    if (editingScore.value) {
      // 更新成绩
      await scoreApi.updateScore(formData.value);
      // 更新本地数据
      const index = scores.value.findIndex(
        (s) =>
          s.studentId === formData.value.studentId &&
          s.examId === formData.value.examId
      );
      if (index !== -1) {
        scores.value[index] = { ...formData.value };
      }
    } else {
      // 添加成绩
      const newScore = await scoreApi.addScore(formData.value);
      scores.value.push(newScore);
    }
    closeScoreEditModal();
  } catch (error) {
    console.error("保存成绩失败:", error);
  }
};

// 初始化数据
onMounted(async () => {
  try {
    loading.value = true;
    // 并行加载数据以提高性能
    await Promise.all([
      fetchCurrentTeacherInfo(),
      fetchScores(),
      fetchStudents(),
      fetchExams(),
      fetchTeachers(),
    ]);
  } catch (error) {
    console.error("初始化数据失败:", error);
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

.btn {
  padding: 10px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: 500;
  transition: background-color 0.3s ease;
  display: flex;
  align-items: center;
  gap: 5px;
}

.btn-primary {
  background-color: #3498db;
  color: white;
}

.btn-primary:hover {
  background-color: #2980b9;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
  margin-right: 10px;
}

.btn-secondary:hover {
  background-color: #5a6268;
}

.btn-info {
  background-color: #17a2b8;
  color: white;
  padding: 6px 12px;
}

.btn-info:hover {
  background-color: #138496;
}

.btn-sm {
  font-size: 0.8rem;
  padding: 5px 10px;
}

.search-section {
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
  max-width: 400px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
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

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  color: #333;
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #666;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 600;
  color: #555;
}

.form-control {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
}

.form-control:focus {
  outline: none;
  border-color: #3498db;
}

.modal-footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
