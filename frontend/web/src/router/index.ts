import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import ForgotPasswordView from "../views/ForgotPasswordView.vue";
import StudentView from "../views/StudentView.vue";
import TeacherView from "../views/TeacherView.vue";
import AdminView from "../views/AdminView.vue";
import { isLoggedIn } from "../api/auth";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
      meta: { requiresAuth: false }, // 首页不需要登录即可访问
    },
    {
      path: "/login",
      name: "login",
      component: LoginView,
    },
    {
      path: "/forgot-password",
      name: "forgotPassword",
      component: ForgotPasswordView,
    },
    {
      path: "/student",
      name: "student",
      component: StudentView,
      meta: { requiresAuth: true }, // 需要登录才能访问
      children: [
        {
          path: "",
          redirect: "/student/personal-info",
        },
        {
          path: "personal-info",
          name: "studentPersonalInfo",
          component: () => import("../views/student/PersonalInfoView.vue"),
        },
        {
          path: "courses",
          name: "studentCourses",
          component: () => import("../views/student/CoursesView.vue"),
        },
        {
          path: "exams",
          name: "studentExams",
          component: () => import("../views/student/ExamsView.vue"),
        },
        {
          path: "schedule",
          name: "studentSchedule",
          component: () => import("../views/student/ScheduleView.vue"),
        },
        {
          path: "elective",
          name: "studentElective",
          component: () => import("../views/student/CourseSelectionView.vue"),
        },
        {
          path: "scores",
          name: "studentScores",
          component: () => import("../views/student/StudentScoresView.vue"),
        },
      ],
    },
    {
      path: "/teacher",
      name: "teacher",
      component: TeacherView,
      meta: { requiresAuth: true }, // 需要登录才能访问
      children: [
        {
          path: "",
          redirect: "/teacher/personal-info",
        },
        {
          path: "personal-info",
          name: "teacherPersonalInfo",
          component: () => import("../views/teacher/PersonalInfoView.vue"),
        },
        {
          path: "courses",
          name: "teacherCourses",
          component: () => import("../views/teacher/CoursesView.vue"),
        },
        {
          path: "exams",
          name: "teacherExams",
          component: () => import("../views/teacher/ExamsView.vue"),
        },
        {
          path: "schedule",
          name: "teacherSchedule",
          component: () => import("../views/teacher/ScheduleView.vue"),
        },
        {
          path: "scores",
          name: "teacherScores",
          component: () => import("../views/teacher/ScoresView.vue"),
        },
      ],
    },
    {
      path: "/admin",
      name: "admin",
      component: AdminView,
      meta: { requiresAuth: true }, // 需要登录才能访问
      children: [
        {
          path: "",
          redirect: "/admin/dashboard",
        },
        {
          path: "dashboard",
          name: "adminDashboard",
          component: () => import("../views/admin/DashboardView.vue"),
        },
        {
          path: "users",
          name: "adminUsers",
          component: () => import("../views/admin/UserManagementView.vue"),
        },
        {
          path: "colleges",
          name: "adminColleges",
          component: () => import("../views/admin/CollegeManagementView.vue"),
        },
        {
          path: "majors",
          name: "adminMajors",
          component: () => import("../views/admin/MajorManagementView.vue"),
        },
        {
          path: "courses",
          name: "adminCourses",
          component: () => import("../views/admin/CourseManagementView.vue"),
        },
        {
          path: "classrooms",
          name: "adminClassrooms",
          component: () => import("../views/admin/ClassroomManagementView.vue"),
        },
        {
          path: "teaching-classes",
          name: "adminTeachingClasses",
          component: () =>
            import("../views/admin/TeachingClassManagementView.vue"),
        },
        {
          path: "exams",
          name: "adminExams",
          component: () => import("../views/admin/ExamManagementView.vue"),
        },
        {
          path: "schedules",
          name: "adminSchedules",
          component: () => import("../views/admin/ScheduleManagementView.vue"),
        },
      ],
    },
  ],
});

// 路由守卫：检查登录状态
router.beforeEach((to, from, next) => {
  // 如果路由需要登录且用户未登录，则跳转到登录页
  if (to.meta.requiresAuth && !isLoggedIn.value) {
    next({ name: "login" });
  } else {
    next();
  }
});

export default router;
