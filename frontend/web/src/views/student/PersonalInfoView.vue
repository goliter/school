<template>
  <div class="personal-info-container">
    <div class="management-header">
      <h2>个人信息</h2>
      <button class="btn btn-primary" @click="openEditModal">
        <span>✏️</span> 编辑信息
      </button>
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载个人信息...</p>
    </div>
    <div v-else-if="student" class="personal-info-content">
      <div class="info-section">
        <h3>基本信息</h3>
        <div class="info-grid">
          <div class="info-item">
            <label>学号</label>
            <span>{{ student.studentId }}</span>
          </div>
          <div class="info-item">
            <label>姓名</label>
            <span>{{ student.name }}</span>
          </div>
          <div class="info-item">
            <label>性别</label>
            <span>{{ student.gender }}</span>
          </div>
          <div class="info-item">
            <label>出生日期</label>
            <span>{{ formatDate(student.birthDate) }}</span>
          </div>
          <div class="info-item">
            <label>联系电话</label>
            <span>{{ student.phone }}</span>
          </div>
        </div>
      </div>

      <div class="info-section">
        <h3>所属信息</h3>
        <div class="info-grid">
          <div class="info-item">
            <label>专业代码</label>
            <span>{{ student.majorCode }}</span>
          </div>
          <div class="info-item">
            <label>专业名称</label>
            <span>{{ currentMajor?.majorName || "未找到专业信息" }}</span>
          </div>
          <div class="info-item">
            <label>学院代码</label>
            <span>{{ currentMajor?.collegeId || "未找到学院信息" }}</span>
          </div>
          <div class="info-item">
            <label>学院名称</label>
            <span>{{ currentCollege?.collegeName || "未找到学院信息" }}</span>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="error-state">
      <p>加载个人信息失败，请稍后重试</p>
      <button class="btn btn-primary" @click="fetchStudentInfo">
        重新加载
      </button>
    </div>

    <!-- 编辑信息模态框 -->
    <div
      v-if="showEditModal"
      class="modal-overlay"
      @click.self="closeEditModal"
    >
      <div class="modal">
        <div class="modal-header">
          <h3>编辑个人信息</h3>
          <button class="modal-close" @click="closeEditModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveEdit">
            <div class="form-group">
              <label>学号</label>
              <input
                v-model="editForm.studentId"
                type="text"
                class="form-control"
                disabled
              />
            </div>

            <div class="form-group">
              <label>姓名</label>
              <input
                v-model="editForm.name"
                type="text"
                class="form-control"
                required
              />
            </div>

            <div class="form-group">
              <label>性别</label>
              <select v-model="editForm.gender" class="form-control" required>
                <option value="男">男</option>
                <option value="女">女</option>
              </select>
            </div>

            <div class="form-group">
              <label>出生日期</label>
              <input
                v-model="editForm.birthDate"
                type="date"
                class="form-control"
                required
              />
            </div>

            <div class="form-group">
              <label>联系电话</label>
              <input
                v-model="editForm.phone"
                type="text"
                class="form-control"
              />
            </div>

            <div class="form-group">
              <label>密码（留空不修改）</label>
              <input
                v-model="editForm.password"
                type="password"
                class="form-control"
              />
            </div>

            <div class="form-actions">
              <button
                type="button"
                class="btn btn-secondary"
                @click="closeEditModal"
              >
                取消
              </button>
              <button type="submit" class="btn btn-primary">保存修改</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { studentApi, type Student, type StudentDto } from "../../api/studentApi";
import { majorApi, type Major } from "../../api/majorApi";
import { collegeApi, type College } from "../../api/collegeApi";
import { userAccountApi } from "../../api/userAccountApi";
import { currentUser } from "../../api/auth";

// 数据
const student = ref<Student | null>(null);
const majors = ref<Major[]>([]);
const colleges = ref<College[]>([]);
const loading = ref(true);

// 编辑模态框
const showEditModal = ref(false);
const editForm = ref<Student & { password?: string }>({
  studentId: "",
  name: "",
  gender: "男",
  birthDate: new Date(),
  phone: "",
  majorCode: "",
  userId: "",
  password: "",
});

// 获取学生信息
const fetchStudentInfo = async () => {
  try {
    loading.value = true;
    const userId = currentUser.value?.userId;
    if (!userId) {
      console.error("未找到当前登录用户ID");
      return;
    }

    // 先获取所有学生，然后根据userId筛选
    const allStudents = await studentApi.getAllStudents();
    const currentStudent = allStudents.find((s) => s.userId === userId);

    if (currentStudent) {
      student.value = currentStudent;
    } else {
      console.error("未找到对应的学生信息");
    }
  } catch (error) {
    console.error("获取学生信息失败:", error);
  } finally {
    loading.value = false;
  }
};

// 获取所有专业
const fetchMajors = async () => {
  try {
    const data = await majorApi.getAllMajors();
    majors.value = data;
  } catch (error) {
    console.error("获取专业数据失败:", error);
  }
};

// 获取所有学院
const fetchColleges = async () => {
  try {
    const data = await collegeApi.getAllColleges();
    colleges.value = data;
  } catch (error) {
    console.error("获取学院数据失败:", error);
  }
};

// 计算当前学生的专业信息
const currentMajor = computed(() => {
  if (student.value) {
    return majors.value.find(
      (major) => major.majorCode === student.value?.majorCode
    );
  }
  return undefined;
});

// 计算当前学生的学院信息
const currentCollege = computed(() => {
  if (currentMajor.value) {
    return colleges.value.find(
      (college) => college.collegeId === currentMajor.value?.collegeId
    );
  }
  return undefined;
});

// 格式化日期
const formatDate = (date: Date | string) => {
  const d = new Date(date);
  return d.toISOString().split('T')[0];
};

// 打开编辑模态框
const openEditModal = () => {
  if (student.value) {
    editForm.value = {
      ...student.value,
      password: currentUser.value?.password || "",
    };
    showEditModal.value = true;
  }
};

// 关闭编辑模态框
const closeEditModal = () => {
  showEditModal.value = false;
};

// 保存编辑
const saveEdit = async () => {
  try {
    // 更新学生信息，直接使用表单中的密码
    const studentData = {
      studentId: editForm.value.studentId,
      name: editForm.value.name,
      gender: editForm.value.gender,
      birthDate: editForm.value.birthDate,
      phone: editForm.value.phone,
      majorCode: editForm.value.majorCode,
      userId: currentUser.value?.userId || editForm.value.userId || "",
      password: editForm.value.password,
      role: "student",
      status: currentUser.value?.status || 1,
    };
    await studentApi.updateStudent(editForm.value.studentId, studentData as StudentDto);

    await fetchStudentInfo();
    closeEditModal();
    alert("个人信息更新成功！");
  } catch (error) {
    console.error("更新个人信息失败:", error);
    alert("更新个人信息失败，请稍后重试。");
  }
};

// 初始化数据
onMounted(async () => {
  await Promise.all([fetchStudentInfo(), fetchMajors(), fetchColleges()]);
});
</script>

<style scoped>
.personal-info-container {
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

.error-state {
  text-align: center;
  padding: 50px 0;
  color: #e74c3c;
}

.personal-info-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.info-section {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
}

.info-section h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #333;
  font-size: 1.2rem;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 15px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.info-item label {
  font-weight: 600;
  color: #555;
  font-size: 0.9rem;
}

.info-item span {
  color: #333;
  font-size: 1rem;
  padding: 8px;
  background-color: white;
  border-radius: 4px;
  border: 1px solid #eee;
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

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>