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
function add_user(userName, userPassword) {
    var formData = {userName: userName, userPassword: userPassword};
    server_user_request(ADD_PATH, formData);
}

/**
 * 用户服务请求
 * @param path
 * @param formData
 */
function server_user_request(path, formData) {
    server_request(MODULE_PATH + path, formData);
}