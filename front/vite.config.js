import { defineConfig } from 'vite';
import path from 'path'
import vue from '@vitejs/plugin-vue';

// 跨域代理
export default defineConfig(() => {
    return {
        server: {
            host: '0.0.0.0',
            port: 5173,
            open: true, // 自动打开浏览器
            proxy: {
                // 这里的 `/api` 会被代理到后端地址
                '/api': {
                    target: 'http://localhost:8081', // 你的后端服务器地址
                    changeOrigin: true, // 修改请求头中的 origin
                    secure: false, // 如果是https接口，需要配置为true
                    ws: true, // 如果后端支持 WebSocket，可以加上
                    rewrite: (path) => path.replace(/^\/api/, '') // 重写路径，去掉 /api 前缀
                },
            },
        },
        resolve: {
            alias: {
                // 设置别名：这里的 /src 是项目根目录下的 src 文件夹
                '@': path.resolve(__dirname, './src'),
            },
        },
        plugins: [vue()],
    }
});