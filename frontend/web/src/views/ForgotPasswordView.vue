<script setup lang="ts">
</script>

<template>
  <div class="container">
    <!-- 导航栏 -->
    <header class="header">
      <div class="header-content">
        <div class="logo">
          <h1>东北林业大学教务系统</h1>
        </div>
        <nav class="nav">
          <router-link to="/login" class="login-btn">返回登录</router-link>
          <router-link to="/" class="home-btn">首页</router-link>
        </nav>
      </div>
    </header>

    <!-- 忘记密码内容 -->
    <main class="forgot-password-main">
      <div class="forgot-password-container">
        <div class="forgot-password-box">
          <div class="forgot-password-header">
            <h2>找回密码</h2>
            <p>请按照以下方式前往教务处办理密码重置</p>
          </div>

          <div class="forgot-password-content">
            <div class="info-section">
              <h3>密码重置方式</h3>
              <p>由于系统安全要求，密码重置需要本人携带有效证件前往教务处办理。</p>
            </div>

            <div class="process-section">
              <h3>办理流程</h3>
              <ol>
                <li>携带本人有效证件（身份证、学生证或工作证）</li>
                <li>前往教务处办公室（行政楼105室）</li>
                <li>填写密码重置申请表</li>
                <li>工作人员核实身份后，为您重置密码</li>
                <li>密码重置后，请及时登录系统修改初始密码</li>
              </ol>
            </div>

            <div class="contact-section">
              <h3>教务处联系方式</h3>
              <ul>
                <li><strong>办公地址：</strong>东北林业大学行政楼105室</li>
                <li><strong>联系电话：</strong>0451-82190500</li>
                <li><strong>办公时间：</strong>周一至周五 8:30-11:30，13:30-16:30</li>
                <li><strong>电子邮件：</strong>jwc@nefu.edu.cn</li>
              </ul>
            </div>

            <div class="note-section">
              <h3>注意事项</h3>
              <ul>
                <li>密码重置必须由本人办理，不接受委托代办</li>
                <li>请务必携带有效证件，否则无法办理</li>
                <li>密码重置后，初始密码将发送至您的校园邮箱</li>
                <li>如遇特殊情况，请提前电话联系教务处</li>
              </ul>
            </div>
          </div>

          <div class="action-section">
            <router-link to="/login" class="back-to-login-btn">返回登录</router-link>
          </div>
        </div>
      </div>
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <p>&copy; 2025 东北林业大学教务管理中心 版权所有</p>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.container {
  font-family: 'Microsoft YaHei', sans-serif;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

/* 导航栏 */
.header {
  background-color: #003366;
  color: white;
  padding: 1rem 0;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
}

.logo h1 {
  font-size: 1.8rem;
  margin: 0;
  font-weight: 600;
}

.nav {
  display: flex;
  gap: 1rem;
}

.login-btn, .home-btn {
  padding: 0.5rem 1.5rem;
  border-radius: 4px;
  text-decoration: none;
  font-weight: 500;
  transition: background-color 0.3s;
}

.login-btn {
  background-color: #0066cc;
  color: white;
}

.login-btn:hover {
  background-color: #004499;
}

.home-btn {
  background-color: transparent;
  color: white;
  border: 1px solid white;
}

.home-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* 忘记密码主区域 */
.forgot-password-main {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 2rem;
}

.forgot-password-container {
  width: 100%;
  max-width: 1200px;
  display: flex;
  justify-content: center;
}

.forgot-password-box {
  background-color: white;
  padding: 3rem;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 800px;
}

.forgot-password-header {
  text-align: center;
  margin-bottom: 2rem;
}

.forgot-password-header h2 {
  color: #003366;
  font-size: 2rem;
  margin: 0 0 0.5rem;
}

.forgot-password-header p {
  color: #666;
  margin: 0;
}

/* 内容区域 */
.forgot-password-content {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.info-section, .process-section, .contact-section, .note-section {
  padding: 1.5rem;
  background-color: #f9f9f9;
  border-radius: 8px;
  border-left: 4px solid #0066cc;
}

.info-section h3, .process-section h3, .contact-section h3, .note-section h3 {
  color: #003366;
  font-size: 1.2rem;
  margin: 0 0 1rem;
}

.info-section p {
  color: #333;
  line-height: 1.6;
  margin: 0;
}

/* 流程列表 */
.process-section ol {
  padding-left: 1.5rem;
  margin: 0;
}

.process-section li {
  color: #333;
  margin-bottom: 0.5rem;
  line-height: 1.6;
}

/* 联系方式列表 */
.contact-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.contact-section li {
  color: #333;
  margin-bottom: 0.5rem;
  line-height: 1.6;
}

/* 注意事项列表 */
.note-section ul {
  list-style: disc;
  padding-left: 1.5rem;
  margin: 0;
}

.note-section li {
  color: #333;
  margin-bottom: 0.5rem;
  line-height: 1.6;
}

/* 操作区域 */
.action-section {
  text-align: center;
  margin-top: 2rem;
}

.back-to-login-btn {
  display: inline-block;
  background-color: #0066cc;
  color: white;
  padding: 0.8rem 2rem;
  border-radius: 4px;
  text-decoration: none;
  font-weight: 500;
  transition: background-color 0.3s;
}

.back-to-login-btn:hover {
  background-color: #004499;
}

/* 页脚 */
.footer {
  background-color: #003366;
  color: white;
  padding: 2rem 0;
  margin-top: auto;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  text-align: center;
}

.footer-content p {
  margin: 0;
  opacity: 0.9;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    padding: 0 1rem;
  }

  .logo h1 {
    font-size: 1.4rem;
  }

  .forgot-password-box {
    padding: 2rem 1rem;
  }

  .forgot-password-header h2 {
    font-size: 1.5rem;
  }
}
</style>
