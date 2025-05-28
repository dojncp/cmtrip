import request from '@/utils/request'

// Backend API address: In `vite.config.js`, `'/api'` will be replaced with `'http://localhost:<port>'`.
// 后端API地址：在vite.config.js中，'/api'将被替换为'http://localhost:<port>'
const API_URL = '/user';

export function login(userName, password) {
    const data = {
        userName: userName,
        password: password,
    }
    return request({
        url: API_URL + '/login',
        headers: {
            "Content-Type": "application/json;charset=utf-8",
        },
        method: 'post',
        data: data
    });
}

export function register(ctUser) {
    console.log("ctUser:", ctUser)
    return request({
        url:  API_URL + '/register',
        method: 'post',
        data: ctUser
    })
}

export function getUserInfo(userId) {
    return request({
        url:  API_URL + '/get-user-info',
        method: 'get',
        params: {
            userId: userId,
        }
    })
}

export function logout() {
    return request({
        url:  API_URL + '/logout',
        method: 'post',
    })
}