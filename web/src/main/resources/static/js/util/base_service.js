//基础服务

/**
 * 发送请求的地址
 * @type {string}
 */
var serverAddress = "http://127.0.0.1:9999/web";

/**
 * 请求方式（post或get）默认为get
 * 此接口修改默认为post
 * @type {string}
 */
var serverType = "post";

/**
 * 服务器返回数据类型
 * @type {string}
 */
var serverDataType = "json";

/**
 * 默认设置为true，所有请求均为异步请求。
 * 如果需要发送同步请求，请将此选项设置为false。
 * 注意，同步请求将锁住浏览器，用户其他操作必须等待请求完成才可以执行。
 * @type {boolean}
 */
var serverAsync = true;

/**
 * 通用服务调用接口
 * @param apiName 接口名称
 * @param formData 调用参数
 */
function server_request(apiName, formData) {
    $.ajax({
        url: serverAddress + apiName,
        type: serverType,
        async: serverAsync,
        dataType: serverDataType,
        data: formData,
        success: function (result) {
            server_request_callback(result);
        },
        error: function (data) {
            server_request_callback(data);
        }
    });
}

/**
 * 通用服务调用接口-回调函数
 * (不论接口调用是否正常，均会由此函数返回)
 * @param data 返回数据
 */
function server_request_callback(data) {
    console.log(data);
}

