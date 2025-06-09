import request from '@/utils/request'

// Backend API address: In `vite.config.js`, `'/api'` will be replaced with `'http://localhost:<port>'`.
// 后端API地址：在vite.config.js中，'/api'将被替换为'http://localhost:<port>'
const API_URL = '/pass-entity';

export function getMyPassEntities() {
    return request({
        url: API_URL + '/list-my-all',
        method: 'GET',
    })
}

export function getMyPassEntitiesByPassId(passId) {
    return request({
        url: API_URL + '/list-my-all-by-pass-id',
        method: 'GET',
        params: {
            passId: passId
        }
    });
}

export function addPassEntity(data) {
    return request({
        url: API_URL + "/add",
        method: 'POST',
        data: data
    });
}

export function editPassEntity(data) {
    return request({
        url: API_URL + "/edit",
        method: 'POST',
        data: data
    });
}

export function deletePassEntityById(id) {
    return request({
        url: API_URL + '/delete',
        method: 'delete',
        params: {
            id: id
        }
    });
}