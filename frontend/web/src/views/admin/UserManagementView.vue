<template>
  <div class="user-management-container">
    <div class="management-header">
      <h2>用户管理</h2>
      <button class="btn btn-primary" @click="openAddUserModal">
        <span>➕</span> 添加用户
      </button>
    </div>

    <div class="filter-section">
      <select v-model="filterRole" class="filter-select">
        <option value="">所有角色</option>
        <option value="student">学生</option>
        <option value="teacher">教师</option>
        <option value="admin">管理员</option>
      </select>
      <input
        v-model="searchQuery"
        type="text"
        placeholder="搜索用户名或姓名..."
        class="search-input"
      />
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载用户数据...</p>
    </div>

    <!-- 用户表格 -->
    <div v-else class="user-table-container">
      <table class="user-table">
        <thead>
          <tr>
            <th>用户ID</th>
            <th>姓名</th>
            <th>角色</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in filteredUsers" :key="user.userId">
            <td>{{ user.userId }}</td>
            <td>{{ user.name || "-" }}</td>
            <td>{{ getRoleName(user.role) }}</td>
            <td>
              <span
                :class="[
                  'status-badge',
                  user.status === 1 ? 'status-active' : 'status-inactive',
                ]"
              >
                {{ user.status === 1 ? "正常" : "禁用" }}
              </span>
            </td>
            <td>{{ formatDate(user.createTime) }}</td>
            <td>
              <button
                class="btn btn-sm btn-edit"
                @click="openEditUserModal(user)"
              >
                编辑
              </button>
              <button
                class="btn btn-sm btn-delete"
                @click="deleteUser(user.userId, user.role)"
              >
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="filteredUsers.length === 0" class="empty-state">
        <p>暂无用户数据</p>
      </div>
    </div>

    <!-- 添加/编辑用户模态框 -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ isEditMode ? "编辑用户" : "添加用户" }}</h3>
          <button class="modal-close" @click="closeModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveUser">
            <div class="form-group">
              <label>角色</label>
              <select v-model="formData.role" class="form-control">
                <option value="student">学生</option>
                <option value="teacher">教师</option>
                <option value="admin">管理员</option>
              </select>
            </div>

            <div class="form-group">
              <label>用户ID</label>
              <input
                v-model="formData.userId"
                type="text"
                class="form-control"
                :disabled="isEditMode"
                required
              />
            </div>

            <div v-if="!isEditMode" class="form-group">
              <label>密码</label>
              <input
                v-model="formData.password"
                type="password"
                class="form-control"
                required
              />
            </div>

            <div class="form-group">
              <label>姓名</label>
              <input
                v-model="formData.name"
                type="text"
                class="form-control"
                required
              />
            </div>

            <div class="form-group">
              <label>状态</label>
              <select v-model="formData.status" class="form-control">
                <option value="1">正常</option>
                <option value="0">禁用</option>
              </select>
            </div>

            <!-- 学生特有字段 -->
            <div
              v-if="formData.role === 'student'"
              class="role-specific-fields"
            >
              <h4>学生信息</h4>
              <div class="form-group">
                <label>学号</label>
                <input
                  v-model="formData.studentId"
                  type="text"
                  class="form-control"
                  :disabled="isEditMode"
                  required
                />
              </div>
              <div class="form-group">
                <label>性别</label>
                <select v-model="formData.gender" class="form-control">
                  <option value="男">男</option>
                  <option value="女">女</option>
                </select>
              </div>
              <div class="form-group">
                <label>出生日期</label>
                <input
                  v-model="formData.birthDate"
                  type="date"
                  class="form-control"
                  required
                />
              </div>
              <div class="form-group">
                <label>联系电话</label>
                <input
                  v-model="formData.phone"
                  type="text"
                  class="form-control"
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
            </div>

            <!-- 教师特有字段 -->
            <div
              v-if="formData.role === 'teacher'"
              class="role-specific-fields"
            >
              <h4>教师信息</h4>
              <div class="form-group">
                <label>工号</label>
                <input
                  v-model="formData.teacherId"
                  type="text"
                  class="form-control"
                  :disabled="isEditMode"
                  required
                />
              </div>
              <div class="form-group">
                <label>职称</label>
                <input
                  v-model="formData.title"
                  type="text"
                  class="form-control"
                />
              </div>
              <div class="form-group">
                <label>办公室</label>
                <input
                  v-model="formData.office"
                  type="text"
                  class="form-control"
                />
              </div>
              <div class="form-group">
                <label>联系电话</label>
                <input
                  v-model="formData.phone"
                  type="text"
                  class="form-control"
                />
              </div>
              <div class="form-group">
                <label>职务</label>
                <input
                  v-model="formData.duty"
                  type="text"
                  class="form-control"
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
                {{ isEditMode ? "保存修改" : "添加用户" }}
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
import { useRouter } from "vue-router";
import { userAccountApi } from "../../api/userAccountApi";
import {
  studentApi,
  type Student,
  type StudentDto,
} from "../../api/studentApi";
import {
  teacherApi,
  type Teacher,
  type TeacherDto,
} from "../../api/teacherApi";

