//用户服务

/**
 * 模块路径
 * @type {string}
 */
const MODULE_PATH = "/console/user";

/**
 * 添加用户路径
 * @type {string}
 */
const ADD_PATH = "/add";

/**
 * 添加用户
 */
function addUser(userName, userPassword) {
    let formData = {userName: userName, userPassword: userPassword};
    serverRequestCallback(ADD_PATH, formData);
}

/**
 * 用户服务请求
 * @param path
 * @param formData
 */
function serverRequestCallback(path, formData) {
    serverRequest(MODULE_PATH + path, formData);
}