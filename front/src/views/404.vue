<template>
  <div class="not-found-container">
    <div class="not-found-card">
      <h1>404</h1>
      <p class="title">Oops! Page not found</p>
      <p class="subtitle">The page you're looking for doesn't exist or has been moved</p>
      <p class="title">页面未找到</p>
      <p class="subtitle">您访问的页面未找到，或者已被移除。</p>

      <el-button
          type="primary"
          @click="redirectToDefaultPage"
          class="redirect-button"
      >
        {{ redirectButtonText }}
      </el-button>
    </div>

    <div class="floating-shapes">
      <div class="shape circle"></div>
      <div class="shape triangle"></div>
      <div class="shape square"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const redirectButtonText = ref('Go to Login')

// Check the login status 检查登录状态
const checkLoginStatus = () => {
  return !!localStorage.getItem('satoken')
}

// Jump to the page 跳转
const redirectToDefaultPage = () => {
  if (checkLoginStatus()) {
    router.push('/home')
  } else {
    router.push('/login')
  }
}

// Update button text according to the login status 根据登录状态更新按钮文字
onMounted(() => {
  redirectButtonText.value = checkLoginStatus() ? 'Go to Homepage 返回主页' : 'Go to Login 去登录'
  // Record 404 access paths 记录404访问路径
  console.warn(`404 accessed: ${router.currentRoute.value.fullPath}`)
})
</script>

<style scoped lang="scss">
.not-found-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  position: relative;
  overflow: hidden;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
}

.not-found-card {
  background: white;
  border-radius: 16px;
  padding: 40px;
  width: 100%;
  max-width: 500px;
  text-align: center;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.05);
  z-index: 2;

  h1 {
    font-size: 100px;
    margin: 0;
    color: #667eea;
    line-height: 1;
  }

  .title {
    font-size: 24px;
    color: #2d3748;
    margin-top: 20px;
    margin-bottom: 10px;
  }

  .subtitle {
    color: #718096;
    margin-bottom: 30px;
  }
}

.redirect-button {
  width: 300px;
  padding: 15px;
  margin-top: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-size: 16px;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
  }
}

.floating-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;

  .shape {
    position: absolute;
    opacity: 0.1;
    filter: blur(30px);
  }

  .circle {
    width: 200px;
    height: 200px;
    border-radius: 50%;
    background: #667eea;
    top: 20%;
    left: 10%;
    animation: float 15s infinite alternate ease-in-out;
  }

  .triangle {
    width: 0;
    height: 0;
    border-left: 100px solid transparent;
    border-right: 100px solid transparent;
    border-bottom: 200px solid #764ba2;
    bottom: 10%;
    right: 10%;
    animation: float 20s infinite alternate ease-in-out;
  }

  .square {
    width: 150px;
    height: 150px;
    background: #4299e1;
    top: 60%;
    left: 30%;
    animation: float 12s infinite alternate ease-in-out;
  }
}

@keyframes float {
  0% { transform: translate(0, 0); }
  50% { transform: translate(50px, -50px); }
  100% { transform: translate(-50px, 50px); }
}
</style>