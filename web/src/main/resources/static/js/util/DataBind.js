/***
 *数据绑定
 *
 *使用方式： {标签ID: {标签属性: 属性值}}
 *代码示例：
 * --JavaScript部分
 *  const data1 = {
 *       userName: {val: 'admin', readOnly: true},
 *       password: {val: 123456},
 *       login: {val: '提交按钮'}
 *   };
 *   loadData(data1);
 *
 * --HTML部分
 *  <input id="userName" type="text" placeholder="请输入用户名">
 *  <input id="password" type="password" placeholder="请输入密码">
 *  <button id="login" type="button"></button>
 * @author 陈文
 */

/**
 * 标签池{标签:{内部方法:JS Document方法}}
 */
const tagPool = {
    INPUT: {val: 'value', readOnly: 'readOnly'},
    BUTTON: {val: 'innerHTML'}
}

/**
 * 加载数据
 * @param pageData   页面数据
 */
function loadData(pageData) {
    //解析数据,获得实体
    for (let model in pageData) {
        //获取属性集合
        let properties = pageData[model];
        //解析属性方法
        for (let property in properties) {
            //方法值
            let value = properties[property];
            //获取页面元素(实体的映射)
            let element = document.getElementById(model);
            //定义属性及绑定监听
            defineProperty(pageData[model], property, element, property);
            //立即更新为当前值
            properties[property] = value;
        }
    }
}

/**
 * 定义属性及绑定监听
 * @param pageData     页面数据
 * @param model        实体(名称)
 * @param element      操作元素
 * @param property     操作属性
 */
function defineProperty(pageData, model, element, property) {
    Object.defineProperty(pageData, model, {
        configurable: true,
        enumerable: true,
        set(newValue) {
            //标签名
            let tagName = element.tagName;
            //从标签库中找到对应标签
            for (let tag in tagPool) {
                if (tag == tagName) {
                    //获取标签库中对应属性
                    let propertyName = tagPool[tag][property];
                    //如果找不到对应方法，则直接使用方法类型名
                    if (isEmpty(propertyName)) {
                        propertyName = property;
                    }
                    //操作DOM节点，修改对应标签值的指定属性值
                    element[propertyName] = newValue;
                    break;
                }
            }
        }
    });
}

function isEmpty(val) {
    if (typeof (val) != "undefined" && val != null && val != '') {
        return false;
    }
    return true;
}

function isNotEmpty(val) {
    !isEmpty(val);
}

