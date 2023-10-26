import {createRouter, createWebHistory} from 'vue-router'
import {notification} from "ant-design-vue";
import store from "@/store";

const routes = [{
    path: '/login', // name属性可忽略
    component: () => import(/* webpackChunkName: "about" */ '../views/LoginView.vue')
}, {
    path: '/', // name属性可忽略
    component: () => import(/* webpackChunkName: "about" */ '../views/ConsoleView.vue'), // 自定义变量
    meta: {
        // 用于指定是否开启路由登录检验拦截
        // 适用于静态页面，动态页面因为通过Axios拦截器实现动态拦截而不需要指定
        loginRequire: true
    }
}]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL), routes
})

// 路由登录拦截（对于没有请求的静态页面。动态登录拦截通过Axios拦截器实现）
// 同样采用拦截链实现
router.beforeEach((to, from, next) => {
    // 要不要对meta.loginRequire属性做监控拦截
    if (to.matched.some(function (item) {
        console.log(item, "是否需要登录校验：", item.meta.loginRequire || false);
        return item.meta.loginRequire
    }))
    // 需要路由登录检验
    {
        const _member = store.state.member;
        console.log("页面登录校验：", _member);
        if (!_member.token) {
            console.log("登录失效");
            notification.error({description: "登录失效，请重新登录"});
            next('/login');
        } else {
            next();
        }
    }
    // 不需要路由登录检验
    else {
        // 拦截链继续处理
        next();
    }
});

export default router
