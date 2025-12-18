<template>
  <div class="classroom-management-container">
    <div class="management-header">
      <h2>教室管理</h2>
      <button @click="openAddModal" class="btn btn-primary">
        <i class="icon-plus"></i> 添加教室
      </button>
    </div>

    <div class="search-bar">
      <input
        v-model="searchKeyword"
        placeholder="搜索教学楼或房间号..."
        class="search-input"
      />
    </div>

    <div class="classroom-list">
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>正在加载教室数据...</p>
      </div>
      <template v-else>
        <table class="data-table">
          <thead>
            <tr>
              <th>教室ID</th>
              <th>教学楼</th>
              <th>容量</th>
              <th>类型</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="classroom in filteredClassrooms"
              :key="classroom.classroomId"
            >
              <td>{{ classroom.classroomId }}</td>
              <td>{{ classroom.building }}</td>
              <td>{{ classroom.capacity }}</td>
              <td>{{ classroom.nature }}</td>
              <td class="action-buttons">
                <button @click="editClassroom(classroom)" class="edit-btn">
                  <i class="icon-edit"></i> 编辑
                </button>
                <button
                  @click="deleteClassroom(classroom.classroomId)"
                  class="delete-btn"
                >
                  <i class="icon-delete"></i> 删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-if="filteredClassrooms.length === 0" class="empty-state">
          <p>暂无教室数据</p>
        </div>
      </template>
    </div>

    <!-- 添加/编辑教室模态框 -->
    <div v-if="showModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEditing ? "编辑教室" : "添加教室" }}</h3>
          <button @click="closeModal" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveClassroom">
            <div class="form-group">
              <label for="classroomId">教室ID</label>
              <input
                id="classroomId"
                v-model="formData.classroomId"
                type="text"
                placeholder="请输入教室ID"
                :disabled="isEditing"
                required
              />
            </div>

            <div class="form-group">
              <label for="building">教学楼</label>
              <input
                id="building"
                v-model="formData.building"
                type="text"
                placeholder="请输入教学楼"
                required
              />
            </div>

            <div class="form-group">
              <label for="capacity">容量</label>
              <input
                id="capacity"
                v-model.number="formData.capacity"
                type="number"
                min="1"
                placeholder="请输入容量"
                required
              />
            </div>

            <div class="form-group">
              <label for="type">类型</label>
              <input
                id="nature"
                v-model="formData.nature"
                type="text"
                placeholder="请输入教室类型（如：普通教室、实验室、多媒体教室等）"
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
import { classroomApi, Classroom } from "../../api/classroomApi";

// 响应式数据
const loading = ref(true);
const classrooms = ref<Classroom[]>([]);
const searchKeyword = ref("");
const showModal = ref(false);
const isEditing = ref(false);
const formData = ref<Classroom>({
  classroomId: "",
  building: "",
  capacity: 0,
  nature: "",
});

// 计算属性：筛选后的教室列表
const filteredClassrooms = computed(() => {
  if (!searchKeyword.value) return classrooms.value;
  const keyword = searchKeyword.value.toLowerCase();
  return classrooms.value.filter((classroom) =>
    classroom.building.toLowerCase().includes(keyword)
  );
});

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
    classroomId: "",
    building: "",
    capacity: 0,
    nature: "",
  };
  showModal.value = true;
};

// 打开编辑模态框
const editClassroom = (classroom: Classroom) => {
  isEditing.value = true;
  formData.value = { ...classroom };
  showModal.value = true;
};

// 关闭模态框
const closeModal = () => {
  showModal.value = false;
};

// 保存教室
const saveClassroom = async () => {
  try {
    if (isEditing.value) {
      await classroomApi.updateClassroom(
        formData.value.classroomId,
        formData.value
      );
    } else {
      await classroomApi.addClassroom(formData.value);
    }
    await fetchClassrooms();
    closeModal();
  } catch (error) {
    console.error(isEditing.value ? "更新教室失败:" : "添加教室失败:", error);
  }
};

// 删除教室
const deleteClassroom = async (classroomId: string) => {
  if (confirm("确定要删除这个教室吗？")) {
    try {
      await classroomApi.deleteClassroom(classroomId);
      await fetchClassrooms();
    } catch (error) {
      console.error("删除教室失败:", error);
    }
  }
};

// 组件挂载时加载数据
onMounted(async () => {
  try {
    loading.value = true;
    await fetchClassrooms();
  } catch (error) {
    console.error("加载教室数据失败:", error);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.classroom-management-container {
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
.form-group textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus,
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
</style>
