<template>
  <div class="courses-container">
    <div class="management-header">
      <h2>教学班管理</h2>
    </div>

    <div class="search-section">
      <input
        v-model="searchQuery"
        type="text"
        placeholder="搜索班级名称或课程名称..."
        class="search-input"
      />
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载教学班数据...</p>
    </div>
    <div v-else class="courses-table-container">
      <table class="courses-table">
        <thead>
          <tr>
            <th>教学班编号</th>
            <th>班级名称</th>
            <th>课程名称</th>
            <th>学期</th>
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
            <td>{{ teachingClass.courseName }}</td>
            <td>{{ teachingClass.semester }}</td>
            <td>
              <button
                class="btn btn-sm btn-info"
                @click="openClassDetailsModal(teachingClass)"
              >
                详情
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="filteredTeachingClasses.length === 0" class="empty-state">
        <p>暂无教学班数据</p>
      </div>
    </div>

    <!-- 教学班详情模态框 -->
    <div
      v-if="showDetailsModal"
      class="modal-overlay"
      @click.self="closeClassDetailsModal"
    >
      <div class="modal">
        <div class="modal-header">
          <h3>教学班详情</h3>
          <button class="modal-close" @click="closeClassDetailsModal">
            &times;
          </button>
        </div>
        <div class="modal-body">
          <div v-if="selectedTeachingClass" class="course-details">
            <div class="detail-item">
              <label>教学班编号</label>
              <span>{{ selectedTeachingClass.classId }}</span>
            </div>
            <div class="detail-item">
              <label>班级名称</label>
              <span>{{ selectedTeachingClass.className }}</span>
            </div>
            <div class="detail-item">
              <label>课程名称</label>
              <span>{{ selectedTeachingClass.courseName }}</span>
            </div>
            <div class="detail-item">
              <label>学期</label>
              <span>{{ selectedTeachingClass.semester }}</span>
            </div>
            <div class="detail-item">
              <label>上课时间</label>
              <span>{{
                getLatestClassTime(selectedTeachingClass?.scheduleInfo)
              }}</span>
            </div>
            <div class="detail-item">
              <label>学生人数</label>
              <span>{{ selectedTeachingClass.studentCount || 0 }}</span>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" @click="closeClassDetailsModal">
              关闭
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import {
  teachingClassApi,
  type TeachingClass,
} from "../../api/teachingClassApi";
import { teacherApi, type Teacher } from "../../api/teacherApi";
import { currentUser } from "../../api/auth";
import type { ScheduleInfo } from "../../api/scheduleApi";

// 数据
const teachingClasses = ref<TeachingClass[]>([]);
const searchQuery = ref("");
const loading = ref(true);
const currentTeacher = ref<Teacher | null>(null);

// 详情模态框
const showDetailsModal = ref(false);
const selectedTeachingClass = ref<TeachingClass | null>(null);

// 获取当前登录用户的教师信息
const fetchCurrentTeacherInfo = async () => {
  try {
    const userId = currentUser.value?.userId;
    if (!userId) {
      console.error("未找到当前登录用户ID");
      return;
    }

    // 先获取所有教师，然后根据userId筛选
    const allTeachers = await teacherApi.getAllTeachers();
    const teacher = allTeachers.find((t) => t.userId === userId);

    if (teacher) {
      currentTeacher.value = teacher;
    } else {
      console.error("未找到对应的教师信息");
    }
  } catch (error) {
    console.error("获取当前教师信息失败:", error);
  }
};

// 获取教师负责的教学班
const fetchTeachingClasses = async () => {
  try {
    loading.value = true;
    if (!currentTeacher.value) {
      teachingClasses.value = [];
      return;
    }
    const data = await teachingClassApi.getTeachingClassesByTeacher(
      currentTeacher.value.teacherId
    );
    teachingClasses.value = data;
  } catch (error) {
    console.error("获取教学班数据失败:", error);
  } finally {
    loading.value = false;
  }
};

// 搜索筛选
const filteredTeachingClasses = computed(() => {
  if (!searchQuery.value) {
    return teachingClasses.value;
  }
  const query = searchQuery.value.toLowerCase();
  return teachingClasses.value.filter((teachingClass) => {
    return (
      teachingClass.classId.toLowerCase().includes(query) ||
      teachingClass.className.toLowerCase().includes(query) ||
      teachingClass.courseName.toLowerCase().includes(query)
    );
  });
});

// 打开教学班详情模态框
const openClassDetailsModal = (teachingClass: TeachingClass) => {
  selectedTeachingClass.value = teachingClass;
  showDetailsModal.value = true;
};

// 关闭教学班详情模态框
const closeClassDetailsModal = () => {
  showDetailsModal.value = false;
  selectedTeachingClass.value = null;
};

// 获取最近的上课时间
const getLatestClassTime = (scheduleInfoStr: string | undefined): string => {
  if (!scheduleInfoStr) return "-";

  try {
    const scheduleInfo: ScheduleInfo = JSON.parse(scheduleInfoStr);
    const times = scheduleInfo.times || [];

    if (times.length === 0) return "-";

    // 转换为Date对象以便比较
    const dateTimes = times.map((time) => ({
      ...time,
      date: new Date(time.year, time.month - 1, time.day),
    }));

    // 过滤掉已过去的时间
    const now = new Date();
    const futureTimes = dateTimes.filter((item) => item.date >= now);

    if (futureTimes.length === 0) {
      // 如果没有未来时间，返回最近的过去时间
      const latestPast = dateTimes.reduce((latest, current) =>
        current.date > latest.date ? current : latest
      );
      return formatScheduleTime(latestPast);
    }

    // 找到最近的未来时间
    const latestTime = futureTimes.reduce((earliest, current) =>
      current.date < earliest.date ? current : earliest
    );

    return formatScheduleTime(latestTime);
  } catch (error) {
    console.error("解析课表信息失败:", error);
    return "-";
  }
};

// 格式化上课时间显示
const formatScheduleTime = (time: {
  year: number;
  month: number;
  day: number;
  period: string;
}): string => {
  return `${time.year}-${time.month.toString().padStart(2, "0")}-${time.day
    .toString()
    .padStart(2, "0")} ${time.period}节`;
};

// 初始化数据
onMounted(async () => {
  await fetchCurrentTeacherInfo();
  await fetchTeachingClasses();
});
</script>

<style scoped>
.courses-container {
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

.btn-info {
  background-color: #17a2b8;
  color: white;
  padding: 6px 12px;
}

.btn-info:hover {
  background-color: #138496;
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

.courses-table-container {
  overflow-x: auto;
}

.courses-table {
  width: 100%;
  border-collapse: collapse;
}

.courses-table th,
.courses-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.courses-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.courses-table tr:hover {
  background-color: #f5f5f5;
}

.empty-state {
  text-align: center;
  padding: 50px 0;
  color: #666;
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

.course-details {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.detail-item:last-child {
  border-bottom: none;
}

.detail-item label {
  font-weight: 600;
  color: #555;
}

.detail-item span {
  color: #333;
}

.modal-footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
