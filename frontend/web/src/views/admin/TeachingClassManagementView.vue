<template>
  <div class="teaching-class-management-container">
    <div class="management-header">
      <h2>教学班管理</h2>
      <button @click="openAddModal" class="btn btn-primary">
        <i class="icon-plus"></i> 添加教学班
      </button>
    </div>

    <div class="search-bar">
      <input
        v-model="searchKeyword"
        placeholder="搜索班级名称..."
        class="search-input"
      />
    </div>

    <div class="teaching-class-list">
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>正在加载教学班数据...</p>
      </div>
      <template v-else>
        <table class="data-table">
          <thead>
            <tr>
              <th>班级ID</th>
              <th>班级名称</th>
              <th>学期</th>
              <th>课程</th>
              <th>教师</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="teachingClass in filteredTeachingClasses"
              :key="teachingClass.classId"
            >
              <td>{{ teachingClass.classId }}</td>
              <td>{{ teachingClass.className }}</td>
              <td>{{ teachingClass.semester }}</td>
              <td>{{ getCourseName(teachingClass.courseId) }}</td>
              <td>{{ getTeacherName(teachingClass.teacherId) }}</td>
              <td class="action-buttons">
                <button
                  @click="editTeachingClass(teachingClass)"
                  class="edit-btn"
                >
                  <i class="icon-edit"></i> 编辑
                </button>
                <button
                  @click="deleteTeachingClass(teachingClass.classId)"
                  class="delete-btn"
                >
                  <i class="icon-delete"></i> 删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-if="filteredTeachingClasses.length === 0" class="empty-state">
          <p>暂无教学班数据</p>
        </div>
      </template>
    </div>

    <!-- 添加/编辑教学班模态框 -->
    <div v-if="showModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEditing ? "编辑教学班" : "添加教学班" }}</h3>
          <button @click="closeModal" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveTeachingClass">
            <div class="form-group">
              <label for="classId">班级ID</label>
              <input
                id="classId"
                v-model="formData.classId"
                type="text"
                placeholder="请输入班级ID"
                :disabled="isEditing"
                required
              />
            </div>

            <div class="form-group">
              <label for="className">班级名称</label>
              <input
                id="className"
                v-model="formData.className"
                type="text"
                placeholder="请输入班级名称"
                required
              />
            </div>

            <div class="form-group">
              <label for="courseId">课程</label>
              <select id="courseId" v-model="formData.courseId" required>
                <option value="">请选择课程</option>
                <option
                  v-for="course in courses"
                  :key="course.courseId"
                  :value="course.courseId"
                >
                  {{ course.courseName }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label for="teacherId">教师</label>
              <select id="teacherId" v-model="formData.teacherId" required>
                <option value="">请选择教师</option>
                <option
                  v-for="teacher in teachers"
                  :key="teacher.teacherId"
                  :value="teacher.teacherId"
                >
                  {{ teacher.name }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label for="semester">学期</label>
              <input
                id="semester"
                v-model="formData.semester"
                type="text"
                placeholder="请输入学期（如：2023-2024学年第一学期）"
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
import { teachingClassApi, TeachingClass } from "../../api/teachingClassApi";
import { courseApi } from "../../api/courseApi";
import { teacherApi } from "../../api/teacherApi";

// 响应式数据
const loading = ref(true);
const teachingClasses = ref<TeachingClass[]>([]);
const courses = ref<any[]>([]);
const teachers = ref<any[]>([]);
const searchKeyword = ref("");
const showModal = ref(false);
const isEditing = ref(false);
const formData = ref<TeachingClass>({
  classId: "",
  courseId: "",
  teacherId: "",
  className: "",
  semester: "",
});

// 计算属性：筛选后的教学班列表
const filteredTeachingClasses = computed(() => {
  if (!searchKeyword.value) return teachingClasses.value;
  const keyword = searchKeyword.value.toLowerCase();
  return teachingClasses.value.filter((teachingClass) =>
    teachingClass.className.toLowerCase().includes(keyword)
  );
});

// 获取课程名称
const getCourseName = (courseId: string): string => {
  const course = courses.value.find((c) => c.courseId === courseId);
  return course ? course.courseName : "未知课程";
};

// 获取教师名称
const getTeacherName = (teacherId: string): string => {
  const teacher = teachers.value.find((t) => t.teacherId === teacherId);
  return teacher ? teacher.name : "未知教师";
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

// 加载所有课程
const fetchCourses = async () => {
  try {
    const data = await courseApi.getAllCourses();
    courses.value = data;
  } catch (error) {
    console.error("获取课程数据失败:", error);
  }
};

// 加载所有教师
const fetchTeachers = async () => {
  try {
    const data = await teacherApi.getAllTeachers();
    teachers.value = data;
  } catch (error) {
    console.error("获取教师数据失败:", error);
  }
};

// 打开添加模态框
const openAddModal = () => {
  isEditing.value = false;
  formData.value = {
    classId: "",
    courseId: "",
    teacherId: "",
    className: "",
    semester: "",
  };
  showModal.value = true;
};

// 打开编辑模态框
const editTeachingClass = (teachingClass: TeachingClass) => {
  isEditing.value = true;
  formData.value = { ...teachingClass };
  showModal.value = true;
};

// 关闭模态框
const closeModal = () => {
  showModal.value = false;
};

// 保存教学班
const saveTeachingClass = async () => {
  try {
    if (isEditing.value) {
      await teachingClassApi.updateTeachingClass(
        formData.value.classId,
        formData.value
      );
    } else {
      await teachingClassApi.addTeachingClass(formData.value);
    }
    await fetchTeachingClasses();
    closeModal();
  } catch (error) {
    console.error(
      isEditing.value ? "更新教学班失败:" : "添加教学班失败:",
      error
    );
  }
};

// 删除教学班
const deleteTeachingClass = async (classId: string) => {
  if (confirm("确定要删除这个教学班吗？")) {
    try {
      await teachingClassApi.deleteTeachingClass(classId);
      await fetchTeachingClasses();
    } catch (error) {
      console.error("删除教学班失败:", error);
    }
  }
};

// 组件挂载时加载数据
onMounted(async () => {
  try {
    loading.value = true;
    await Promise.all([
      fetchTeachingClasses(),
      fetchCourses(),
      fetchTeachers(),
    ]);
  } catch (error) {
    console.error("加载教学班数据失败:", error);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.teaching-class-management-container {
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
