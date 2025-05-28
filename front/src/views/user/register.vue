<template>
  <div class="register-page">
    <div class="register-card">
      <div class="logo-container">
        <img src="@/assets/logo.png" alt="CMTRIP Logo" class="logo">
      </div>

      <h1 class="title">Create Your Account</h1>
      <p class="subtitle">Join our community today</p>

      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <div class="form-group">
          <label for="username">Username</label>
          <div class="input-container">
            <i class="fas fa-user"></i>
            <el-form-item prop="userName">
              <el-input
                  id="username"
                  v-model="form.userName"
                  placeholder="Enter your username"
                  class="input-field"
                  @focus="focusField('username')"
                  @blur="blurField('username')"
              />
            </el-form-item>
          </div>
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <div class="input-container">
            <i class="fas fa-lock"></i>
            <el-form-item prop="password">
              <el-input
                  id="password"
                  v-model="form.password"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="Enter your password"
                  class="input-field"
                  @focus="focusField('password')"
                  @blur="blurField('password')"
              />
            </el-form-item>
            <el-button class="toggle-password" @click="togglePasswordVisibility">
              <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
            </el-button>
          </div>
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <div class="input-container">
            <i class="fas fa-envelope"></i>
            <el-form-item prop="email">
              <el-input
                  id="email"
                  v-model="form.email"
                  placeholder="Enter your email"
                  class="input-field"
                  @focus="focusField('email')"
                  @blur="blurField('email')"
              />
            </el-form-item>
          </div>
        </div>

        <el-button @click="onRegister" class="submit-btn" :disabled="loading">
          <span v-if="!loading">Create Account</span>
          <span v-else class="loading-spinner"></span>
        </el-button>
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

        <p class="login-text">
          Already have an account?
          <router-link to="/login" class="login-link">Sign in</router-link>
        </p>
      </el-form>
    </div>

    <div class="floating-shapes">
      <div class="shape circle"></div>
      <div class="shape triangle"></div>
      <div class="shape square"></div>
    </div>
  </div>
</template>

<script setup>
import {computed, reactive, ref} from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/login'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const showPassword = ref(false)

const form = ref({
  userName: '',
  password: '',
  email: ''
})

const rules = {
  userName: [
    { required: true, message: 'Username is required', trigger: 'blur' },
    { min: 3, message: 'Minimum 3 characters', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Password is required', trigger: 'blur' },
    { min: 8, message: 'Password must be at least 8 characters', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (!/[A-Z]/.test(value)) {
          callback(new Error('Password must contain at least one uppercase letter'))
        } else if (!/[0-9]/.test(value)) {
          callback(new Error('Password must contain at least one number'))
        } else if (!/[^A-Za-z0-9]/.test(value)) {
          callback(new Error('Password must contain at least one special character'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  email: [
    { required: true, message: 'Email is required', trigger: 'blur' },
    { type: 'email', message: 'Please enter a valid email', trigger: 'blur' }
  ]
}

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value
  const passwordInput = document.getElementById('password')
  if (passwordInput) {
    passwordInput.type = showPassword.value ? 'text' : 'password'
  }
}

const focusField = (fieldId) => {
  const container = document.getElementById(fieldId)?.parentElement
  if (container) container.classList.add('focused')
}

const blurField = (fieldId) => {
  const container = document.getElementById(fieldId)?.parentElement
  if (container) container.classList.remove('focused')
}

const onRegister = async () => {
  formRef.value.validate(async(valid) => {
    if (!valid) {
      ElMessage.error("Please fill in all items correctly!");
      return
    }
    try {
      // Perform custom validation (e.g., password length) 执行自定义验证（如密码长度）
      if (form.value.password.length < 8) {
        ElMessage.error("Password must contain at least 8 characters!")
        return
      }
      // Proceed with registration only after validation is successful 验证通过后才执行注册
      console.log("Validation succeeded 验证通过，form.value：", form.value)
      loading.value = true
      await register(form.value)
      // Only navigate after successful registration 注册成功后才跳转
      ElMessage.success('Registration successful! Redirecting to login...')
      setTimeout(() => router.push('/login'), 1500)
    } catch (error) {
    } finally {
      loading.value = false
    }
  });
}
</script>

<style scoped lang="scss">
.register-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  position: relative;
  overflow: hidden;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
}

.register-card {
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
  margin-bottom: 24px;
  position: relative; // Provide contextual positioning for error messages 为错误提示提供定位上下文

  label {
    display: block;
    font-size: 14px;
    color: #4a5568;
    margin-bottom: 8px;
    font-weight: 500;
  }
}

// Adjust the input container style to add bottom margin 调整输入容器样式，增加底部间距
.input-container {
  position: relative;
  display: flex;
  align-items: center;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  transition: all 0.3s ease;
  background: #f8fafc;
  padding: 12px 16px;
  margin-bottom: 4px; // Reserve space for error messages 为错误提示留出空间

  &.is-error {
    border-color: #ff4d4f; // Border turns red in error state 错误状态边框变红
  }

  &.focused {
    border-color: #4299e1;
    box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.2);
    background: white;
  }

  i:not(.toggle-password i) {
    margin-right: 12px;
    color: #a0aec0;
    font-size: 18px;
  }

  .toggle-password {
    margin-left: 12px;
    background: none;
    border: none;
    color: #a0aec0;
    cursor: pointer;
    padding: 0;
    font-size: 18px;
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 4px;

    &:hover {
      color: #718096;
      background: rgba(0, 0, 0, 0.05);
    }
  }
}

.input-field {
  flex: 1;
  border: none;
  font-size: 15px;
  background: transparent;
  color: #2d3748;
  padding: 0;

  &:focus {
    outline: none;
  }

  &::placeholder {
    color: #a0aec0;
  }
}

// Element Plus form item styles Element Plus表单项目样式
:deep(.el-form-item) {
  margin-bottom: 0;
  flex: 1;
}

// Position and style of validation error messages 验证错误提示位置和样式
:deep(.el-form-item__error) {
  position: absolute; // Absolute positioning 绝对定位
  top: 100%; // Positioned at the bottom of the container 定位到容器底部
  left: 0; // Left aligned 左对齐
  padding-top: 3px;
  margin-top: 12px; // Increase the spacing appropriately from the input box 与输入框的间距适当拉大
  line-height: 1.2;
  font-size: 12px;
  color: #ff4d4f;
  white-space: nowrap;
}

:deep(.el-input) {
  --el-input-height: auto;
}

:deep(.el-input__wrapper) {
  box-shadow: none !important;
  background: transparent !important;
  padding: 0 !important;

  .el-input__inner {
    height: auto;
    min-height: 24px;
    line-height: 1.5;
  }
}

.submit-btn {
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
  margin-top: 10px;
  height: 50px;

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

.divider {
  margin: 24px 0;

  .divider-line {
    flex: 1;
    height: 1px;
    background: #e2e8f0;
  }

  .divider-text {
    padding: 0 12px;
    color: #a0aec0;
    font-size: 13px;
  }
}

.social-login {
  margin-bottom: 24px;

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

  .social-icons {
    display: flex;
    justify-content: center;
    gap: 16px;
  }
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

.login-text {
  text-align: center;
  color: #718096;
  font-size: 14px;

  .login-link {
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

.success-message {
  background-color: #52c41a !important;
  color: white !important;
}

.error-message {
  background-color: #ff4d4f !important;
  color: white !important;
}

</style>