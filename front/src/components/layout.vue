<template>
  <div class="layout">
    <header class="header">
      <router-link to="/home" class="logo-wrapper">
        <h2 class="logo"
            v-on:contextmenu.prevent
            v-on:dragstart.prevent
            v-on:selectstart.prevent>
          <img src="/favicon.ico"
               alt="Logo"
               draggable="false"
               v-on:contextmenu.prevent
               v-on:dragstart.prevent
               v-on:selectstart.prevent>
          CM-TRIP: Support Your Trips
        </h2>
      </router-link>
      <div>
        {{ $t('header.language') }}: <LanguageSwitcher />
      </div>
      <div>
        <el-text>{{ $t('header.currentUser') }}: {{ userName }}</el-text>
        <button class="logout-button" @click="logout">
          {{ $t('header.logout') }}
        </button>
      </div>
    </header>

    <nav class="navbar">
      <ul class="menu">
        <li v-for="menu in topMenus" :key="menu.id" class="menu-item">
          <template v-if="menu.children && menu.children.length">
            <span>{{ menu.menuName }}</span>
            <ul class="submenu">
              <li v-for="sub in menu.children" :key="sub.id">
                <router-link :to="sub.path">{{ sub.menuName }}</router-link>
              </li>
            </ul>
          </template>
          <template v-else>
            <router-link :to="menu.path" class="top-level-link">
              {{ menu.menuName }}
            </router-link>
          </template>
        </li>
      </ul>
    </nav>

    <div class="content">
      <router-view />
    </div>

    <footer class="footer">
      <div class="footer-content">
        <div class="footer-section">
          <h3>{{ $t('footer.aboutUs') }}</h3>
          <p>CMTRIP: {{ $t('footer.slogan') }}</p>
        </div>
        <div class="footer-section">
          <h3>{{ $t('footer.github') }}</h3>
          <p>https://github.com/dojncp/cmtrip</p>
        </div>
        <div class="footer-section">
          <h3>{{ $t('footer.email') }}</h3>
          <p>dojncp@outlook.com</p>
        </div>
        <div class="footer-section">
          <h3>{{ $t('footer.fastLinks') }}</h3>
          <ul>
            <li><router-link to="/privacy">{{ $t('footer.privacy') }}</router-link></li>
            <li><router-link to="/terms">{{ $t('footer.terms') }}</router-link></li>
            <li><router-link to="/help">{{ $t('footer.help') }}</router-link></li>
          </ul>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2025 CMTRIP. {{ $t('footer.rights') }}</p>
        <div class="social-icons">
          <a href="https://github.com/dojncp/cmtrip"><i class="fab fa-github"></i></a>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import LanguageSwitcher from '@/components/language_switcher.vue'
import { getMenuList } from "@/api/menu";
import { logout as apiLogout } from "@/api/login";
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from "element-plus";

// Reactive data 响应式数据
const userName = ref('');
const topMenus = ref([]);
const route = useRoute();
const router = useRouter();

// Initialization logic 初始化逻辑
console.log("当前token：", localStorage.getItem("satoken"));
console.log("当前用户：", localStorage.getItem("userName"));

// Method definition 方法定义
const fetchMenus = async () => {
  const token = localStorage.getItem("satoken");
  if (token) {
    try {
      const response = await getMenuList();
      topMenus.value = response.data?.data || [];
    } catch (error) {
      console.error("Menu retrieve failed", error);
    }
  }
};

const getUserName = () => {
  userName.value = localStorage.getItem('userName') || '';
};

const handleLogout = () => {
  ElMessageBox.confirm(
      'Do you want to log out?',
      'Log Out Confirm',
      {
        confirmButtonText: 'Log out',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        apiLogout();
        localStorage.removeItem("satoken");
        localStorage.removeItem("userName");
        router.push('/login');
        ElMessage({
          type: 'success',
          message: 'Logout successfully!',
        });
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Log out canceled',
        });
      });
};

// Lifecycle hook 生命周期钩子
onMounted(() => {
  fetchMenus();
  getUserName();
});

// Automatically exposed to the template (the template can directly access these refs and methods)
// 自动暴露给模板的内容（模板可直接访问这些 ref 和方法）
defineExpose({
  topMenus,
  logout: handleLogout,
  userName
});
</script>

