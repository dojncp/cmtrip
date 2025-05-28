import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from "@/router";


// Create an axios instance 创建axios实例
const service = axios.create({
    // In axios, the request configuration has a baseURL option, which represents the common part of the request URL
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: '/api',
    // Time Out 超时
    timeout: 10000
});

// Add a response interceptor: all non-200 return values must be treated as errors; otherwise, they will be returned to the frontend as successful responses
// 添加响应拦截器：必须把非200的返回值都视为错误，否则将以成功的形式返回前端
service.interceptors.response.use(
    response => {
        if (response && response.data) {
            // Custom exception values 自定义异常值
            if (response.data.code !== 200) {
                if (response.data.code === 1000) {
                    console.log("Return error code 1000 返回1000错误码")
                    // Clear the token in localStorage 清除 localStorage 中的 token
                    localStorage.removeItem("satoken");
                    router.push('/login');
                    // Return as an error 以错误的形式返回
                    return Promise.reject("Not logged in or login expired, redirect to the login page.\n未登录或登录过期，跳转到登录页");
                } else if (response.data.code === 1001) {
                    console.warn("RuntimeException: ", response.data?.message);
                    ElMessage.error(`Error：${response.data?.message}`);
                    return Promise.reject(response.data?.message);
                } else {
                    console.warn("Other Errors: ", response.data?.message);
                    ElMessage.error(`Error: ${response.data.code} - ${response.data?.message}`);
                    return Promise.reject(response.data?.message);
                }
                const errorMessage = response.data.message;
                return Promise.reject(errorMessage);
            } else {
                return response;
            }
        }
    },
    error => {
        if (error.response) {
            console.log("Enter the error handling process of the frontend response interceptor.\n进入前端响应拦截器的错误处理流程");
            console.log("error.response: ", error.response)
        }
        return Promise.reject(error);
    }
);

export default service
