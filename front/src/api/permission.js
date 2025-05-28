import request from "@/utils/request";

// Backend API address: In `vite.config.js`, `'/api'` will be replaced with `'http://localhost:<port>'`.
// 后端API地址：在vite.config.js中，'/api'将被替换为'http://localhost:<port>'
const API_URL = '/permission';

export function getPermissionList() {
    return request({
       url: API_URL + '/list',
       method: 'get',
    });
}

export function addPermission(ctPermission) {
    return request({
        url: API_URL + '/add',
        method: 'post',
        data: ctPermission
    });
}

export function editPermission(ctPermission) {
    return request({
        url: API_URL + '/edit',
        method: 'post',
        data: ctPermission
    });
}

export function deletePermission(id) {
    return request({
        url: API_URL + '/delete',
        method: 'delete',
        params: {
            id: id
        }
    });
}