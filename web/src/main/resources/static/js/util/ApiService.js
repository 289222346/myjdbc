/**
 * 模块路径
 * @type {string}
 */
const API_SERVICE = "/api/public";

/**
 * 你好哦
 * @type {string}
 */
const HELLO = "/hello";

/**
 * 添加用户
 */
function hello() {
    serverRequestMirror(HELLO);
}

/**
 * 用户服务请求
 * @param path
 * @param formData
 */
function serverRequestMirror(path, formData) {
    serverRequest(API_SERVICE + path, formData);
}