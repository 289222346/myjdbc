const PUBLIC_INFO = '/api/public';
const PUBLIC_INFO1 = '/api';
const HELLO_HTML = '/hello';
const HELLO2 = '/hello2';
const CCC = '/ccc';

/**
 * 第一个注释
 *
 * @return
 */
function helloHtml(formData) {
    serverRequestMirror(HELLO_HTML, formData);
    {
        let s = 4;
    }
    alert("chengg");
}

/**
 * 第二个注释
 *
 * @return
 */
function hello2(formData) {
    serverRequestMirror(HELLO2, formData);
    {
        let s = 4;
    }
    alert("chengg");
}

/**
 *
 * @return
 */
function ccc(formData) {
    serverRequestMirror(CCC, formData);
    {
        let s = 4;
    }
    alert("chengg");
}

function serverRequestMirror(path, formData) {
    serverRequest(PUBLIC_INFO + path, formData);
}
