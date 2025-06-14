import request from '@/utils/request'

// Backend API address: In `vite.config.js`, `'/api'` will be replaced with `'http://localhost:<port>'`.
// 后端API地址：在vite.config.js中，'/api'将被替换为'http://localhost:<port>'
const API_URL = '/pass';

export function getAllPasses() {
    return request({
        url: API_URL + '/list',
        method: 'GET'
    });
}

export function addPass(data) {
    return request({
        url: API_URL + "/add",
        method: 'POST',
        data: data
    });
}

export function addPassWithImage(passForm, image) {
    const formData = new FormData();
    // transfer JSON data to Blob
    const blob = new Blob (
        [JSON.stringify(passForm)],
        {type:'application/json'}
    );
    formData.append('passForm', blob);
    // append the image file
    if (image instanceof File) {
        formData.append('image', image);
    }
    return request({
        url: API_URL + '/add-image',
        method: 'POST',
        data: formData,
    });
}

export function editPass(data) {
    return request({
        url: API_URL + "/edit",
        method: 'POST',
        data: data
    });
}

export function editPassWithImage(passForm, image) {
    const formData = new FormData();
    // transfer JSON data to Blob
    const blob = new Blob (
        [JSON.stringify(passForm)],
        {type:'application/json'}
    );
    formData.append('passForm', blob);
    // append the image file
    if (image instanceof File) {
        formData.append('image', image);
    }
    return request({
        url: API_URL + '/edit-image',
        method: 'POST',
        data: formData,
    });
}

export function deletePassById(id) {
    return request({
        url: API_URL + '/delete',
        method: 'delete',
        params: {
            id: id
        }
    });
}