// 路由实例
const router = useRouter();

// 数据
const users = ref<any[]>([]);
const students = ref<Student[]>([]);
const teachers = ref<Teacher[]>([]);
// 加载状态
const loading = ref(true);

// 筛选条件
const filterRole = ref("");
const searchQuery = ref("");

// 模态框
const showModal = ref(false);
const isEditMode = ref(false);
const formData = ref({
  userId: "",
  password: "",
  role: "student",
  status: "1",
  name: "",
  // 学生字段
  studentId: "",
  gender: "男",
  birthDate: "",
  phone: "",
  majorCode: "",
  // 教师字段
  teacherId: "",
  title: "",
  office: "",
  duty: "",
});

// 格式化日期
const formatDate = (dateString: string) => {
  if (!dateString) return "";
  const date = new Date(dateString);
  return date.toLocaleString();
};

// 获取角色名称
const getRoleName = (role: string) => {
  const roleMap: Record<string, string> = {
    student: "学生",
    teacher: "教师",
    admin: "管理员",
  };
  return roleMap[role] || role;
};

// 合并用户数据
const mergeUserData = async () => {
  try {
    loading.value = true;
    // 获取所有用户账户
    const userAccounts = await userAccountApi.getAllUserAccounts();
    // 获取所有学生
    const allStudents = await studentApi.getAllStudents();
    // 获取所有教师
    const allTeachers = await teacherApi.getAllTeachers();

    // 存储学生和教师数据
    students.value = allStudents;
    teachers.value = allTeachers;

    // 创建学生和教师的映射
    const studentMap = new Map(allStudents.map((s) => [s.userId, s]));
    const teacherMap = new Map(allTeachers.map((t) => [t.userId, t]));

    // 合并用户数据
    users.value = userAccounts.map((user) => {
      let name = "";
      if (user.role === "student") {
        const student = studentMap.get(user.userId);
        name = student?.name || "";
      } else if (user.role === "teacher") {
        const teacher = teacherMap.get(user.userId);
        name = teacher?.name || "";
      }
      return { ...user, name };
    });
  } catch (error) {
    console.error("获取用户数据失败:", error);
  } finally {
    loading.value = false;
  }
};

// 筛选用户
const filteredUsers = computed(() => {
  return users.value.filter((user) => {
    // 角色筛选
    if (filterRole.value && user.role !== filterRole.value) {
      return false;
    }
    // 搜索筛选
    if (searchQuery.value) {
      const query = searchQuery.value.toLowerCase();
      return (
        user.userId.toLowerCase().includes(query) ||
        (user.name && user.name.toLowerCase().includes(query))
      );
    }
    return true;
  });
});

// 打开添加用户模态框
const openAddUserModal = () => {
  isEditMode.value = false;
  formData.value = {
    userId: "",
    password: "",
    role: "student",
    status: "1",
    name: "",
    studentId: "",
    gender: "男",
    birthDate: "",
    phone: "",
    majorCode: "",
    teacherId: "",
    title: "",
    office: "",
    duty: "",
  };
  showModal.value = true;
};

// 打开编辑用户模态框
const openEditUserModal = (user: any) => {
  isEditMode.value = true;
  formData.value = {
    userId: user.userId,
    password: "",
    role: user.role,
    status: user.status.toString(),
    name: user.name,
    studentId: "",
    gender: "男",
    birthDate: "",
    phone: "",
    majorCode: "",
    teacherId: "",
    title: "",
    office: "",
    duty: "",
  };

  // 如果是学生，填充学生特有字段
  if (user.role === "student") {
    const student = students.value.find((s) => s.userId === user.userId);
    if (student) {
      formData.value.studentId = student.studentId;
      formData.value.gender = student.gender;
      formData.value.birthDate = student.birthDate
        ? new Date(student.birthDate).toISOString().split("T")[0]
        : "";
      formData.value.phone = student.phone;
      formData.value.majorCode = student.majorCode;
    }
  }

  // 如果是教师，填充教师特有字段
  if (user.role === "teacher") {
    const teacher = teachers.value.find((t) => t.userId === user.userId);
    if (teacher) {
      formData.value.teacherId = teacher.teacherId;
      formData.value.title = teacher.title;
      formData.value.office = teacher.office;
      formData.value.phone = teacher.phone;
      formData.value.duty = teacher.duty;
      formData.value.majorCode = teacher.majorCode;
    }
  }

  showModal.value = true;
};

