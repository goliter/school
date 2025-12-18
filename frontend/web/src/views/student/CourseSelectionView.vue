<template>
  <div class="course-selection-container">
    <div class="course-selection-header">
      <h2>选课管理</h2>
      <div class="filter-options">
        <div class="filter-group">
          <label for="semester">学期：</label>
          <select
            v-model="selectedSemester"
            @change="fetchCourses"
            id="semester"
            class="form-control"
          >
            <option value="">全部学期</option>
            <option value="2024-2025-1">2024-2025学年第一学期</option>
            <option value="2024-2025-2">2024-2025学年第二学期</option>
          </select>
        </div>
        <div class="filter-group">
          <label for="courseType">课程类型：</label>
          <select
            v-model="selectedCourseType"
            @change="fetchCourses"
            id="courseType"
            class="form-control"
          >
            <option value="">全部类型</option>
            <option value="必修课">必修课</option>
            <option value="选修课">选修课</option>
            <option value="通识课">通识课</option>
          </select>
        </div>
        <div class="filter-group">
          <input
            v-model="searchQuery"
            type="text"
            placeholder="搜索课程名称或教师"
            class="search-input"
          />
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="loading-spinner"></div>
      <p>正在加载课程数据...</p>
    </div>

    <!-- 错误信息 -->
    <div v-else-if="error" class="error-message">
      <p>{{ error }}</p>
      <button @click="fetchCourses" class="retry-btn">重试</button>
    </div>

    <!-- 选课内容 -->
    <div v-else class="course-selection-content">
      <!-- 已选课程列表 -->
      <div class="selected-courses">
        <h3>已选课程</h3>
        <div v-if="selectedCourses.length > 0" class="course-list">
          <div
            v-for="(course, index) in selectedCourses"
            :key="index"
            class="course-card selected"
          >
            <div class="course-header">
              <h4>{{ course.courseName }}</h4>
              <span class="credit">{{ course.credit }}学分</span>
            </div>
            <div class="course-info">
              <p><strong>课程号：</strong>{{ course.courseId }}</p>
              <p><strong>班级：</strong>{{ course.className }}</p>
              <p><strong>教师：</strong>{{ course.teacherName }}</p>
              <p><strong>类型：</strong>{{ course.courseType }}</p>
              <p><strong>学期：</strong>{{ course.semester }}</p>
            </div>
            <div class="course-action">
              <button class="remove-btn" @click="removeCourse(course)">
                退选
              </button>
            </div>
          </div>
        </div>
        <div v-else class="empty-state">
          <p>尚未选择任何课程</p>
        </div>
        <div v-if="selectedCourses.length > 0" class="selection-summary">
          <p><strong>已选课程数：</strong>{{ selectedCourses.length }}</p>
          <p><strong>总学分：</strong>{{ totalCredits }}学分</p>
        </div>
      </div>

      <!-- 可选课程列表 -->
      <div class="available-courses">
        <h3>可选课程</h3>
        <div v-if="filteredCourses.length > 0" class="course-list">
          <div
            v-for="(course, index) in filteredCourses"
            :key="index"
            class="course-card"
            @click="selectCourse(course)"
          >
            <div class="course-header">
              <h4>{{ course.courseName }}</h4>
              <span class="credit">{{ course.credit }}学分</span>
            </div>
            <div class="course-info">
              <p><strong>课程号：</strong>{{ course.courseId }}</p>
              <p><strong>班级：</strong>{{ course.className }}</p>
              <p><strong>教师：</strong>{{ course.teacherName }}</p>
              <p><strong>类型：</strong>{{ course.courseType }}</p>
              <p><strong>学期：</strong>{{ course.semester }}</p>
              <p><strong>剩余名额：</strong>{{ course.availableSeats }}</p>
            </div>
            <div class="course-action">
              <button
                class="select-btn"
                :disabled="course.availableSeats <= 0"
                @click.stop="selectCourse(course)"
              >
                {{ course.availableSeats > 0 ? "选修" : "已满" }}
              </button>
            </div>
          </div>
        </div>
        <div v-else class="empty-state">
          <p>当前没有可选课程</p>
        </div>
      </div>
    </div>

    <!-- 选课成功模态框 -->
    <div
      v-if="showSuccessModal"
      class="modal-overlay"
      @click.self="closeSuccessModal"
    >
      <div class="modal">
        <div class="modal-header">
          <h3>选课成功</h3>
          <button class="modal-close" @click="closeSuccessModal">
            &times;
          </button>
        </div>
        <div class="modal-body">
          <p>您已成功选择{{ selectedCourses.length }}门课程！</p>
        </div>
        <div class="modal-footer">
          <button class="btn btn-primary" @click="closeSuccessModal">
            确定
          </button>
        </div>
      </div>
    </div>

    <!-- 确认弹窗 -->
    <div
      v-if="showConfirmModal"
      class="modal-overlay"
      @click.self="closeConfirmModal"
    >
      <div class="modal">
        <div class="modal-header">
          <h3>{{ confirmAction === "select" ? "确认选修" : "确认退选" }}</h3>
          <button class="modal-close" @click="closeConfirmModal">
            &times;
          </button>
        </div>
        <div class="modal-body">
          <p>{{ confirmMessage }}</p>
          <div class="course-preview" v-if="currentCourse">
            <h4>{{ currentCourse.courseName }}</h4>
            <p><strong>课程号：</strong>{{ currentCourse.courseId }}</p>
            <p><strong>班级：</strong>{{ currentCourse.className }}</p>
            <p><strong>教师：</strong>{{ currentCourse.teacherName }}</p>
            <p><strong>学分：</strong>{{ currentCourse.credit }}</p>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="closeConfirmModal">
            取消
          </button>
          <button
            class="btn btn-primary"
            @click="
              confirmAction === 'select'
                ? confirmSelectCourse()
                : confirmRemoveCourse()
            "
          >
            确认
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { courseApi, type Course } from "../../api/courseApi";
import {
  teachingClassApi,
  type TeachingClass,
} from "../../api/teachingClassApi";
import { electiveApi, type Elective } from "../../api/electiveApi";
import { teacherApi, type Teacher } from "../../api/teacherApi";
import { currentUser } from "../../api/auth";

