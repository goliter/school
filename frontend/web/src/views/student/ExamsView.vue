<template>
  <div class="exam-management-container">
    <div class="management-header">
      <h2>考试管理</h2>
    </div>

    <div class="search-bar">
      <input
        v-model="searchKeyword"
        placeholder="搜索考试ID或课程名称..."
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
              <th>课程</th>
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
              <td>{{ getCourseName(exam.classId) }}</td>
              <td>{{ getClassName(exam.classId) }}</td>
              <td>{{ getClassroomName(exam.classroomId) }}</td>
              <td>{{ formatExamDate(exam.examDateInfo) }}</td>
              <td>
                {{ exam.examDateInfo ? exam.examDateInfo.period : "未设置" }}
              </td>
              <td class="action-buttons">
                <button @click="viewExam(exam)" class="view-btn">
                  <i class="icon-view"></i> 查看
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

    <!-- 查看考试详情模态框 -->
    <div v-if="showDetailModal" class="modal-overlay" @click="closeDetailModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>考试详情</h3>
          <button @click="closeDetailModal" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <div class="detail-item">
            <span class="detail-label">考试ID：</span>
            <span class="detail-value">{{ currentExam.examId }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">考试名称：</span>
            <span class="detail-value">{{ currentExam.examName }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">课程：</span>
            <span class="detail-value">{{ getCourseName(currentExam.classId) }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">班级：</span>
            <span class="detail-value">{{
              getClassName(currentExam.classId)
            }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">教室：</span>
            <span class="detail-value">{{
              getClassroomName(currentExam.classroomId)
            }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">考试日期：</span>
            <span class="detail-value">{{
              formatExamDate(currentExam.examDateInfo)
            }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">节次：</span>
            <span class="detail-value">{{
              currentExam.examDateInfo
                ? currentExam.examDateInfo.period
                : "未设置"
            }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { examApi, Exam, ExamDateInfo } from "../../api/examApi";
import { teachingClassApi, type TeachingClass } from "../../api/teachingClassApi";
import { classroomApi, type Classroom } from "../../api/classroomApi";
import { studentApi, type Student } from "../../api/studentApi";
import { courseApi, type Course } from "../../api/courseApi";
import { electiveApi, type Elective } from "../../api/electiveApi";
import { currentUser } from "../../api/auth";

// 当前学生信息
const currentStudent = ref<Student | null>(null);

// 响应式数据
const loading = ref(true);
const exams = ref<Exam[]>([]);
const teachingClasses = ref<TeachingClass[]>([]);
const classrooms = ref<Classroom[]>([]);
const courses = ref<Course[]>([]);
const electives = ref<Elective[]>([]);
const searchKeyword = ref("");
const showDetailModal = ref(false);
const currentExam = ref<Exam>({
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
    exam.examId.toLowerCase().includes(keyword) ||
    getCourseName(exam.classId).toLowerCase().includes(keyword)
  );
});

// 获取班级名称
const getClassName = (classId: string): string => {
  const teachingClass = teachingClasses.value.find(
    (tc) => tc.classId === classId
  );
  return teachingClass ? teachingClass.className : "未知班级";
};

// 获取课程名称
const getCourseName = (classId: string): string => {
  const teachingClass = teachingClasses.value.find(
    (tc) => tc.classId === classId
  );
  if (teachingClass) {
    const course = courses.value.find(c => c.courseId === teachingClass.courseId);
    return course ? course.courseName : "未知课程";
  }
  return "未知课程";
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

// 获取当前学生信息
const fetchCurrentStudentInfo = async () => {
  try {
    const userId = currentUser.value?.userId;
    if (!userId) {
      console.error("未找到当前登录用户ID");
      return;
    }
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

// 获取学生选修的课程
const fetchElectives = async () => {
  try {
    if (!currentStudent.value) return;
    const data = await electiveApi.getAllElectives();
    electives.value = data.filter(e => e.studentId === currentStudent.value?.studentId);
  } catch (error) {
    console.error("获取选修数据失败:", error);
  }
};

// 获取所有课程
const fetchCourses = async () => {
  try {
    const data = await courseApi.getAllCourses();
    courses.value = data;
  } catch (error) {
    console.error("获取课程数据失败:", error);
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

// 加载所有教学班
const fetchTeachingClasses = async () => {
  try {
    const data = await teachingClassApi.getAllTeachingClasses();
    teachingClasses.value = data;
  } catch (error) {
    console.error("获取教学班数据失败:", error);
  }
};

// 加载学生的考试
const fetchExams = async () => {
  try {
    if (!currentStudent.value) {
      exams.value = [];
      return;
    }
    
    // 获取所有考试
    const allExams = await examApi.getAllExams();
    
    // 筛选出学生所选课程的考试
    const studentClassIds = electives.value.map(e => e.classId);
    const studentExams = allExams.filter(exam => studentClassIds.includes(exam.classId));
    
    exams.value = studentExams.map((exam) => ({
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

// 查看考试详情
const viewExam = (exam: Exam) => {
  // 确保examDateInfo是对象形式
  const examData = { ...exam };
  if (examData.examDateInfo && typeof examData.examDateInfo === "string") {
    examData.examDateInfo = JSON.parse(examData.examDateInfo);
  }
  currentExam.value = examData;
  showDetailModal.value = true;
};

// 关闭详情模态框
const closeDetailModal = () => {
  showDetailModal.value = false;
};

// 组件挂载时加载数据
onMounted(async () => {
  try {
    loading.value = true;
    // 获取当前学生信息
    await fetchCurrentStudentInfo();
    // 并行获取其他数据
    await Promise.all([
      fetchElectives(),
      fetchCourses(),
      fetchTeachingClasses(),
      fetchClassrooms(),
      fetchExams()
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

.view-btn {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.view-btn:hover {
  background-color: #66b1ff;
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

.detail-item {
  margin-bottom: 16px;
}

.detail-label {
  display: inline-block;
  width: 100px;
  font-weight: bold;
  color: #303133;
}

.detail-value {
  color: #606266;
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