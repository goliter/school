/* ===============================
   2. 学院表
   =============================== */
CREATE TABLE IF NOT EXISTS college (
                         college_id   VARCHAR(20) PRIMARY KEY COMMENT '学院编号',
                         college_name VARCHAR(50) NOT NULL COMMENT '学院名称',
                         address      VARCHAR(100),
                         phone        VARCHAR(20)
) ENGINE=InnoDB;

/* ===============================
   3. 专业表
   =============================== */
CREATE TABLE IF NOT EXISTS major (
                       major_code VARCHAR(20) PRIMARY KEY COMMENT '专业编码',
                       major_name VARCHAR(50) NOT NULL,
                       office     VARCHAR(100),
                       phone      VARCHAR(20),
                       college_id VARCHAR(20) NOT NULL,
                       FOREIGN KEY (college_id) REFERENCES college(college_id)
) ENGINE=InnoDB;

/* ===============================
   4. 用户账号表（登录核心）
   =============================== */
CREATE TABLE IF NOT EXISTS user_account (
                              user_id     VARCHAR(20) PRIMARY KEY COMMENT '登录账号',
                              password    VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
                              role        ENUM('student','teacher','admin') NOT NULL COMMENT '角色',
                              status      TINYINT DEFAULT 1 COMMENT '状态(1正常 0禁用)',
                              create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

/* ===============================
   5. 学生表
   =============================== */
CREATE TABLE IF NOT EXISTS student (
                         student_id VARCHAR(20) PRIMARY KEY COMMENT '学号',
                         name       VARCHAR(50) NOT NULL,
                         gender     CHAR(1),
                         birth_date DATE,
                         phone      VARCHAR(20),
                         major_code VARCHAR(20) NOT NULL,
                         user_id    VARCHAR(20) UNIQUE COMMENT '关联登录账号',
                         FOREIGN KEY (major_code) REFERENCES major(major_code),
                         FOREIGN KEY (user_id) REFERENCES user_account(user_id)
) ENGINE=InnoDB;

/* ===============================
   6. 教师表
   =============================== */
CREATE TABLE IF NOT EXISTS teacher (
                         teacher_id VARCHAR(20) PRIMARY KEY COMMENT '工号',
                         name       VARCHAR(50) NOT NULL,
                         title      VARCHAR(30),
                         office     VARCHAR(100),
                         phone      VARCHAR(20),
                         duty       VARCHAR(30),
                         major_code VARCHAR(20) NOT NULL,
                         user_id    VARCHAR(20) UNIQUE COMMENT '关联登录账号',
                         FOREIGN KEY (major_code) REFERENCES major(major_code),
                         FOREIGN KEY (user_id) REFERENCES user_account(user_id)
) ENGINE=InnoDB;

/* ===============================
   7. 课程表
   =============================== */
CREATE TABLE IF NOT EXISTS course (
                        course_id   VARCHAR(20) PRIMARY KEY,
                        course_name VARCHAR(50) NOT NULL,
                        credit      DECIMAL(3,1),
                        course_type VARCHAR(20),
                        major_code  VARCHAR(20),
                        FOREIGN KEY (major_code) REFERENCES major(major_code)
) ENGINE=InnoDB;

/* ===============================
   8. 教学班
   =============================== */
CREATE TABLE IF NOT EXISTS teaching_class (
                                class_id   VARCHAR(20) PRIMARY KEY,
                                class_name VARCHAR(50),
                                semester   VARCHAR(20),
                                course_id  VARCHAR(20),
                                teacher_id VARCHAR(20),
                                FOREIGN KEY (course_id) REFERENCES course(course_id),
                                FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id)
) ENGINE=InnoDB;

/* ===============================
   9. 教室
   =============================== */
CREATE TABLE IF NOT EXISTS classroom (
                           classroom_id VARCHAR(20) PRIMARY KEY,
                           building     VARCHAR(50),
                           nature       VARCHAR(20),
                           capacity     INT
) ENGINE=InnoDB;

/* ===============================
   10. 课表
   =============================== */
CREATE TABLE IF NOT EXISTS schedule (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          class_id     VARCHAR(20),
                          classroom_id VARCHAR(20),
                          schedule_info JSON,
                          FOREIGN KEY (class_id) REFERENCES teaching_class(class_id),
                          FOREIGN KEY (classroom_id) REFERENCES classroom(classroom_id)
) ENGINE=InnoDB;

/* ===============================
   11. 考试
   =============================== */
CREATE TABLE IF NOT EXISTS exam (
                      exam_id      VARCHAR(20) PRIMARY KEY,
                      exam_name    VARCHAR(50),
                      exam_date_info JSON,
                      classroom_id VARCHAR(20),
                      class_id     VARCHAR(20),
                      FOREIGN KEY (classroom_id) REFERENCES classroom(classroom_id),
                      FOREIGN KEY (class_id) REFERENCES teaching_class(class_id)
) ENGINE=InnoDB;

/* ===============================
   12. 成绩
   =============================== */
CREATE TABLE IF NOT EXISTS score (
                       student_id VARCHAR(20),
                       exam_id    VARCHAR(20),
                       score      DECIMAL(5,2),
                       PRIMARY KEY (student_id, exam_id),
                       FOREIGN KEY (student_id) REFERENCES student(student_id),
                       FOREIGN KEY (exam_id) REFERENCES exam(exam_id)
) ENGINE=InnoDB;

/* ===============================
   13. 选修
   =============================== */
CREATE TABLE IF NOT EXISTS elective (
                          student_id VARCHAR(20),
                          class_id   VARCHAR(20),
                          evaluation VARCHAR(100),
                          PRIMARY KEY (student_id, class_id),
                          FOREIGN KEY (student_id) REFERENCES student(student_id),
                          FOREIGN KEY (class_id) REFERENCES teaching_class(class_id)
) ENGINE=InnoDB;