// 定义类型
interface SelectableCourse {
  courseId: string;
  courseName: string;
  className: string;
  classId: string;
  teacherId: string;
  teacherName: string;
  courseType: string;
  credit: number;
  semester: string;
  availableSeats: number;
}

// 状态管理
const loading = ref(false);
const error = ref("");
const submitting = ref(false);
const selectedSemester = ref("");
const selectedCourseType = ref("");
const searchQuery = ref("");
const showSuccessModal = ref(false);
// 确认弹窗状态
const showConfirmModal = ref(false);
const confirmAction = ref<string>(""); // "select" 或 "remove"
const currentCourse = ref<SelectableCourse | null>(null);
const confirmMessage = ref("");

// 数据存储
const courses = ref<Course[]>([]);
const teachingClasses = ref<TeachingClass[]>([]);
const teachers = ref<Teacher[]>([]);
const userElectives = ref<Elective[]>([]);

// 可选课程和已选课程
const availableCourses = ref<SelectableCourse[]>([]);
const selectedCourses = ref<SelectableCourse[]>([]);

// 计算已选课程总学分
const totalCredits = computed(() => {
  return selectedCourses.value.reduce((sum, course) => sum + course.credit, 0);
});

// 过滤可选课程
const filteredCourses = computed(() => {
  let filtered = [...availableCourses.value];

  // 过滤掉已选课程
  const selectedClassIds = selectedCourses.value.map((c) => c.classId);
  filtered = filtered.filter(
    (course) => !selectedClassIds.includes(course.classId)
  );

  // 按学期过滤
  if (selectedSemester.value) {
    filtered = filtered.filter(
      (course) => course.semester === selectedSemester.value
    );
  }

  // 按课程类型过滤
  if (selectedCourseType.value) {
    filtered = filtered.filter(
      (course) => course.courseType === selectedCourseType.value
    );
  }

  // 按搜索关键词过滤
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    filtered = filtered.filter(
      (course) =>
        course.courseName.toLowerCase().includes(query) ||
        course.teacherName.toLowerCase().includes(query) ||
        course.courseId.toLowerCase().includes(query)
    );
  }

  return filtered;
});

// 获取所有课程数据
const fetchCourses = async () => {
  loading.value = true;
  error.value = "";

  try {
    // 获取当前学生的ID
    const studentId = currentUser.value?.userId || "1";

    // 1. 获取所有课程
    const courseData = await courseApi.getAllCourses();
    courses.value = courseData;
    console.log("课程数据:", courses.value);

    // 2. 获取所有教学班
    const teachingClassData = await teachingClassApi.getAllTeachingClasses();
    teachingClasses.value = teachingClassData;
    console.log("教学班数据:", teachingClasses.value);

    // 3. 获取所有教师信息
    const teacherData = await teacherApi.getAllTeachers();
    teachers.value = teacherData;
    console.log("教师数据:", teachers.value);

    // 4. 获取学生已选课程
    const electiveData = await electiveApi.getElectivesByStudentId(studentId);
    userElectives.value = electiveData;
    console.log("已选课程数据:", userElectives.value);

    // 构建可选课程列表
    console.log("开始构建可选课程列表...");
    buildAvailableCourses();
    console.log("构建完成，可选课程:", availableCourses.value);

    // 初始化已选课程列表
    console.log("开始初始化已选课程列表...");
    initializeSelectedCourses();
    console.log("初始化完成，已选课程:", selectedCourses.value);
  } catch (err: any) {
    error.value = err.response?.data?.message || "加载课程数据失败";
  } finally {
    loading.value = false;
  }
};

