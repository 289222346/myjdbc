const USER_CONTROLLER = '/console/user';
const ADD = '/add';

/**
 * 新增用户
 *
 * @param baseUser 用户实体
 */
function add(userName, userPassword) {
    let formData = {userName: userName, userPassword: userPassword};
    serverRequestMirror(ADD, formData);
}

function serverRequestMirror(path, formData) {
    serverRequest(USER_CONTROLLER + path, formData);
}

