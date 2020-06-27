const PUBLIC_INFO = '/api/public';
const PUBLIC_INFO1 = '/api';
const HELLO2 = '/hello2';
const CCC = '/ccc';
const HELLO_HTML = '/hello';

/**
 * 第二个注释
 *
 * @return
 */
function hello2(formData) {
    serverRequestMirror(HELLO2, formData);
}

/**
 * 第三个注释
 *
 * @return
 */
function ccc(formData) {
    serverRequestMirror(CCC, formData);
}

/**
 * 第一个注释
 *
 * @return
 */
function helloHtml(formData) {
    serverRequestMirror(HELLO_HTML, formData);
}

function serverRequestMirror(path, formData) {
    serverRequest(PUBLIC_INFO + path, formData);
}