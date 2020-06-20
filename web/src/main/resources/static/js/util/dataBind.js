/**
 * 标签池{标签:{内部方法:JS Document方法}}
 */
const tagPool = {
    INPUT: {val: 'value', readOnly: 'readOnly'},
    BUTTON: {val: 'innerHTML'}
}

/**
 * 设置为只读
 * @param pageData 页面数据
 */
function setReadonly(pageData) {
    set(pageData, 'readOnly');
}

/**
 * 设定显示内容(或者输入框的value值)
 * @param pageData 页面数据
 */
function setVal(pageData) {
    set(pageData, 'val');
}

/**
 * 设置属性
 * @param pageData   页面数据
 * @param methodType 方法名称
 */
function set(pageData, methodType) {
    for (let propertyName in pageData) {
        let value = pageData[propertyName];
        let element = document.getElementById(propertyName);
        //定义属性及绑定监听
        defineProperty(pageData, propertyName, element, methodType);
        //立即更新为当前值
        pageData[propertyName] = value;
    }
}

/**
 * 定义属性及绑定监听
 * @param pageData     实体
 * @param propertyName 属性名称
 * @param element      操作元素
 * @param methodType   操作方法
 */
function defineProperty(pageData, propertyName, element, methodType) {
    Object.defineProperty(pageData, propertyName, {
        configurable: true,
        enumerable: true,
        set(newValue) {
            //标签名
            let tagName = element.tagName;
            //从标签库中找到对应标签
            for (let tag in tagPool) {
                if (tag == tagName) {
                    //获取标签库中对应，该标签对应操作赋值方法
                    let methodName = tagPool[tag][methodType];
                    //如果找不到对应方法，则直接使用方法类型名
                    if (isEmpty(methodName)) {
                        methodName = methodType;
                    }
                    alert(methodName);
                    element[methodName] = newValue;
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

