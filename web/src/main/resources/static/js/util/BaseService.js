/**
 * 发送请求的地址
 * @type {string}
 */
const SERVER_ADDRESS = "/web";

const BASE_SERVICE = "/base";

/**
 * 请求方式（post或get）默认为get
 * 此接口修改默认为post
 * @type {string}
 */
const serverType = "post";

/**
 * 服务器返回数据类型
 * @type {string}
 */
const serverDataType = "json";

/**
 * 默认设置为true，所有请求均为异步请求。
 * 如果需要发送同步请求，请将此选项设置为false。
 * 注意，同步请求将锁住浏览器，用户其他操作必须等待请求完成才可以执行。
 * @type {boolean}
 */
const serverAsync = true;

/**
 * 基本服务数据
 * @type {{ajaxDataResultMsg: {val: null}, ajaxDataCode: {val: null}}}
 */
const baseServiceData = {
    ajaxDataCode: {val: null},
    ajaxDataResultMsg: {val: null}
    // ajaxDataCode: {code: null, resultMsg: null}
};

/**
 * 通用服务调用接口
 * @param apiName 接口名称
 * @param formData 调用参数
 */
function serverRequest(apiName, formData) {
    $.ajax({
        url: SERVER_ADDRESS + apiName,
        type: serverType,
        async: serverAsync,
        dataType: serverDataType,
        data: formData,
        success: function (result) {
            serverRequestCallback(result);
        },
        error: function (result) {
            serverRequestCallback(result);
        }
    });
}

/**
 * 通用服务调用接口本地镜像（对路径进行处理）
 * @param path
 * @param formData
 */
function serverRequestMirror(path, formData) {
    serverRequest(BASE_SERVICE + path, formData);
}

/**
 * 通用服务调用接口-回调函数
 * (不论接口调用是否正常，均会由此函数返回)
 * @param data 返回数据
 */
function serverRequestCallback(result) {
    console.log(result);
    showResult(result.code, result.resultMsg);
}

/**
 * 显示返回结果
 */
function showResult(code, resultMsg) {
    baseServiceData.ajaxDataCode.val = code;
    baseServiceData.ajaxDataResultMsg.val = resultMsg;
}


window.onload = function () {
    alert("页面加载完成====》onload");
    loadData(baseServiceData)
}