// 构建可选课程列表
const buildAvailableCourses = () => {
  console.log("开始构建可选课程列表...");
  console.log("教学班数量:", teachingClasses.value.length);
  console.log("课程数量:", courses.value.length);
  console.log("教师数量:", teachers.value.length);
  availableCourses.value = [];

  teachingClasses.value.forEach((teachingClass, index) => {
    console.log(`处理第${index + 1}个教学班:`, teachingClass);
    // 查找对应的课程
    const course = courses.value.find(
      (c) => c.courseId === teachingClass.courseId
    );
    console.log("找到的课程:", course);
    if (!course) {
      console.log(`未找到课程，跳过该教学班: ${teachingClass.classId}`);
      return;
    }

    // 查找对应的教师
    const teacher = teachers.value.find(
      (t) => t.teacherId === teachingClass.teacherId
    );
    console.log("找到的教师:", teacher);
    // 如果找不到教师，使用默认值
    if (!teacher) {
      console.log(`未找到教师，使用默认值: ${teachingClass.classId}`);
    }

    // 计算可用座位数（假设每个教学班有50个名额）
    const totalSeats = 50;
    const enrolledCount = userElectives.value.filter(
      (e) => e.classId === teachingClass.classId
    ).length;
    const availableSeats = totalSeats - enrolledCount;

    // 创建可选课程
    const selectableCourse: SelectableCourse = {
      courseId: course.courseId,
      courseName: course.courseName,
      className: teachingClass.className,
      classId: teachingClass.classId,
      teacherId: teacher ? teacher.teacherId : "",
      teacherName: teacher ? teacher.name : "未知教师",
      courseType: course.courseType,
      credit: course.credit,
      semester: teachingClass.semester,
      availableSeats: availableSeats,
    };

    availableCourses.value.push(selectableCourse);
    console.log("添加到可选课程列表:", selectableCourse);
  });
  console.log("构建完成，可选课程数量:", availableCourses.value.length);
};

// 初始化已选课程列表
const initializeSelectedCourses = () => {
  selectedCourses.value = [];

  // 将学生已选的课程添加到已选列表中
  userElectives.value.forEach((elective) => {
    const course = availableCourses.value.find(
      (c) => c.classId === elective.classId
    );
    if (course) {
      selectedCourses.value.push(course);
    }
  });
};

// 打开确认弹窗
const openConfirmModal = (action: string, course: SelectableCourse) => {
  confirmAction.value = action;
  currentCourse.value = course;
  if (action === "select") {
    confirmMessage.value = `您确定要选修课程《${course.courseName}》吗？`;
  } else {
    confirmMessage.value = `您确定要退选课程《${course.courseName}》吗？`;
  }
  showConfirmModal.value = true;
};

// 关闭确认弹窗
const closeConfirmModal = () => {
  showConfirmModal.value = false;
  currentCourse.value = null;
  confirmMessage.value = "";
  confirmAction.value = "";
};

// 确认选修课程
const confirmSelectCourse = async () => {
  if (currentCourse.value) {
    try {
      // 获取当前学生的ID
      const studentId = currentUser.value?.userId || "1";

      // 检查是否已选择该课程
      const isSelected = selectedCourses.value.some(
        (c) => c.classId === currentCourse.value!.classId
      );
      if (isSelected) {
        closeConfirmModal();
        return;
      }

      // 检查是否还有名额
      if (currentCourse.value.availableSeats <= 0) {
        closeConfirmModal();
        return;
      }

      // 立即向后端提交选修请求
      const electiveData = {
        studentId: studentId,
        classId: currentCourse.value.classId,
        status: "active",
      };
      await electiveApi.addElective(electiveData as Elective);

      // 刷新课程数据
      await fetchCourses();

      closeConfirmModal();
    } catch (err: any) {
      error.value = err.response?.data?.message || "选修课程失败";
      closeConfirmModal();
    }
  }
};

// 确认退选课程
const confirmRemoveCourse = async () => {
  if (currentCourse.value) {
    try {
      // 获取当前学生的ID
      const studentId = currentUser.value?.userId || "1";

      // 立即向后端提交退选请求
      await electiveApi.deleteElective(studentId, currentCourse.value.classId);

      // 从已选课程列表中移除
      const index = selectedCourses.value.findIndex(
        (c) => c.classId === currentCourse.value!.classId
      );
      if (index !== -1) {
        selectedCourses.value.splice(index, 1);
      }

      // 刷新可选课程列表（更新剩余名额）
      await fetchCourses();

      closeConfirmModal();
    } catch (err: any) {
      error.value = err.response?.data?.message || "退选课程失败";
      closeConfirmModal();
    }
  }
};

