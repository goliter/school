<template>
  <div class="schedule-management-container">
    <div class="management-header">
      <h2>课表管理</h2>
      <button @click="openAddModal" class="btn btn-primary">
        <i class="icon-plus"></i> 添加课表
      </button>
    </div>

    <div class="search-bar">
      <input
        v-model="searchKeyword"
        placeholder="搜索班级ID..."
        class="search-input"
      />
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载课表数据...</p>
    </div>
    <div v-else class="schedule-list">
      <table class="data-table">
        <thead>
          <tr>
            <th>班级</th>
            <th>教室</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <template
            v-for="(schedule, scheduleIndex) in filteredSchedules"
            :key="`${schedule.classId}-${schedule.classroomId}-${scheduleIndex}`"
          >
            <tr class="schedule-header">
              <td>
                {{ getClassName(schedule.classId) }}
              </td>
              <td>
                {{ getClassroomName(schedule.classroomId) }}
              </td>
              <td class="action-buttons">
                <button @click="editSchedule(schedule)" class="edit-btn">
                  <i class="icon-edit"></i> 编辑
                </button>
                <button @click="deleteSchedule(schedule)" class="delete-btn">
                  <i class="icon-delete"></i> 删除
                </button>
              </td>
            </tr>
          </template>
        </tbody>
      </table>
      <div v-if="filteredSchedules.length === 0" class="empty-state">
        <p>暂无课表数据</p>
      </div>
    </div>

    <!-- 添加/编辑课表模态框 -->
    <div v-if="showModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEditing ? "编辑课表" : "添加课表" }}</h3>
          <button @click="closeModal" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveSchedule">
            <div class="form-group">
              <label for="classId">班级</label>
              <select id="classId" v-model="formData.classId" required>
                <option value="">请选择班级</option>
                <option
                  v-for="teachingClass in teachingClasses"
                  :key="teachingClass.classId"
                  :value="teachingClass.classId"
                >
                  {{ teachingClass.className }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label for="classroomId">教室</label>
              <select id="classroomId" v-model="formData.classroomId" required>
                <option value="">请选择教室</option>
                <option
                  v-for="classroom in classrooms"
                  :key="classroom.classroomId"
                  :value="classroom.classroomId"
                >
                  {{ classroom.building }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label>上课时间</label>
              <div
                v-for="(time, index) in formData.scheduleInfo.times"
                :key="index"
                class="time-entry"
              >
                <div class="time-fields">
                  <input
                    v-model.number="formData.scheduleInfo.times[index].year"
                    type="number"
                    placeholder="年"
                    required
                    class="time-input"
                    min="2020"
                    max="2030"
                  />
                  <input
                    v-model.number="formData.scheduleInfo.times[index].month"
                    type="number"
                    placeholder="月"
                    required
                    class="time-input"
                    min="1"
                    max="12"
                  />
                  <input
                    v-model.number="formData.scheduleInfo.times[index].day"
                    type="number"
                    placeholder="日"
                    required
                    class="time-input"
                    min="1"
                    max="31"
                  />
                  <input
                    v-model="formData.scheduleInfo.times[index].period"
                    type="text"
                    placeholder="节次（如：1-2节）"
                    required
                    class="time-input"
                  />
                </div>
                <button
                  @click="removeTime(index)"
                  class="remove-time-btn"
                  :disabled="formData.scheduleInfo.times.length <= 1"
                >
                  <i class="icon-delete"></i> 移除
                </button>
              </div>
              <button @click="addTime" class="add-time-btn">
                <i class="icon-plus"></i> 添加时间
              </button>
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
import { scheduleApi, Schedule, ScheduleInfo } from "../../api/scheduleApi";
import { teachingClassApi } from "../../api/teachingClassApi";
import { classroomApi } from "../../api/classroomApi";

// 响应式数据
const schedules = ref<Schedule[]>([]);
const teachingClasses = ref<any[]>([]);
const classrooms = ref<any[]>([]);
const searchKeyword = ref("");
const showModal = ref(false);
const isEditing = ref(false);
const loading = ref(true);
const formData = ref<Schedule>({
  classId: "",
  classroomId: "",
  scheduleInfo: {
    times: [
      {
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1,
        day: new Date().getDate(),
        period: "",
      },
    ],
  },
});

// 计算属性：筛选后的课表列表
const filteredSchedules = computed(() => {
  if (!searchKeyword.value) return schedules.value;
  const keyword = searchKeyword.value.toLowerCase();
  return schedules.value.filter((schedule) =>
    schedule.classId.toLowerCase().includes(keyword)
  );
});

// 获取班级名称
const getClassName = (classId: string): string => {
  const teachingClass = teachingClasses.value.find(
    (tc) => tc.classId === classId
  );
  return teachingClass ? teachingClass.className : "未知班级";
};

// 获取教室名称
const getClassroomName = (classroomId: string): string => {
  const classroom = classrooms.value.find((c) => c.classroomId === classroomId);
  return classroom ? classroom.building : "未知教室";
};

// 加载所有课表
const fetchSchedules = async () => {
  try {
    const data = await scheduleApi.getAllSchedules();
    schedules.value = data.map((schedule) => {
      const updatedSchedule = { ...schedule };
      // 确保scheduleInfo是对象形式
      if (
        updatedSchedule.scheduleInfo &&
        typeof updatedSchedule.scheduleInfo === "string"
      ) {
        updatedSchedule.scheduleInfo = JSON.parse(updatedSchedule.scheduleInfo);
      }
      return updatedSchedule;
    });
  } catch (error) {
    console.error("获取课表数据失败:", error);
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

// 加载所有教室
const fetchClassrooms = async () => {
  try {
    const data = await classroomApi.getAllClassrooms();
    classrooms.value = data;
  } catch (error) {
    console.error("获取教室数据失败:", error);
  }
};

// 添加时间
const addTime = () => {
  formData.value.scheduleInfo.times.push({
    year: new Date().getFullYear(),
    month: new Date().getMonth() + 1,
    day: new Date().getDate(),
    period: "",
  });
};

// 移除时间
const removeTime = (index: number) => {
  if (formData.value.scheduleInfo.times.length > 1) {
    formData.value.scheduleInfo.times.splice(index, 1);
  }
};

// 打开添加模态框
const openAddModal = () => {
  isEditing.value = false;
  formData.value = {
    classId: "",
    classroomId: "",
    scheduleInfo: {
      times: [
        {
          year: new Date().getFullYear(),
          month: new Date().getMonth() + 1,
          day: new Date().getDate(),
          period: "",
        },
      ],
    },
  };
  showModal.value = true;
};

// 打开编辑模态框
const editSchedule = (schedule: Schedule) => {
  isEditing.value = true;
  const scheduleData = { ...schedule };
  // 确保scheduleInfo是对象形式
  if (
    scheduleData.scheduleInfo &&
    typeof scheduleData.scheduleInfo === "string"
  ) {
    scheduleData.scheduleInfo = JSON.parse(scheduleData.scheduleInfo);
  }
  formData.value = scheduleData;
  showModal.value = true;
};

// 关闭模态框
const closeModal = () => {
  showModal.value = false;
};

// 保存课表
const saveSchedule = async () => {
  try {
    if (isEditing.value) {
      await scheduleApi.updateSchedule(formData.value);
    } else {
      await scheduleApi.addSchedule(formData.value);
    }
    await fetchSchedules();
    closeModal();
  } catch (error: any) {
    console.error(isEditing.value ? "更新课表失败:" : "添加课表失败:", error);
    // 显示友好的错误提示，包含时间冲突的可能性
    alert(
      `${
        isEditing.value ? "更新" : "添加"
      }课表失败！可能的原因：\n\n1. 该时间和教室已有其他考试或课表\n`
    );
  }
};

// 删除课表
const deleteSchedule = async (schedule: Schedule) => {
  if (confirm("确定要删除这个课表吗？")) {
    try {
      if (schedule.id) {
        await scheduleApi.deleteSchedule(schedule.id);
        await fetchSchedules();
      }
    } catch (error) {
      console.error("删除课表失败:", error);
    }
  }
};

// 组件挂载时加载数据
onMounted(async () => {
  try {
    loading.value = true;
    await Promise.all([
      fetchSchedules(),
      fetchTeachingClasses(),
      fetchClassrooms(),
    ]);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.schedule-management-container {
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

.time-entry {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  padding: 8px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.time-fields {
  display: flex;
  gap: 8px;
  flex: 1;
}

.time-select,
.time-input {
  flex: 1;
}

.add-time-btn,
.remove-time-btn {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.remove-time-btn {
  background-color: #f56c6c;
}

.remove-time-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.schedule-header {
  background-color: #f5f7fa;
  font-weight: bold;
}

.schedule-header td {
  vertical-align: middle;
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
  padding: 50px 0;
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
  color: #909399;
  font-size: 0.9rem;
}
</style>
