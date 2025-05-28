import request from '@/utils/request'

// Backend API address: In `vite.config.js`, `'/api'` will be replaced with `'http://localhost:<port>'`.
// 后端API地址：在vite.config.js中，'/api'将被替换为'http://localhost:<port>'
const API_URL = '/menu';

export function getMenuList(query) {
    return request({
        url: API_URL + '/list',
        method: 'get',
        params: query
    });
}

export function addMenu(menuData) {
    return request({
        url: API_URL + '/add',
        method: 'post',
        data: menuData
    });
}

export function updateMenu(id, menuData) {
    return request({
        url: API_URL + '/update/' + id,
        method: 'put',
        data: menuData
    });
}

export function deleteMenu(id) {
    return request({
        url: API_URL + '/delete/' + id,
        method: 'delete'
    });
}