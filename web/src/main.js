import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// 引入 Ant Design
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue'

// 启动App，启用store、router组件，并将内容渲染到public/index.html中名为app的div内
// 通过 main.js（框架配置入口），将 index.html(html页面入口）和 App.vue（vue页面入口）关联起来
const app = createApp(App);
app.use(Antd).use(store).use(router).mount('#app')

// 注册组件
const icons = Icons
for (const i in icons) {
    app.component(i, icons[i])
}