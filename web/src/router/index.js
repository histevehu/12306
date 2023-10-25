import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    {
        path: '/login',
        // name属性可忽略
        component: () => import(/* webpackChunkName: "about" */ '../views/LoginView.vue')
    },
    {
        path: '/',
        // name属性可忽略
        component: () => import(/* webpackChunkName: "about" */ '../views/ConsoleView.vue')
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
