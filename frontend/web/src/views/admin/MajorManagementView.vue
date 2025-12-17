<template>
  <div class="major-management">
    <div class="page-header">
      <h2>专业管理</h2>
      <button @click="showAddModal = true" class="add-btn">
        <i class="icon-plus"></i> 添加专业
      </button>
    </div>

    <div class="search-bar">
      <input
        v-model="searchKeyword"
        placeholder="搜索专业名称或代码..."
        class="search-input"
      />
    </div>

    <div class="major-list">
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>正在加载专业数据...</p>
      </div>
      <template v-else>
        <table class="data-table">
          <thead>
            <tr>
              <th>专业代码</th>
              <th>专业名称</th>
              <th>所属学院</th>
              <th>专业描述</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="major in filteredMajors" :key="major.majorCode">
              <td>{{ major.majorCode }}</td>
              <td>{{ major.majorName }}</td>
              <td>{{ getCollegeName(major.collegeId) }}</td>
              <td>{{ major.description || "-" }}</td>
              <td class="action-buttons">
                <button @click="editMajor(major)" class="edit-btn">
                  <i class="icon-edit"></i> 编辑
                </button>
                <button
                  @click="deleteMajor(major.majorCode)"
                  class="delete-btn"
                >
                  <i class="icon-delete"></i> 删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-if="filteredMajors.length === 0" class="empty-state">
          <p>暂无专业数据</p>
        </div>
      </template>
    </div>

    <!-- 添加/编辑专业模态框 -->
    <div v-if="showModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEditing ? "编辑专业" : "添加专业" }}</h3>
          <button @click="closeModal" class="close-btn">×</button>
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
              <label for="description">专业描述</label>
              <textarea
                id="description"
                v-model="formData.description"
                rows="3"
                placeholder="请输入专业描述"
              ></textarea>
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
import { majorApi, Major } from "../../api/majorApi";
import { collegeApi, College } from "../../api/collegeApi";

// 响应式数据
const majors = ref<Major[]>([]);
const colleges = ref<College[]>([]);
const searchKeyword = ref("");
const showModal = ref(false);
const showAddModal = ref(false);
const isEditing = ref(false);
const loading = ref(true);
const formData = ref<Major>({
  majorCode: "",
  majorName: "",
  collegeId: "",
  description: "",
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
    description: "",
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
  showAddModal.value = false;
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

// 监听添加按钮状态
const unwatchAddModal = computed(() => showAddModal.value).value;
if (unwatchAddModal) {
  openAddModal();
}

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
.major-management {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #333;
}

.add-btn {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.add-btn:hover {
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
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
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
</style>
