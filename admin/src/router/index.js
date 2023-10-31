import {createRouter, createWebHistory} from 'vue-router'

const routes = [{
    path: '/', // name属性可忽略
    component: () => import(/* webpackChunkName: "about" */ '../views/ConsoleView.vue'),
    children: [
        {
            // 二级路由path=父path+path='/welcome'
            path: 'welcome',
            component: () => import(/* webpackChunkName: "about" */ '../views/ConsoleSubViews/WelcomeView.vue')
        },
        {
            path: 'about',
            component: () => import('../views/AboutView.vue')
        },
        {
            path: 'base/',
            children: [{
                path: 'station',
                component: () => import('../views/business/StationView.vue')
            },
                {
                    path: 'train',
                    component: () => import('../views/business/TrainView.vue')
                },
                {
                    path: 'trainStation',
                    component: () => import('../views/business/TrainStationView.vue')
                },
                {
                    path: 'trainCarriage',
                    component: () => import('../views/business/TrainCarriageView.vue')
                },
                {
                    path: 'trainSeat',
                    component: () => import('../views/business/TrainSeatView.vue')
                },]
        },
        {
            path: 'batch/',
            children: [{
                path: 'job',
                component: () => import('../views/batch/JobView.vue')
            }]
        },]
}, {
    path: '',
    redirect: '/welcome'
},]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL), routes
})

export default router
