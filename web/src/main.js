import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// 引入 Ant Design
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue'
// 引入 Axios
import axios from "axios";

// 启动App，启用store、router组件，并将内容渲染到public/index.html中名为app的div内
// 通过 main.js（框架配置入口），将 index.html(html页面入口）和 App.vue（vue页面入口）关联起来
const app = createApp(App);
app.use(Antd).use(store).use(router).mount('#app')

// 注册Ant Design Icon组件
const icons = Icons
for (const i in icons) {
    app.component(i, icons[i])
}

// Axios拦截器配置:
// 1.打印请求和返回结果的日志
// 2.添加JWT到请求的header中
// 3.动态登录拦截：若返回结果显示登录失效则自动跳转到登录页（静态登录拦截通过路由登录拦截实现）
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    const _token = store.state.member.token;
    if (_token) {
        config.headers.token = _token;
        console.log("请求headers添加token:", _token);
    }
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    const response = error.response;
    const status = response.status;
    if (status === 401) {
        // HTTP状态码是401，登录失效，跳转到登录页
        console.log("未登录或登录超时，跳到登录页");
        store.commit("setMember", {});
        // notification.error({description: "登录失效，请重新登录"});
        router.push('/login');
    }
    return Promise.reject(error);
});
// 读取环境配置文件
// package.json的scripts中指定了--mode dev，则自动读取web根目录中的.env.dev配置文件
console.log('环境：', process.env.NODE_ENV);
console.log('服务端：', process.env.VUE_APP_SERVER);
// 自动设置Axios的Base URL
axios.defaults.baseURL = process.env.VUE_APP_SERVER;