<template>
  <div class="schedule-container">
    <div class="schedule-header">
      <h2>个人课表</h2>
      <div class="date-selector">
        <button @click="changeWeek(-1)" class="week-btn">
          <span class="icon">‹</span> 上一周
        </button>
        <span class="current-week">{{
          formatWeekRange(currentWeekStart)
        }}</span>
        <button @click="changeWeek(1)" class="week-btn">
          下一周 <span class="icon">›</span>
        </button>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载课表数据...</p>
    </div>

    <!-- 课表内容 -->
    <div v-else class="schedule-content">
      <div class="schedule-grid">
        <!-- 表头：星期 -->
        <div class="grid-header empty"></div>
        <div
          v-for="weekday in weekdays"
          :key="weekday.value"
          class="grid-header weekday"
        >
          {{ weekday.label }}
        </div>

        <!-- 表格内容：每行包含一个节次和对应的7天课程 -->
        <template v-for="(period, periodIndex) in periods" :key="period.value">
          <!-- 节次标题 -->
          <div class="grid-period">
            {{ period.label }}
          </div>

          <!-- 7天的课程单元格 -->
          <div
            v-for="weekday in weekdays"
            :key="`${weekday.value}-${period.value}`"
            class="grid-cell"
            :class="{
              'has-course': getCourseForCell(weekday.value, period.value),
            }"
            @click="viewCourseDetail(weekday.value, period.value)"
          >
            <div
              v-if="getCourseForCell(weekday.value, period.value)"
              class="course-info"
            >
              <div class="course-name">
                {{ getCourseForCell(weekday.value, period.value)?.courseName }}
              </div>
              <div class="course-classroom">
                {{
                  getCourseForCell(weekday.value, period.value)?.classroomName
                }}
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>

    <!-- 课程详情模态框 -->
    <div
      v-if="showCourseModal"
      class="modal-overlay"
      @click.self="closeCourseModal"
    >
      <div class="modal">
        <div class="modal-header">
          <h3>课程详情</h3>
          <button class="modal-close" @click="closeCourseModal">&times;</button>
        </div>
        <div v-if="selectedCourse" class="modal-body">
          <div class="detail-item">
            <label>课程名称：</label>
            <span>{{ selectedCourse.courseName }}</span>
          </div>
          <div class="detail-item">
            <label>班级：</label>
            <span>{{ selectedCourse.className }}</span>
          </div>
          <div class="detail-item">
            <label>教师：</label>
            <span>{{ selectedCourse.teacherName }}</span>
          </div>
          <div class="detail-item">
            <label>教室：</label>
            <span>{{ selectedCourse.classroomName }}</span>
          </div>
          <div class="detail-item">
            <label>时间：</label>
            <span
              >{{ getWeekdayName(selectedCourse.day) }}
              {{ selectedCourse.period }}节</span
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { electiveApi } from "../../api/electiveApi";
import { scheduleApi } from "../../api/scheduleApi";
import { teachingClassApi, TeachingClass } from "../../api/teachingClassApi";
import { courseApi, Course } from "../../api/courseApi";
import { teacherApi, Teacher } from "../../api/teacherApi";
import { classroomApi, Classroom } from "../../api/classroomApi";
import { currentUser } from "../../api/auth";

// 类型定义
interface CourseSchedule {
  courseId: string;
  courseName: string;
  className: string;
  teacherName: string;
  classroomId: string;
  classroomName: string;
  day: number; // 1-7 对应周一到周日
  period: string; // 1-6 对应6节课
  date: Date;
}

interface Weekday {
  value: number;
  label: string;
}

interface Period {
  value: string;
  label: string;
}

// 响应式数据
const loading = ref(true);
const currentWeekStart = ref<Date>(new Date());
const courseSchedules = ref<CourseSchedule[]>([]);
const showCourseModal = ref(false);
const selectedCourse = ref<CourseSchedule | null>(null);

// 常量定义
const weekdays: Weekday[] = [
  { value: 1, label: "周一" },
  { value: 2, label: "周二" },
  { value: 3, label: "周三" },
  { value: 4, label: "周四" },
  { value: 5, label: "周五" },
  { value: 6, label: "周六" },
  { value: 7, label: "周日" },
];

const periods: Period[] = [
  { value: "1", label: "第1节" },
  { value: "2", label: "第2节" },
  { value: "3", label: "第3节" },
  { value: "4", label: "第4节" },
  { value: "5", label: "第5节" },
  { value: "6", label: "第6节" },
];

// 计算当前周的日期范围
const currentWeekDates = computed(() => {
  const dates: Date[] = [];
  const startDate = new Date(currentWeekStart.value);

  // 计算本周一的日期
  const dayOfWeek = startDate.getDay();
  const diff = startDate.getDate() - dayOfWeek + (dayOfWeek === 0 ? -6 : 1);
  const monday = new Date(startDate.setDate(diff));

  // 设置为当天凌晨
  monday.setHours(0, 0, 0, 0);

  // 生成一周的日期
  for (let i = 0; i < 7; i++) {
    const date = new Date(monday);
    date.setDate(monday.getDate() + i);
    dates.push(date);
  }

  return dates;
});

// 格式化周范围显示
const formatWeekRange = (date: Date): string => {
  const dates = currentWeekDates.value;
  const startDate = dates[0];
  const endDate = dates[6];

  const formatDate = (d: Date): string => {
    return `${d.getMonth() + 1}月${d.getDate()}日`;
  };

  return `${formatDate(startDate)} - ${formatDate(endDate)}`;
};

