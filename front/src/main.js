import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import i18n from './i18n'
import Antd from 'ant-design-vue'
import { createPinia } from 'pinia';
import ElementPlus, { ElMessage } from 'element-plus';
import 'element-plus/dist/index.css';
import 'ant-design-vue/dist/reset.css';
import {getMenuList} from "@/api/menu";  // Compatible with Ant Design Vue 4.x+ 适用于 Ant Design Vue 4.x+

// Import the Font Awesome icon library 导入Font Awesome图标库
import '@fortawesome/fontawesome-free/css/all.min.css'

const app = createApp(App);
app.use(i18n)
app.use(router);
app.use(Antd)
app.use(createPinia());
app.use(ElementPlus);

// Main entry point of the frontend application 前端主启动项
async function initializeApp() {
    try {
        // Wait for the initial routes to finish loading 等待初始路由加载完成
        await router.isReady();
        // Dynamically load routes 动态加载路由
        const response = await getMenuList();
        const menuList = response.data?.data || [];
        const secondLevelRoutes = router.getSecondLevelRoutes(menuList);
        secondLevelRoutes.forEach(route => {
            router.addRoute('Layout', route);
        });
        // Wait for the loading animation to finish 等待加载动画完成
        if (!window.loadingAnimationComplete) {
            await new Promise(resolve => {
                document.addEventListener('app-loading-complete', resolve);
            });
        }
    } catch (error) {
        console.error("Application initial failed:", error);
        ElMessage.error("Application initial failed!");
    } finally {
        app.mount('#app');
    }
}

initializeApp();

// Globally register message notification style (ElMessage) 全局注册消息提醒样式（ElMessage）
app.config.globalProperties.$message = ElMessage;
