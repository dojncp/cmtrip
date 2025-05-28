import request from '@/utils/request'

// Backend API address: In `vite.config.js`, `'/api'` will be replaced with `'http://localhost:<port>'`.
// 后端API地址：在vite.config.js中，'/api'将被替换为'http://localhost:<port>'
const API_URL = '/action';

export function getMyActionsByTripId(tripId) {
    return request({
        url: API_URL + '/list-by-trip-id',
        method: 'GET',
        params:
            { tripId: tripId }
    });
}

export function addAction(tripId, ctAction) {
    return request({
        url: API_URL + "/add",
        method: "POST",
        params:
            { tripId: tripId },
        data: ctAction
    })
}

export function addActionWithImage(tripId, actionForm, image) {
    const formData = new FormData();
    // transfer JSON data to Blob
    const blob = new Blob(
        [JSON.stringify(actionForm)],
        {type:'application/json'}
    );
    formData.append('actionForm', blob);
    // append the image file
    if (image instanceof File) {
        formData.append('image', image);
    }
    return request({
        url: API_URL + '/add-image',
        method: 'POST',
        params: {
            tripId: tripId
        },
        data: formData
    });
}

export function editAction(ctAction) {
    return request({
       url: API_URL + "/edit",
       method: 'POST',
       data: ctAction
    });
}

export function editActionWithImage(actionForm, image) {
    const formData = new FormData();
    // transfer JSON data to Blob
    const blob = new Blob (
        [JSON.stringify(actionForm)],
        {type:'application/json'}
    );
    formData.append('actionForm', blob);
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

export function deleteActionById(id) {
    return request({
        url: API_URL + '/delete',
        method: 'delete',
        params: {
            id: id
        }
    });
}