// 关闭模态框
const closeModal = () => {
  showModal.value = false;
};

// 保存用户
const saveUser = async () => {
  try {
    if (isEditMode.value) {
      // 更新用户
      await updateUser();
    } else {
      // 添加新用户
      await addUser();
    }
    closeModal();
    await mergeUserData();
  } catch (error) {
    console.error("保存用户失败:", error);
  }
};

// 添加用户
const addUser = async () => {
  // 创建用户账户
  await userAccountApi.addUserAccount({
    userId: formData.value.userId,
    password: formData.value.password,
    role: formData.value.role,
    status: parseInt(formData.value.status),
    name: formData.value.name,
    email: "",
    phone: formData.value.phone,
  });

  // 根据角色创建对应的实体
  if (formData.value.role === "student") {
    const studentDto: StudentDto = {
      studentId: formData.value.studentId,
      name: formData.value.name,
      gender: formData.value.gender,
      birthDate: new Date(formData.value.birthDate),
      phone: formData.value.phone,
      majorCode: formData.value.majorCode,
      userId: formData.value.userId,
      password: formData.value.password,
      role: "student",
      status: parseInt(formData.value.status),
    };
    await studentApi.addStudent(studentDto);
  } else if (formData.value.role === "teacher") {
    const teacherDto: TeacherDto = {
      teacherId: formData.value.teacherId,
      name: formData.value.name,
      title: formData.value.title,
      office: formData.value.office,
      phone: formData.value.phone,
      duty: formData.value.duty,
      majorCode: formData.value.majorCode,
      userId: formData.value.userId,
      password: formData.value.password,
      status: parseInt(formData.value.status),
    };
    await teacherApi.addTeacher(teacherDto);
  }
};

// 更新用户
const updateUser = async () => {
  // 更新用户账户
  await userAccountApi.updateUserAccount(formData.value.userId, {
    userId: formData.value.userId,
    password: formData.value.password || "",
    role: formData.value.role,
    status: parseInt(formData.value.status),
    name: formData.value.name,
    email: "",
    phone: formData.value.phone,
  });

  // 根据角色更新对应的实体
  if (formData.value.role === "student") {
    const studentDto: StudentDto = {
      studentId: formData.value.studentId,
      name: formData.value.name,
      gender: formData.value.gender,
      birthDate: new Date(formData.value.birthDate),
      phone: formData.value.phone,
      majorCode: formData.value.majorCode,
      userId: formData.value.userId,
      password: formData.value.password || "",
      role: "student",
      status: parseInt(formData.value.status),
    };
    await studentApi.updateStudent(formData.value.studentId, studentDto);
  } else if (formData.value.role === "teacher") {
    const teacherDto: TeacherDto = {
      teacherId: formData.value.teacherId,
      name: formData.value.name,
      title: formData.value.title,
      office: formData.value.office,
      phone: formData.value.phone,
      duty: formData.value.duty,
      majorCode: formData.value.majorCode,
      userId: formData.value.userId,
      password: formData.value.password || "",
      status: parseInt(formData.value.status),
    };
    await teacherApi.updateTeacher(formData.value.teacherId, teacherDto);
  }
};

// 删除用户
const deleteUser = async (userId: string, role: string) => {
  if (confirm("确定要删除这个用户吗？")) {
    try {
      // 根据角色删除对应的实体
      if (role === "student") {
        const student = students.value.find((s) => s.userId === userId);
        if (student) {
          await studentApi.deleteStudent(student.studentId);
        }
      } else if (role === "teacher") {
        const teacher = teachers.value.find((t) => t.userId === userId);
        if (teacher) {
          await teacherApi.deleteTeacher(teacher.teacherId);
        }
      } else if (role === "admin") {
        // 直接删除管理员账户
        await userAccountApi.deleteUserAccount(userId);
      }
      // 重新加载用户数据
      await mergeUserData();
    } catch (error) {
      console.error("删除用户失败:", error);
    }
  }
};

// 初始化数据
onMounted(async () => {
  await mergeUserData();
});
</script>

<style scoped>
.user-management-container {
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

.filter-section {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.filter-select,
.search-input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
}

.filter-select {
  min-width: 120px;
}

.search-input {
  flex: 1;
  max-width: 400px;
}

.user-table-container {
  overflow-x: auto;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
}

.user-table th,
.user-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.user-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.user-table tr:hover {
  background-color: #f5f5f5;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 500;
}

.status-active {
  background-color: #d4edda;
  color: #155724;
}

.status-inactive {
  background-color: #f8d7da;
  color: #721c24;
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
  max-height: 80vh;
  overflow-y: auto;
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

.role-specific-fields {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.role-specific-fields h4 {
  margin: 0 0 16px 0;
  color: #333;
  font-size: 1.1rem;
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
