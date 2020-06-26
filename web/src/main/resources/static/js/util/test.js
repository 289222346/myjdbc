const WEB_CONTROLLER = '/api/public';
const TEST = '/test';
const HELLO_HTML2 = '/hello2';

/**
 * 就一个测试接口
 *
 * @author 陈文
 * @date 2020/06/27  22:38
 */
function test(formData) {
    serverRequestMirror(TEST, formData);
}

function helloHtml2(formData) {
    serverRequestMirror(HELLO_HTML2, formData);
}

function serverRequestMirror(path, formData) {
    serverRequest(WEB_CONTROLLER + path, formData);
}
