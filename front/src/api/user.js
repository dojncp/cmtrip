import request from '@/utils/request'

// Backend API address: In `vite.config.js`, `'/api'` will be replaced with `'http://localhost:<port>'`.
// 后端API地址：在vite.config.js中，'/api'将被替换为'http://localhost:<port>'
const API_URL = '/user';

export function getUserList(query) {
    return request({
        url: API_URL + '/list',
        method: 'get',
        params: query
    });
}

export function getUserRoleNames(id) {
    return request({
        url: API_URL + '/get-user-roles',
        method: 'get',
        params: {
            id: id,
        }
    });
}

export function updateUserRoles(data) {
    return request({
        url: API_URL + "/update-user-roles",
        method: 'post',
        data: data
    });
}

export function getUserPermissionNames(id) {
    return request({
        url: API_URL + '/get-user-permissions',
        method: 'get',
        params: {
            id: id,
        }
    });
}

export function deleteUser(id) {
    return request({
        url: API_URL + '/delete',
        method: 'delete',
        params: {
            id: id,
        }
    });
}