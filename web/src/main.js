import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 启动App，启用store、router组件，并将内容渲染到public/index.html中名为app的div内
// 通过 main.js（框架配置入口），将 index.html(html页面入口）和 App.vue（vue页面入口）关联起来
createApp(App).use(store).use(router).mount('#app')