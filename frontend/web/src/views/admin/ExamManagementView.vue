<template>
  <div class="exam-management-container">
    <div class="management-header">
      <h2>考试管理</h2>
      <button @click="openAddModal" class="btn btn-primary">
        <i class="icon-plus"></i> 添加考试
      </button>
    </div>

    <div class="search-bar">
      <input
        v-model="searchKeyword"
        placeholder="搜索考试ID..."
        class="search-input"
      />
    </div>

    <div class="exam-list">
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>正在加载考试数据...</p>
      </div>
      <template v-else>
        <table class="data-table">
          <thead>
            <tr>
              <th>考试ID</th>
              <th>考试名称</th>
              <th>班级</th>
              <th>教室</th>
              <th>考试日期</th>
              <th>节次</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="exam in filteredExams" :key="exam.examId">
              <td>{{ exam.examId }}</td>
              <td>{{ exam.examName }}</td>
              <td>{{ getClassName(exam.classId) }}</td>
              <td>{{ getClassroomName(exam.classroomId) }}</td>
              <td>{{ formatExamDate(exam.examDateInfo) }}</td>
              <td>
                {{ exam.examDateInfo ? exam.examDateInfo.period : "未设置" }}
              </td>
              <td class="action-buttons">
                <button @click="editExam(exam)" class="edit-btn">
                  <i class="icon-edit"></i> 编辑
                </button>
                <button @click="deleteExam(exam.examId)" class="delete-btn">
                  <i class="icon-delete"></i> 删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-if="filteredExams.length === 0" class="empty-state">
          <p>暂无考试数据</p>
        </div>
      </template>
    </div>

    <!-- 添加/编辑考试模态框 -->
    <div v-if="showModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEditing ? "编辑考试" : "添加考试" }}</h3>
          <button @click="closeModal" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveExam">
            <div class="form-group">
              <label for="examId">考试ID</label>
              <input
                id="examId"
                v-model="formData.examId"
                type="text"
                placeholder="请输入考试ID"
                :disabled="isEditing"
                required
              />
            </div>

            <div class="form-group">
              <label for="classId">班级</label>
              <select id="classId" v-model="formData.classId" required>
                <option value="">请选择班级</option>
                <option
                  v-for="teachingClass in teachingClasses"
                  :key="teachingClass.classId"
                  :value="teachingClass.classId"
                >
                  {{ teachingClass.className }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label for="classroomId">教室</label>
              <select id="classroomId" v-model="formData.classroomId" required>
                <option value="">请选择教室</option>
                <option
                  v-for="classroom in classrooms"
                  :key="classroom.classroomId"
                  :value="classroom.classroomId"
                >
                  {{ classroom.building }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label for="examName">考试名称</label>
              <input
                id="examName"
                v-model="formData.examName"
                type="text"
                placeholder="请输入考试名称（如：期中考试、期末考试、补考等）"
                required
              />
            </div>

            <div class="form-group">
              <label for="examYear">年份</label>
              <input
                id="examYear"
                v-model.number="formData.examDateInfo.year"
                type="number"
                required
              />
            </div>

            <div class="form-group">
              <label for="examMonth">月份</label>
              <input
                id="examMonth"
                v-model.number="formData.examDateInfo.month"
                type="number"
                min="1"
                max="12"
                required
              />
            </div>

            <div class="form-group">
              <label for="examDay">日期</label>
              <input
                id="examDay"
                v-model.number="formData.examDateInfo.day"
                type="number"
                min="1"
                max="31"
                required
              />
            </div>

            <div class="form-group">
              <label for="examPeriod">节次</label>
              <input
                id="examPeriod"
                v-model.number="formData.examDateInfo.period"
                type="number"
                placeholder="请输入节次（如：1-2节输入1，3-4节输入3）"
                required
              />
            </div>

            <div class="form-actions">
              <button type="button" @click="closeModal" class="cancel-btn">
                取消
              </button>
              <button type="submit" class="submit-btn">
                {{ isEditing ? "更新" : "添加" }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { examApi, Exam, ExamDateInfo } from "../../api/examApi";
import { teachingClassApi } from "../../api/teachingClassApi";
import { classroomApi } from "../../api/classroomApi";

// 响应式数据
const loading = ref(true);
const exams = ref<Exam[]>([]);
const teachingClasses = ref<any[]>([]);
const classrooms = ref<any[]>([]);
const searchKeyword = ref("");
const showModal = ref(false);
const isEditing = ref(false);
const formData = ref<Exam>({
  examId: "",
  classId: "",
  classroomId: "",
  examName: "",
  examDateInfo: {
    year: new Date().getFullYear(),
    month: new Date().getMonth() + 1,
    day: new Date().getDate(),
    period: 1,
  },
});

// 计算属性：筛选后的考试列表
const filteredExams = computed(() => {
  if (!searchKeyword.value) return exams.value;
  const keyword = searchKeyword.value.toLowerCase();
  return exams.value.filter((exam) =>
    exam.examId.toLowerCase().includes(keyword)
  );
});

// 获取班级名称
const getClassName = (classId: string): string => {
  const teachingClass = teachingClasses.value.find(
    (tc) => tc.classId === classId
  );
  return teachingClass ? teachingClass.className : "未知班级";
};

// 获取教室名称
const getClassroomName = (classroomId: string): string => {
  const classroom = classrooms.value.find((c) => c.classroomId === classroomId);
  return classroom ? classroom.building : "未知教室";
};

// 格式化考试日期
const formatExamDate = (dateInfo: ExamDateInfo | null): string => {
  if (!dateInfo) return "未设置日期";
  return `${dateInfo.year}-${String(dateInfo.month).padStart(2, "0")}-${String(
    dateInfo.day
  ).padStart(2, "0")}`;
};

// 加载所有考试
const fetchExams = async () => {
  try {
    const data = await examApi.getAllExams();
    exams.value = data.map((exam) => ({
      ...exam,
      examDateInfo: exam.examDateInfo
        ? typeof exam.examDateInfo === "string"
          ? JSON.parse(exam.examDateInfo)
          : exam.examDateInfo
        : {
            year: new Date().getFullYear(),
            month: new Date().getMonth() + 1,
            day: new Date().getDate(),
            period: 1,
          },
    }));
  } catch (error) {
    console.error("获取考试数据失败:", error);
  }
};

// 加载所有教学班
const fetchTeachingClasses = async () => {
  try {
    const data = await teachingClassApi.getAllTeachingClasses();
    teachingClasses.value = data;
  } catch (error) {
    console.error("获取教学班数据失败:", error);
  }
};

// 加载所有教室
const fetchClassrooms = async () => {
  try {
    const data = await classroomApi.getAllClassrooms();
    classrooms.value = data;
  } catch (error) {
    console.error("获取教室数据失败:", error);
  }
};

// 打开添加模态框
const openAddModal = () => {
  isEditing.value = false;
  formData.value = {
    examId: "",
    classId: "",
    classroomId: "",
    examName: "",
    examDateInfo: {
      year: new Date().getFullYear(),
      month: new Date().getMonth() + 1,
      day: new Date().getDate(),
      period: 1,
    },
  };
  showModal.value = true;
};

// 打开编辑模态框
const editExam = (exam: Exam) => {
  isEditing.value = true;
  // 确保examDateInfo是对象形式
  const examData = { ...exam };
  if (examData.examDateInfo && typeof examData.examDateInfo === "string") {
    examData.examDateInfo = JSON.parse(examData.examDateInfo);
  }
  formData.value = examData;
  showModal.value = true;
};

// 关闭模态框
const closeModal = () => {
  showModal.value = false;
};

// 保存考试
const saveExam = async () => {
  try {
    if (isEditing.value) {
      await examApi.updateExam(formData.value.examId, formData.value);
    } else {
      await examApi.addExam(formData.value);
    }
    await fetchExams();
    closeModal();
  } catch (error: any) {
    console.error(isEditing.value ? "更新考试失败:" : "添加考试失败:", error);
    // 显示友好的错误提示，包含时间冲突的可能性
    alert(
      `${
        isEditing.value ? "更新" : "添加"
      }考试失败！可能的原因：\n\n1. 该时间和教室已有其他考试或课表`
    );
  }
};

// 删除考试
const deleteExam = async (examId: string) => {
  if (confirm("确定要删除这个考试吗？")) {
    try {
      await examApi.deleteExam(examId);
      await fetchExams();
    } catch (error) {
      console.error("删除考试失败:", error);
    }
  }
};

// 组件挂载时加载数据
onMounted(async () => {
  try {
    loading.value = true;
    await Promise.all([
      fetchExams(),
      fetchTeachingClasses(),
      fetchClassrooms(),
    ]);
  } catch (error) {
    console.error("加载考试数据失败:", error);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.exam-management-container {
  padding: 20px;
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
}

.btn-primary {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-primary:hover {
  background-color: #66b1ff;
}

.search-bar {
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
}

.search-input:focus {
  outline: none;
  border-color: #409eff;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.data-table th,
.data-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ebeef5;
}

.data-table th {
  background-color: #f5f7fa;
  font-weight: bold;
  color: #303133;
}

.data-table tr:hover {
  background-color: #f5f7fa;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.edit-btn {
  background-color: #67c23a;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.edit-btn:hover {
  background-color: #85ce61;
}

.delete-btn {
  background-color: #f56c6c;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.delete-btn:hover {
  background-color: #f78989;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #909399;
  background-color: white;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 500px;
  max-width: 90%;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #ebeef5;
}

.modal-header h3 {
  margin: 0;
  color: #303133;
}

.close-btn {
  background: none;
  border: none;
  font-size: 20px;
  color: #909399;
  cursor: pointer;
}

.close-btn:hover {
  color: #606266;
}

.modal-body {
  padding: 16px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #303133;
  font-weight: 500;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #409eff;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 24px;
}

.cancel-btn {
  background-color: #f5f7fa;
  color: #606266;
  border: 1px solid #dcdfe6;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.cancel-btn:hover {
  background-color: #e4e7ed;
}

.submit-btn {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.submit-btn:hover {
  background-color: #66b1ff;
}

/* 加载动画样式 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background-color: white;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(64, 158, 255, 0.2);
  border-top: 4px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
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
  color: #409eff;
  font-size: 14px;
  margin: 0;
}
</style>
