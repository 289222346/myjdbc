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
 * @param path     接口路径(名称)
 * @param formData 调用参数
 */
function serverRequestMirror(path, formData) {
    serverRequest(API_SERVICE + path, formData);
}