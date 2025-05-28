import request from "@/utils/request";

// Backend API address: In `vite.config.js`, `'/api'` will be replaced with `'http://localhost:<port>'`.
// 后端API地址：在vite.config.js中，'/api'将被替换为'http://localhost:<port>'
const API_URL = '/role';

export function getRoleList() {
    return request({
        url: API_URL + '/list',
        method: 'get',
    });
}

export function getRoleListBesidesSuperAdmin() {
    return request({
        url: API_URL + '/list-besides-superAdmin',
        method: 'get',
    });
}

export function addRole(ctRole) {
    return request({
        url: API_URL + '/add',
        method: 'post',
        data: ctRole
    });
}

export function editRole(ctRole) {
    return request({
        url: API_URL + '/edit',
        method: 'post',
        data: ctRole
    });
}

export function deleteRole(id) {
    return request({
        url: API_URL + '/delete',
        method: 'delete',
        params: {
            id: id
        }
    });
}

export function getRolesByUserId(id) {
    return request({
        url: API_URL + '/get-list-by-user-id',
        method: 'get',
        params: {
            userId: id,
        }
    })
}

export function updateRolePermissions(data) {
    return request({
        url: API_URL + "/update-role-permissions",
        method: 'post',
        data: data
    });
}

export function getRolePermissionNames(id) {
    return request({
        url: API_URL + '/get-role-permissions',
        method: 'get',
        params: {
            id: id
        }
    });
}

