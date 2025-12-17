<template>
  <div class="college-management-container">
    <div class="management-header">
      <h2>学院管理</h2>
      <button class="btn btn-primary" @click="openAddCollegeModal">
        <span>➕</span> 添加学院
      </button>
    </div>

    <div class="search-section">
      <input
        v-model="searchQuery"
        type="text"
        placeholder="搜索学院名称或编号..."
        class="search-input"
      />
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载学院数据...</p>
    </div>
    <div v-else class="college-table-container">
      <table class="college-table">
        <thead>
          <tr>
            <th>学院编号</th>
            <th>学院名称</th>
            <th>地址</th>
            <th>联系电话</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="college in filteredColleges" :key="college.collegeId">
            <td>{{ college.collegeId }}</td>
            <td>{{ college.collegeName }}</td>
            <td>{{ college.address || "-" }}</td>
            <td>{{ college.phone || "-" }}</td>
            <td>
              <button
                class="btn btn-sm btn-edit"
                @click="openEditCollegeModal(college)"
              >
                编辑
              </button>
              <button
                class="btn btn-sm btn-delete"
                @click="deleteCollege(college.collegeId)"
              >
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="filteredColleges.length === 0" class="empty-state">
        <p>暂无学院数据</p>
      </div>
    </div>

    <!-- 添加/编辑学院模态框 -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ isEditMode ? "编辑学院" : "添加学院" }}</h3>
          <button class="modal-close" @click="closeModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveCollege">
            <div class="form-group">
              <label>学院编号</label>
              <input
                v-model="formData.collegeId"
                type="text"
                class="form-control"
                :disabled="isEditMode"
                required
              />
            </div>

            <div class="form-group">
              <label>学院名称</label>
              <input
                v-model="formData.collegeName"
                type="text"
                class="form-control"
                required
              />
            </div>

            <div class="form-group">
              <label>地址</label>
              <input
                v-model="formData.address"
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

            <div class="form-actions">
              <button
                type="button"
                class="btn btn-secondary"
                @click="closeModal"
              >
                取消
              </button>
              <button type="submit" class="btn btn-primary">
                {{ isEditMode ? "保存修改" : "添加学院" }}
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
import { collegeApi, type College } from "../../api/collegeApi";

// 数据
const colleges = ref<College[]>([]);
const searchQuery = ref("");
const loading = ref(true);

// 模态框
const showModal = ref(false);
const isEditMode = ref(false);
const formData = ref<College>({
  collegeId: "",
  collegeName: "",
  address: "",
  phone: "",
});

// 获取所有学院
const fetchColleges = async () => {
  try {
    loading.value = true;
    const data = await collegeApi.getAllColleges();
    colleges.value = data;
  } catch (error) {
    console.error("获取学院数据失败:", error);
  } finally {
    loading.value = false;
  }
};

// 筛选学院
const filteredColleges = computed(() => {
  if (!searchQuery.value) {
    return colleges.value;
  }
  const query = searchQuery.value.toLowerCase();
  return colleges.value.filter((college) => {
    return (
      college.collegeId.toLowerCase().includes(query) ||
      college.collegeName.toLowerCase().includes(query)
    );
  });
});

// 打开添加学院模态框
const openAddCollegeModal = () => {
  isEditMode.value = false;
  formData.value = {
    collegeId: "",
    collegeName: "",
    address: "",
    phone: "",
  };
  showModal.value = true;
};

// 打开编辑学院模态框
const openEditCollegeModal = (college: College) => {
  isEditMode.value = true;
  formData.value = { ...college };
  showModal.value = true;
};

// 关闭模态框
const closeModal = () => {
  showModal.value = false;
};

// 保存学院
const saveCollege = async () => {
  try {
    if (isEditMode.value) {
      // 更新学院
      await collegeApi.updateCollege(formData.value.collegeId, formData.value);
    } else {
      // 添加学院
      await collegeApi.addCollege(formData.value);
    }
    closeModal();
    await fetchColleges();
  } catch (error) {
    console.error("保存学院数据失败:", error);
  }
};

// 删除学院
const deleteCollege = async (collegeId: string) => {
  if (confirm("确定要删除这个学院吗？")) {
    try {
      await collegeApi.deleteCollege(collegeId);
      await fetchColleges();
    } catch (error) {
      console.error("删除学院失败:", error);
    }
  }
};

// 初始化数据
onMounted(async () => {
  await fetchColleges();
});
</script>

<style scoped>
.college-management-container {
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

.college-table-container {
  overflow-x: auto;
}

.college-table {
  width: 100%;
  border-collapse: collapse;
}

.college-table th,
.college-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.college-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.college-table tr:hover {
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
