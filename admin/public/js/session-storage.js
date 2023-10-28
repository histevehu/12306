// 所有的session key都在这里统一定义，可以避免多个功能使用同一个key
SESSION_ORDER = "SESSION_ORDER";
SESSION_TICKET_PARAMS = "SESSION_TICKET_PARAMS";

SessionStorage = {
    get: function (key) {
        // sessionStorage是HTML5自带函数，用于会话缓存，若浏览器关闭后失效
        // localStorage则在浏览器关闭后仍保留
        var v = sessionStorage.getItem(key);
        if (v && typeof(v) !== "undefined" && v !== "undefined") {
            return JSON.parse(v);
        }
    },
    set: function (key, data) {
        // 对象保存进缓存需要就行序列化，同理读取时需要反序列化
        sessionStorage.setItem(key, JSON.stringify(data));
    },
    remove: function (key) {
        sessionStorage.removeItem(key);
    },
    clearAll: function () {
        sessionStorage.clear();
    }
};
