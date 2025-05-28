<template>
  <div class="login-container">
    <div class="login-box">
      <div class="logo-container">
        <img src="@/assets/logo.png" alt="CMTRIP Logo" class="logo">
      </div>

      <h1 class="title">Welcome Back</h1>
      <p class="subtitle">Sign in to your account</p>

      <div class="form-group">
        <label for="username">Username</label>
        <div class="input-container">
          <i class="fas fa-user"></i>
          <input
              id="username"
              v-model="userName"
              class="input-field"
              placeholder="Enter your username"
              @focus="focusField('username')"
              @blur="blurField('username')"
          >
        </div>
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <div class="input-container">
          <i class="fas fa-lock"></i>
          <input
              id="password"
              v-model="password"
              :type="showPassword ? 'text' : 'password'"
              class="input-field"
              placeholder="Enter your password"
              @focus="focusField('password')"
              @blur="blurField('password')"
          >
          <el-button class="toggle-password" @click="togglePasswordVisibility">
            <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
          </el-button>
        </div>
      </div>

      <div class="forget-password">
        <router-link to="/forget-password">Forgot password?</router-link>
      </div>

      <button @click="handleLogin" class="login-button" :disabled="loading">
        <span v-if="!loading">Sign In</span>
        <span v-else class="loading-spinner"></span>
      </button>

      <div class="social-login">
        <p class="divider">or continue with</p>
        <div class="social-icons">
          <button class="social-btn google">
            <i class="fab fa-google"></i>
          </button>
          <button class="social-btn apple">
            <i class="fab fa-apple"></i>
          </button>
          <button class="social-btn facebook">
            <i class="fab fa-facebook-f"></i>
          </button>
        </div>
      </div>

      <p class="register-text">
        Don't have an account?
        <router-link to="/register" class="register-link">Sign up</router-link>
      </p>
    </div>

    <div class="floating-shapes">
      <div class="shape circle"></div>
      <div class="shape triangle"></div>
      <div class="shape square"></div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/login'
import { ElMessage } from 'element-plus'

const userName = ref('')
const password = ref('')
const showPassword = ref(false)
const loading = ref(false)
const router = useRouter()

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value
  const passwordInput = document.getElementById('password')
  if (passwordInput) {
    passwordInput.type = showPassword.value ? 'text' : 'password'
  }
}

const focusField = (fieldId) => {
  const field = document.getElementById(fieldId)
  if (field) {
    field.parentElement.classList.add('focused')
  }
}

const blurField = (fieldId) => {
  const field = document.getElementById(fieldId)
  if (field) {
    field.parentElement.classList.remove('focused')
  }
}

const handleLogin = async () => {
  if (!userName.value || !password.value) {
    ElMessage.error('Please fill in all fields')
    return
  }

  loading.value = true
  try {
    const response = await login(userName.value, password.value)
    if (response.data) {
      if (response.data.code === 200) {
        const token = response.data.message
        localStorage.setItem('satoken', token)
        localStorage.setItem('userName', userName.value)
        ElMessage.success('Login successfully!')
        await router.push('/home')
      } else {
        ElMessage.error(response.data.message)
      }
    } else {
      ElMessage.error('Connection error! Please try again later.')
    }
  } catch (error) {
    // ElMessage.error('Login failed. Please check your credentials.')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  position: relative;
  overflow: hidden;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
}

.login-box {
  background: white;
  border-radius: 16px;
  padding: 40px;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.05);
  z-index: 2;
  transform: translateY(0);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.1);

  &:hover {
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
    transform: translateY(-5px);
  }
}

.logo-container {
  text-align: center;
  margin-bottom: 24px;

  .logo {
    height: 48px;
    width: auto;
  }
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #2d3748;
  text-align: center;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 14px;
  color: #718096;
  text-align: center;
  margin-bottom: 32px;
}

.form-group {
  margin-bottom: 20px;

  label {
    display: block;
    font-size: 14px;
    color: #4a5568;
    margin-bottom: 8px;
    font-weight: 500;
  }
}

.input-container {
  position: relative;
  display: flex;
  align-items: center;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  transition: all 0.3s ease;
  background: #f8fafc;

  &.focused {
    border-color: #4299e1;
    box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.2);
    background: white;
  }

  i:not(.toggle-password i) { /* 锁定图标 */
    position: absolute;
    left: 16px;
    color: #a0aec0;
    font-size: 18px;
  }

  .toggle-password {
    position: absolute;
    right: 12px; /* 调整右边距 */
    background: none;
    border: none;
    color: #a0aec0;
    cursor: pointer;
    padding: 0;
    font-size: 18px;
    width: 24px; /* 固定宽度 */
    height: 24px; /* 固定高度 */
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 4px;

    &:hover {
      color: #718096;
      background: rgba(0, 0, 0, 0.05);
    }

    i {
      font-size: 16px; /* 调整图标大小 */
    }
  }
}

.input-field {
  width: 100%;
  padding: 14px 48px 14px 48px; /* 左右padding对称 */
  border: none;
  border-radius: 8px;
  font-size: 15px;
  background: transparent;
  color: #2d3748;

  &:focus {
    outline: none;
  }

  &::placeholder {
    color: #a0aec0;
  }
}

.forget-password {
  text-align: right;
  margin-bottom: 24px;

  a {
    color: #718096;
    font-size: 13px;
    text-decoration: none;
    transition: color 0.2s;

    &:hover {
      color: #4299e1;
      text-decoration: underline;
    }
  }
}

.login-button {
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);

  &:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 7px 14px rgba(0, 0, 0, 0.15);
  }

  &:active:not(:disabled) {
    transform: translateY(0);
  }

  &:disabled {
    opacity: 0.7;
    cursor: not-allowed;
  }

  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
    transition: all 0.5s;
  }

  &:hover:not(:disabled)::after {
    left: 100%;
  }
}

.loading-spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.social-login {
  margin: 32px 0;

  .divider {
    position: relative;
    text-align: center;
    color: #a0aec0;
    font-size: 13px;
    margin-bottom: 20px;

    &::before, &::after {
      content: '';
      position: absolute;
      top: 50%;
      width: 30%;
      height: 1px;
      background: #e2e8f0;
    }

    &::before {
      left: 0;
    }

    &::after {
      right: 0;
    }
  }
}

.social-icons {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.social-btn {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: 1px solid #e2e8f0;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;

  i {
    font-size: 20px;
  }

  &.google {
    color: #DB4437;

    &:hover {
      background: rgba(219, 68, 55, 0.1);
    }
  }

  &.apple {
    color: #000000;

    &:hover {
      background: rgba(0, 0, 0, 0.1);
    }
  }

  &.facebook {
    color: #4267B2;

    &:hover {
      background: rgba(66, 103, 178, 0.1);
    }
  }

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.05);
  }
}

.register-text {
  text-align: center;
  color: #718096;
  font-size: 14px;

  .register-link {
    color: #4299e1;
    font-weight: 600;
    text-decoration: none;
    margin-left: 4px;

    &:hover {
      text-decoration: underline;
    }
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