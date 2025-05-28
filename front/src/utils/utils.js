// utils/utils.js

const IMAGE_BASE_URL = import.meta.env.VITE_IMAGE_BASE_URL;

/**
 * Concatenate the full URL of the image
 * 拼接图片的完整 URL
 * @param {string} path - The relative path returned from the database, e.g. /profile/xxx.jpeg
 * 数据库返回的相对路径，如 /profile/xxx.jpeg
 * @returns {string} Full image
 * URL完整图片地址
 */
export function getFullImageUrl(path) {
    if (!path) return '';
    if (path.startsWith('http') || path.startsWith('//')) return path;
    return IMAGE_BASE_URL + path;
}

/**
 * Date format conversion utility
 * Date格式转换工具
 * @param dateString
 * @returns {string}
 */
export function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString();
}

/**
 * Datetime format conversion utility
 * Datetime格式转换工具
 * @param dateTimeString
 * @returns {string}
 */
export function formatDateTime(dateTimeString) {
    if (!dateTimeString) return '';
    const date = new Date(dateTimeString);
    return date.toLocaleString();
}

/**
 * Fix the frontend time picker’s time to prevent automatic time conversion during transmission
 * 将前端时间选择器（picker）的时间固定化，避免传递时作了自动时间转换
 * @param date
 * @returns {string|null}
 */
export function formatDateTimeWithoutTimezone(date) {
    if (!date) return null;
    const d = new Date(date);
    const pad = (n) => n.toString().padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
};

/**
 * Fix the frontend date picker’s date to prevent automatic time conversion during transmission
 * 将前端日期选择器（picker）的时间固定化，避免传递时作了自动时间转换
 * @param date
 * @returns {string|null}
 */
export function formatDateWithoutTimezone(date) {
    if (!date) return null;
    const d = new Date(date);
    const pad = (n) => n.toString().padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}`;
};
