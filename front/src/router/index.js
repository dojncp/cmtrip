import { createRouter, createWebHistory } from 'vue-router';
import { getMenuList } from '@/api/menu';

import login from '@/views/user/login.vue'
import register from '@/views/user/register.vue'
import forgetPassword from '@/views/user/forget_password.vue'
import home from '@/views/home.vue'
import layout from "@/components/layout.vue";

// Static routes (routes that require login to access have been removed) 静态路由（移除了需要登录才能访问的路由）
const routes = [
    {
        path: '/',
        redirect: '/home'
    },
    {
        path: '/login',
        name: 'Login',
        component: login
    },
    {
        path: '/register',
        name: 'Register',
        component: register
    },
    {
        path: '/forget-password',
        name: 'ForgetPassword',
        component: forgetPassword
    },
    {
        // First, build the basic layout 先构建基本layout
        path: '/',
        component: layout,
        name: 'Layout',
        children: [
            {
                path: '/home',
                name: 'Home',
                component: home
            },
        ]
    },
];

// Create a Router instance (HTML5 mode) 创建Router实例（HTML5模式）
const router = createRouter({
    history: createWebHistory(),
    routes,
});
// Mount the `getSecondLevelRoutes` function from this file to the router instance for external access
// 将本文件下方的getSecondLevelRoutes函数挂载至router实例，以便外部访问
// Expose the loadRoutes function to the router instance 将 loadRoutes 函数暴露给 router 实例
router.getSecondLevelRoutes = getSecondLevelRoutes;

// Routes that require login to access 需要登录后才能访问的路由
const authRoutes = [
    {
        // Privacy Policy 隐私政策
        path: '/privacy',
        name: 'Privacy',
        component: () => import('@/views/fast_links/privacy.vue')
    },
    {
        // Terms of Service 服务条款
        path: '/terms',
        name: 'Terms',
        component: () => import('@/views/fast_links/terms.vue')
    },
    {
        // Help Center 帮助中心
        path: '/help',
        name: 'Help',
        component: () => import('@/views/fast_links/help.vue')
    }
];

// Import all `.vue` files under the `views` directory to enable dynamic path-based component loading
// 引入views下所有vue文件，以便按动态路径加载组件
function loadComponent(component) {
    const modules = import.meta.glob('../views/**/*.vue');
    return modules[`../views/${component}.vue`]
        || (() => import('../views/404.vue'));
}

// Generate a dynamic routing object based on the `menuList` fetched from the backend
// 根据从后端读到的menuList，制作出一个动态路由对象
function getSecondLevelRoutes(menuList) {
    let dynamicRoutes = [];
    menuList.forEach(menu => {
        if (menu.children && menu.children.length > 0) {
            // When there are children 有children的时候
            menu.children.forEach(subMenu => {
                dynamicRoutes.push({
                    path: subMenu.path, // 直接使用二级菜单的 path
                    name: subMenu.menuName,
                    component: loadComponent(subMenu.component),
                    meta: { title: subMenu.menuName }
                });
            });
        } else {
            // When there are no children 没有children的时候
            dynamicRoutes.push({
                path: menu.path,
                name: menu.menuName,
                component: loadComponent(menu.component),
                meta: { title: menu.menuName}
            });
        }
    });
    return dynamicRoutes;
}

// Dynamic routing related configuration 动态路由相关配置
// Add a flag for dynamic routes 添加动态路由的标记
let dynamicRoutesAdded = false;
// Route guard whitelist 路由守卫白名单
const WHITE_LIST = ['/login', '/register', '/forget-password'];

// Route guard: add routes that can be accessed without login
// 路由守卫：添加不需要登录即可访问的路由
router.beforeEach(async (to, from, next) => {
    // Check the token 检查token
    const token = localStorage.getItem('satoken');
    // Whitelist paths are allowed directly 白名单路径直接放行
    if (WHITE_LIST.includes(to.path)) {
        return next();
    }
    // If there is no token, regardless of whether the accessed page is a 404 page or not, navigate directly to the login page
    // 如果没有token，不管访问的是不是404的页面，都直接导航至登录页
    if (!token) {
        localStorage.removeItem('satoken');
        return next('/login');
    }
    // Only continue if there is a token 有token，才继续
    if (!dynamicRoutesAdded) {
        try {
            const response = await getMenuList();
            const menuList = response.data?.data || [];
            // Dynamic route loading 动态路由读取
            const dynamicRoutes = getSecondLevelRoutes(menuList);
            // Add routes that require login to access 添加需要登录才能访问的路由
            authRoutes.forEach(route => {
                router.addRoute(route);
            });
            // Add dynamic routes 动态路由添加
            dynamicRoutes.forEach(route => {
                router.addRoute('Layout', route);
            });
            // Add a fallback (404 route matching all nonexistent routes) only after login and dynamic routes have been added
            // 仅在登录并添加动态路由后才添加兜底（匹配所有不存在路由）的404路由
            router.addRoute({
                path: '/:pathMatch(.*)*',
                name: 'NotFound',
                component: () => import('@/views/404.vue'),
                meta: { anonymous: true }
            });
            dynamicRoutesAdded = true;
            return next({ ...to, replace: true });
        } catch (error) {
            console.error('Menu loading failed.', error);
            next('/home');
        }
    } else {
        next();
    }
});

// Export the Router instance for import and registration in main.js
// 导出Router实例，供main.js中引用注册
export default router;