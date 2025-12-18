<template>
  <div class="major-management-container">
    <div class="management-header">
      <h2>专业管理</h2>
      <button class="btn btn-primary" @click="openAddModal">
        <span>➕</span> 添加专业
      </button>
    </div>

    <div class="search-section">
      <input
        v-model="searchKeyword"
        placeholder="搜索专业名称或代码..."
        class="search-input"
      />
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载专业数据...</p>
    </div>
    <div v-else class="major-table-container">
      <table class="major-table">
        <thead>
          <tr>
            <th>专业代码</th>
            <th>专业名称</th>
            <th>所属学院</th>
            <th>办公地点</th>
            <th>联系电话</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="major in filteredMajors" :key="major.majorCode">
            <td>{{ major.majorCode }}</td>
            <td>{{ major.majorName }}</td>
            <td>{{ getCollegeName(major.collegeId) }}</td>
            <td>{{ major.office || "-" }}</td>
            <td>{{ major.phone || "-" }}</td>
            <td>
              <button class="btn btn-sm btn-edit" @click="editMajor(major)">
                编辑
              </button>
              <button
                class="btn btn-sm btn-delete"
                @click="deleteMajor(major.majorCode)"
              >
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="filteredMajors.length === 0" class="empty-state">
        <p>暂无专业数据</p>
      </div>
    </div>

    <!-- 添加/编辑专业模态框 -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ isEditing ? "编辑专业" : "添加专业" }}</h3>
          <button @click="closeModal" class="modal-close">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveMajor">
            <div class="form-group">
              <label for="majorCode">专业代码</label>
              <input
                id="majorCode"
                v-model="formData.majorCode"
                type="text"
                placeholder="请输入专业代码"
                :disabled="isEditing"
                required
              />
            </div>

            <div class="form-group">
              <label for="majorName">专业名称</label>
              <input
                id="majorName"
                v-model="formData.majorName"
                type="text"
                placeholder="请输入专业名称"
                required
              />
            </div>

            <div class="form-group">
              <label for="collegeId">所属学院</label>
              <select id="collegeId" v-model="formData.collegeId" required>
                <option value="">请选择学院</option>
                <option
                  v-for="college in colleges"
                  :key="college.collegeId"
                  :value="college.collegeId"
                >
                  {{ college.collegeName }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label for="office">办公地点</label>
              <input
                id="office"
                v-model="formData.office"
                type="text"
                placeholder="请输入办公地点"
              />
            </div>

            <div class="form-group">
              <label for="phone">联系电话</label>
              <input
                id="phone"
                v-model="formData.phone"
                type="text"
                placeholder="请输入联系电话"
              />
            </div>

            <div class="form-actions">
              <button
                type="button"
                @click="closeModal"
                class="btn btn-secondary"
              >
                取消
              </button>
              <button type="submit" class="btn btn-primary">
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
import { majorApi, Major } from "../../api/majorApi";
import { collegeApi, College } from "../../api/collegeApi";

// 响应式数据
const majors = ref<Major[]>([]);
const colleges = ref<College[]>([]);
const searchKeyword = ref("");
const showModal = ref(false);
const isEditing = ref(false);
const loading = ref(true);
const formData = ref<Major>({
  majorCode: "",
  majorName: "",
  collegeId: "",
  office: "",
  phone: "",
});

// 计算属性：筛选后的专业列表
const filteredMajors = computed(() => {
  if (!searchKeyword.value) return majors.value;
  const keyword = searchKeyword.value.toLowerCase();
  return majors.value.filter(
    (major) =>
      major.majorName.toLowerCase().includes(keyword) ||
      major.majorCode.toLowerCase().includes(keyword)
  );
});

// 获取专业名称
const getCollegeName = (collegeId: string): string => {
  const college = colleges.value.find((c) => c.collegeId === collegeId);
  return college ? college.collegeName : "未知学院";
};

// 加载所有专业
const fetchMajors = async () => {
  try {
    const data = await majorApi.getAllMajors();
    majors.value = data;
  } catch (error) {
    console.error("获取专业数据失败:", error);
  }
};

// 加载所有学院
const fetchColleges = async () => {
  try {
    const data = await collegeApi.getAllColleges();
    colleges.value = data;
  } catch (error) {
    console.error("获取学院数据失败:", error);
  }
};

// 打开添加模态框
const openAddModal = () => {
  isEditing.value = false;
  formData.value = {
    majorCode: "",
    majorName: "",
    collegeId: "",
    office: "",
    phone: "",
  };
  showModal.value = true;
};

// 打开编辑模态框
const editMajor = (major: Major) => {
  isEditing.value = true;
  formData.value = { ...major };
  showModal.value = true;
};

// 关闭模态框
const closeModal = () => {
  showModal.value = false;
};

// 保存专业
const saveMajor = async () => {
  try {
    if (isEditing.value) {
      await majorApi.updateMajor(formData.value.majorCode, formData.value);
    } else {
      await majorApi.addMajor(formData.value);
    }
    await fetchMajors();
    closeModal();
  } catch (error) {
    console.error(isEditing.value ? "更新专业失败:" : "添加专业失败:", error);
  }
};

// 删除专业
const deleteMajor = async (majorCode: string) => {
  if (confirm("确定要删除这个专业吗？")) {
    try {
      await majorApi.deleteMajor(majorCode);
      await fetchMajors();
    } catch (error) {
      console.error("删除专业失败:", error);
    }
  }
};

// 组件挂载时加载数据
onMounted(async () => {
  try {
    loading.value = true;
    await Promise.all([fetchMajors(), fetchColleges()]);
  } catch (error) {
    console.error("加载专业数据失败:", error);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.major-management-container {
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

.major-table-container {
  overflow-x: auto;
}

.major-table {
  width: 100%;
  border-collapse: collapse;
}

.major-table th,
.major-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.major-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.major-table tr:hover {
  background-color: #f5f5f5;
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

.form-group input,
.form-group select,
.form-group textarea {
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

.form-actions .btn {
  padding: 8px 16px;
}

.btn.btn-secondary {
  background-color: #95a5a6;
  color: white;
}

.btn.btn-secondary:hover {
  background-color: #7f8c8d;
}

.btn.btn-primary {
  background-color: #3498db;
  color: white;
}

.btn.btn-primary:hover {
  background-color: #2980b9;
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
</style>