// 选择课程（打开确认弹窗）
const selectCourse = (course: SelectableCourse) => {
  openConfirmModal("select", course);
};

// 移除已选课程（打开确认弹窗）
const removeCourse = (course: SelectableCourse) => {
  openConfirmModal("remove", course);
};

// 提交选课
const submitSelection = async () => {
  if (selectedCourses.value.length === 0) return;

  submitting.value = true;

  try {
    // 获取当前学生的ID
    const studentId = currentUser.value?.userId || "1";

    // 1. 获取当前已选课程
    const currentElectives = await electiveApi.getElectivesByStudentId(
      studentId
    );
    const currentClassIds = currentElectives.map((e) => e.classId);

    // 2. 确定需要添加的课程
    const selectedClassIds = selectedCourses.value.map((c) => c.classId);
    const classesToAdd = selectedClassIds.filter(
      (id) => !currentClassIds.includes(id)
    );
    const classesToRemove = currentClassIds.filter(
      (id) => !selectedClassIds.includes(id)
    );

    // 3. 执行添加操作
    for (const classId of classesToAdd) {
      const electiveData: Partial<Elective> = {
        studentId,
        classId,
        status: "active",
      };
      await electiveApi.addElective(electiveData as Elective);
    }

    // 4. 执行删除操作
    for (const classId of classesToRemove) {
      await electiveApi.deleteElective(studentId, classId);
    }

    // 5. 显示成功模态框
    showSuccessModal.value = true;
  } catch (err: any) {
    error.value = err.response?.data?.message || "提交选课失败";
  } finally {
    submitting.value = false;
  }
};

// 关闭成功模态框
const closeSuccessModal = () => {
  showSuccessModal.value = false;
  // 刷新数据
  fetchCourses();
};

// 组件挂载时加载数据
onMounted(() => {
  fetchCourses();
});
</script>

<style scoped>
.course-selection-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.course-selection-header {
  margin-bottom: 20px;
}

.course-selection-header h2 {
  margin: 0 0 20px 0;
  font-size: 1.8rem;
  color: #333;
}

.filter-options {
  display: flex;
  gap: 16px;
  align-items: center;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-group label {
  font-weight: 500;
  color: #333;
}

.filter-group select,
.search-input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.search-input {
  width: 300px;
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
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

.error-message {
  padding: 20px;
  background-color: #f8d7da;
  border: 1px solid #f5c6cb;
  border-radius: 4px;
  color: #721c24;
  text-align: center;
  margin-bottom: 20px;
}

.retry-btn {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
  transition: background-color 0.3s ease;
}

.retry-btn:hover {
  background-color: #2980b9;
}

.course-selection-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.available-courses,
.selected-courses {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 20px;
}

.available-courses h3,
.selected-courses h3 {
  margin: 0 0 16px 0;
  font-size: 1.4rem;
  color: #333;
  border-bottom: 1px solid #ddd;
  padding-bottom: 10px;
}

.course-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 600px;
  overflow-y: auto;
}

.course-card {
  background-color: white;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.course-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.course-card.selected {
  border: 2px solid #3498db;
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.course-header h4 {
  margin: 0;
  font-size: 1.2rem;
  color: #333;
}

.credit {
  background-color: #3498db;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
}

.course-info p {
  margin: 6px 0;
  font-size: 0.9rem;
  color: #666;
}

.course-action {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.select-btn,
.remove-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.3s ease;
}

.select-btn {
  background-color: #27ae60;
  color: white;
}

.select-btn:hover:not(:disabled) {
  background-color: #229954;
}

.select-btn:disabled {
  background-color: #bdc3c7;
  cursor: not-allowed;
}

.remove-btn {
  background-color: #e74c3c;
  color: white;
}

.remove-btn:hover {
  background-color: #c0392b;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #999;
}

.selection-summary {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ddd;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.selection-summary p {
  margin: 0;
  font-size: 1rem;
  color: #333;
}

.submit-btn {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
  transition: background-color 0.3s ease;
  margin-top: 10px;
}

.submit-btn:hover:not(:disabled) {
  background-color: #2980b9;
}

.submit-btn:disabled {
  background-color: #bdc3c7;
  cursor: not-allowed;
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
  max-width: 400px;
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

.modal-footer {
  padding: 20px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s ease;
}

.btn-primary {
  background-color: #3498db;
  color: white;
}

.btn-primary:hover {
  background-color: #2980b9;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .course-selection-content {
    grid-template-columns: 1fr;
  }

  .filter-options {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-group {
    width: 100%;
  }

  .search-input {
    width: 100%;
  }
}
</style>