// 切换周
const changeWeek = (delta: number) => {
  const newDate = new Date(currentWeekStart.value);
  newDate.setDate(newDate.getDate() + delta * 7);
  currentWeekStart.value = newDate;
  loadScheduleData();
};

// 获取学生的课程安排
const loadScheduleData = async () => {
  try {
    loading.value = true;

    // 获取当前学生ID
    const studentId = currentUser.value?.userId;
    if (!studentId) {
      console.error("无法获取当前学生信息");
      return;
    }

    // 获取学生的所有课程安排（新的API，包含所有必要数据）
    const schedules = await scheduleApi.getStudentSchedules(studentId);

    if (schedules.length === 0) {
      courseSchedules.value = [];
      return;
    }

    // 处理返回的课程安排数据
    const courseSchedulesList: CourseSchedule[] = [];

    schedules.forEach((schedule: any) => {
      // 处理scheduleInfo - 解析JSON字符串
      let scheduleInfo;
      try {
        scheduleInfo =
          typeof schedule.scheduleInfo === "string"
            ? JSON.parse(schedule.scheduleInfo)
            : schedule.scheduleInfo;
      } catch (error) {
        console.error("解析scheduleInfo失败:", error);
        scheduleInfo = { times: [] };
      }

      const times =
        scheduleInfo && scheduleInfo.times ? scheduleInfo.times : [];

      times.forEach((time: any) => {
        // 创建日期对象
        const date = new Date(time.year, time.month - 1, time.day);

        courseSchedulesList.push({
          courseId: schedule.courseId || "",
          courseName: schedule.courseName || "未命名课程",
          className: schedule.className || "未知班级",
          teacherName: schedule.teacherName || "未知教师",
          classroomId: schedule.classroomId || "",
          classroomName: schedule.classroomName || "未知教室",
          day: date.getDay() || 7, // 将0转换为7表示周日
          period: time.period || "",
          date: date,
        });
      });
    });

    courseSchedules.value = courseSchedulesList;
  } catch (err: any) {
    console.error("获取课程安排失败:", err);
    // 可以在这里添加用户友好的错误提示
  } finally {
    loading.value = false;
  }
};

// 获取指定单元格的课程
const getCourseForCell = (
  day: number,
  period: string
): CourseSchedule | undefined => {
  const currentDates = currentWeekDates.value;
  const targetDate = currentDates[day - 1];

  return courseSchedules.value.find((schedule) => {
    return (
      schedule.day === day &&
      schedule.period === period &&
      schedule.date.getFullYear() === targetDate.getFullYear() &&
      schedule.date.getMonth() === targetDate.getMonth() &&
      schedule.date.getDate() === targetDate.getDate()
    );
  });
};

// 查看课程详情
const viewCourseDetail = (day: number, period: string) => {
  const course = getCourseForCell(day, period);
  if (course) {
    selectedCourse.value = course;
    showCourseModal.value = true;
  }
};

// 关闭课程详情模态框
const closeCourseModal = () => {
  showCourseModal.value = false;
  selectedCourse.value = null;
};

// 获取星期名称
const getWeekdayName = (day: number): string => {
  const weekday = weekdays.find((w) => w.value === day);
  return weekday ? weekday.label : "";
};

// 组件挂载时初始化
onMounted(() => {
  // 设置为当前周的周一
  const now = new Date();
  const dayOfWeek = now.getDay();
  const diff = now.getDate() - dayOfWeek + (dayOfWeek === 0 ? -6 : 1);
  currentWeekStart.value = new Date(now.setDate(diff));
  currentWeekStart.value.setHours(0, 0, 0, 0);

  // 加载课程安排
  loadScheduleData();
});
</script>

<style scoped>
.schedule-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.schedule-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.schedule-header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.date-selector {
  display: flex;
  align-items: center;
  gap: 15px;
}

.week-btn {
  padding: 8px 16px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.week-btn:hover {
  background-color: #66b1ff;
}

.current-week {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  min-width: 150px;
  text-align: center;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
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

.schedule-grid {
  display: grid;
  grid-template-columns: 80px repeat(7, 1fr);
  grid-template-rows: 50px repeat(6, 100px);
  gap: 2px;
  background-color: #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
}

.grid-header {
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.grid-header.empty {
  background-color: transparent;
}

.grid-header.weekday {
  background-color: #409eff;
  color: white;
}

.grid-period {
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.grid-cell {
  background-color: white;
  padding: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.grid-cell:hover {
  background-color: #f0f9ff;
}

.grid-cell.has-course {
  background-color: #e6f7ff;
}

.course-info {
  background-color: #409eff;
  color: white;
  padding: 8px;
  border-radius: 4px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.course-name {
  font-weight: 500;
  margin-bottom: 4px;
  font-size: 13px;
  line-height: 1.3;
}

.course-classroom {
  font-size: 11px;
  opacity: 0.9;
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
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e8e8e8;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.modal-close:hover {
  background-color: #f5f5f5;
  color: #666;
}

.modal-body {
  padding: 20px;
}

.detail-item {
  display: flex;
  margin-bottom: 15px;
  align-items: center;
}

.detail-item label {
  width: 100px;
  font-weight: 500;
  color: #666;
}

.detail-item span {
  color: #333;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .schedule-container {
    padding: 10px;
  }

  .schedule-header {
    flex-direction: column;
    gap: 15px;
  }

  .schedule-grid {
    grid-template-columns: 60px repeat(7, 1fr);
    grid-template-rows: 40px repeat(6, 80px);
  }

  .grid-header,
  .grid-period {
    font-size: 12px;
  }

  .course-info {
    padding: 5px;
  }

  .course-name {
    font-size: 11px;
  }

  .course-classroom {
    font-size: 9px;
  }
}
</style>