<style scoped lang="scss">
.layout {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  min-height: 100vh;
  position: relative;
  background: linear-gradient(135deg, #f5f7fa 0%, #c8d7c3 100%);
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 90vw;
  padding: 20px;
  background-color: white;
  margin: 20px;
  border-radius: 16px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.05);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.1);

  &:hover {
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
    transform: translateY(-5px);
  }
}

.header .logo {
  margin: 10px 10px;
  font-size: 40px;
  color: #2d3748;
  font-weight: 700;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  -webkit-touch-callout: none;
}

.logo {
  display: flex;
  align-items: center;
  margin: 10px 20px;
  color: #2d3748;
}

.logo img {
  height: 2em;
  margin-right: 10px;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  pointer-events: none;
}

.logo-wrapper {
  text-decoration: none;
  display: inline-block;
}

.logo-wrapper:hover {
  text-decoration: none;
}

.logout-button {
  margin: 10px 20px;
  padding: 10px 15px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  background: linear-gradient(135deg, #e596ad 0%, #e53434 100%);
  color: white;
  border: none;
  border-radius: 8px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 7px 14px rgba(0, 0, 0, 0.15);
  }
}

.navbar {
  width: 90vw;
  background: linear-gradient(135deg, rgba(114, 159, 91, 0.76) 0%, rgba(41, 110, 26, 0.67) 100%);
  padding: 0;
  display: flex;
  justify-content: center;
  z-index: 1000;
  border-radius: 16px;
  margin-bottom: 20px;
  margin-left: auto;
  margin-right: auto;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.05);
}

.menu {
  display: flex;
  justify-content: space-around;
  width: 100%;
  list-style: none;
  padding: 0;
  margin: 0;
}

.menu-item {
  flex: 1;
  text-align: center;
  padding: 14px 15px;
  cursor: pointer;
  color: white;
  font-size: 15px;
  font-weight: 500;
  position: relative;
  transition: all 0.3s ease;

  &[aria-haspopup="true"]::after {
    content: "▼";
    font-size: 10px;
    margin-left: 6px;
  }

  &:hover .submenu {
    display: block;
  }
}

.submenu {
  max-width: 90vw;
  overflow-x: auto;
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(255, 255, 255, 0.95);
  color: #2d3748;
  list-style: none;
  padding: 5px 0;
  border-radius: 8px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
  display: none; /* 默认隐藏 */
  width: 160px;
  z-index: 1001;
  backdrop-filter: blur(10px);

  li {
    transition: background-color 0.3s ease;

    &:hover {
      background-color: #f8fafc !important;
    }

    a {
      text-decoration: none !important;
      color: #4a5568;
      display: block;
      width: 100%;
      height: 100%;
      padding: 10px 15px;
      box-sizing: border-box;
      font-size: 14px;

      &:hover {
        color: #4299e1;
      }
    }
  }
}

.top-level-link {
  display: block;
  color: white;
  font-size: inherit;
  text-decoration: none !important;
  transition: color 0.3s;
}

.content {
  width: 90vw;
  padding: 20px;
  background-color: white;
  border-radius: 16px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.05);
  margin-bottom: 10px;
}

.footer {
  width: 90vw;
  background-color: white;
  color: #2d3748;
  padding: 40px 0 0;
  margin-top: 10px;
  margin-bottom: 20px;
  border-radius: 16px;
  box-shadow: 0 -15px 30px rgba(0, 0, 0, 0.05);
}

.footer-content {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  padding: 0 30px;
}

.footer-section {
  flex: 1;
  min-width: 250px;
  margin-bottom: 20px;
  padding: 0 15px;
}

.footer-section h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
  position: relative;
  padding-bottom: 10px;
  color: #2d3748;
}

.footer-section h3::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 50px;
  height: 2px;
  background: linear-gradient(135deg, #64d369 0%, #0e7c14 100%);
}

.footer-section p, .footer-section li {
  font-size: 14px;
  margin-bottom: 10px;
  line-height: 1.6;
  color: #718096;
}

.footer-section a {
  color: #718096;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-section a:hover {
  color: #4299e1;
}

.footer-bottom {
  border-top: 1px solid #e2e8f0;
  padding: 20px 40px;
  text-align: center;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.footer-bottom p {
  font-size: 14px;
  margin: 0;
  color: #718096;
}

.social-icons {
  display: flex;
  gap: 15px;
}

.social-icons a {
  color: #718096;
  font-size: 20px;
  transition: color 0.3s;
}

.social-icons a:hover {
  color: #4299e1;
}
</style>
