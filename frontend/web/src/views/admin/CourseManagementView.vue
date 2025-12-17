<template>
  <div class="course-management-container">
    <div class="management-header">
      <h2>课程管理</h2>
      <button class="btn btn-primary" @click="openAddCourseModal">
        <span>➕</span> 添加课程
      </button>
    </div>

    <div class="search-section">
      <input
        v-model="searchQuery"
        type="text"
        placeholder="搜索课程名称或编号..."
        class="search-input"
      />
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载课程数据...</p>
    </div>
    <div v-else class="course-table-container">
      <table class="course-table">
        <thead>
          <tr>
            <th>课程编号</th>
            <th>课程名称</th>
            <th>专业代码</th>
            <th>学分</th>
            <th>课程类型</th>
            <th>学时</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="course in filteredCourses" :key="course.courseId">
            <td>{{ course.courseId }}</td>
            <td>{{ course.courseName }}</td>
            <td>{{ course.majorCode }}</td>
            <td>{{ course.credits }}</td>
            <td>{{ course.courseType }}</td>
            <td>{{ course.teachingHours }}</td>
            <td>
              <button
                class="btn btn-sm btn-edit"
                @click="openEditCourseModal(course)"
              >
                编辑
              </button>
              <button
                class="btn btn-sm btn-delete"
                @click="deleteCourse(course.courseId)"
              >
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="filteredCourses.length === 0" class="empty-state">
        <p>暂无课程数据</p>
      </div>
    </div>

    <!-- 添加/编辑课程模态框 -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ isEditMode ? "编辑课程" : "添加课程" }}</h3>
          <button class="modal-close" @click="closeModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveCourse">
            <div class="form-group">
              <label>课程编号</label>
              <input
                v-model="formData.courseId"
                type="text"
                class="form-control"
                :disabled="isEditMode"
                required
              />
            </div>

            <div class="form-group">
              <label>专业代码</label>
              <input
                v-model="formData.majorCode"
                type="text"
                class="form-control"
                required
              />
            </div>

            <div class="form-group">
              <label>课程名称</label>
              <input
                v-model="formData.courseName"
                type="text"
                class="form-control"
                required
              />
            </div>

            <div class="form-group">
              <label>学分</label>
              <input
                v-model.number="formData.credits"
                type="number"
                class="form-control"
                min="0"
                step="0.5"
                required
              />
            </div>

            <div class="form-group">
              <label>课程类型</label>
              <select v-model="formData.courseType" class="form-control">
                <option value="必修课">必修课</option>
                <option value="选修课">选修课</option>
                <option value="通识课">通识课</option>
              </select>
            </div>

            <div class="form-group">
              <label>学时</label>
              <input
                v-model.number="formData.teachingHours"
                type="number"
                class="form-control"
                min="0"
                required
              />
            </div>

            <div class="form-group">
              <label>课程描述</label>
              <textarea
                v-model="formData.description"
                class="form-control"
                rows="3"
              ></textarea>
            </div>

            <div class="form-actions">
              <button
                type="button"
                class="btn btn-secondary"
                @click="closeModal"
              >
                取消
              </button>
              <button type="submit" class="btn btn-primary">
                {{ isEditMode ? "保存修改" : "添加课程" }}
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
import { courseApi, type Course } from "../../api/courseApi";

// 数据
const courses = ref<Course[]>([]);
const searchQuery = ref("");
const loading = ref(true);

// 模态框
const showModal = ref(false);
const isEditMode = ref(false);
const formData = ref<Course>({
  courseId: "",
  majorCode: "",
  courseName: "",
  credits: 0,
  courseType: "必修课",
  teachingHours: 0,
  description: "",
});

// 获取所有课程
const fetchCourses = async () => {
  try {
    loading.value = true;
    const data = await courseApi.getAllCourses();
    courses.value = data;
  } catch (error) {
    console.error("获取课程数据失败:", error);
  } finally {
    loading.value = false;
  }
};

// 筛选课程
const filteredCourses = computed(() => {
  if (!searchQuery.value) {
    return courses.value;
  }
  const query = searchQuery.value.toLowerCase();
  return courses.value.filter((course) => {
    return (
      course.courseId.toLowerCase().includes(query) ||
      course.courseName.toLowerCase().includes(query)
    );
  });
});

// 打开添加课程模态框
const openAddCourseModal = () => {
  isEditMode.value = false;
  formData.value = {
    courseId: "",
    majorCode: "",
    courseName: "",
    credits: 0,
    courseType: "必修课",
    teachingHours: 0,
    description: "",
  };
  showModal.value = true;
};

// 打开编辑课程模态框
const openEditCourseModal = (course: Course) => {
  isEditMode.value = true;
  formData.value = { ...course };
  showModal.value = true;
};

// 关闭模态框
const closeModal = () => {
  showModal.value = false;
};

// 保存课程
const saveCourse = async () => {
  try {
    if (isEditMode.value) {
      // 更新课程
      await courseApi.updateCourse(formData.value.courseId, formData.value);
    } else {
      // 添加课程
      await courseApi.addCourse(formData.value);
    }
    closeModal();
    await fetchCourses();
  } catch (error) {
    console.error("保存课程数据失败:", error);
  }
};

// 删除课程
const deleteCourse = async (courseId: string) => {
  if (confirm("确定要删除这个课程吗？")) {
    try {
      await courseApi.deleteCourse(courseId);
      await fetchCourses();
    } catch (error) {
      console.error("删除课程失败:", error);
    }
  }
};

// 初始化数据
onMounted(async () => {
  await fetchCourses();
});
</script>

<style scoped>
.course-management-container {
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
  background-color: #95a5a6;
  color: white;
}

.btn-secondary:hover {
  background-color: #7f8c8d;
}

.btn-edit {
  background-color: #3498db;
  color: white;
  padding: 6px 12px;
  margin-right: 8px;
}

.btn-edit:hover {
  background-color: #2980b9;
}

.btn-delete {
  background-color: #e74c3c;
  color: white;
  padding: 6px 12px;
}

.btn-delete:hover {
  background-color: #c0392b;
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

.course-table-container {
  overflow-x: auto;
}

.course-table {
  width: 100%;
  border-collapse: collapse;
}

.course-table th,
.course-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.course-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.course-table tr:hover {
  background-color: #f5f5f5;
}

.empty-state {
  text-align: center;
  padding: 50px 0;
  color: #666;
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
  max-width: 600px;
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
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: #333;
}

.form-control {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 24px;
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
