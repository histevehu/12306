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
                component: () => import('../views/base/StationView.vue')
            },
                {
                    path: 'train',
                    component: () => import('../views/base/TrainView.vue')
                },
                {
                    path: 'trainStation',
                    component: () => import('../views/base/TrainStationView.vue')
                },
                {
                    path: 'trainCarriage',
                    component: () => import('../views/base/TrainCarriageView.vue')
                },
                {
                    path: 'trainSeat',
                    component: () => import('../views/base/TrainSeatView.vue')
                },]
        },
        {
            path: 'business/',
            children: [{
                path: 'dailyTrain',
                component: () => import('../views/business/DailyTrainView.vue')
            }, {
                path: 'dailyTrainStation',
                component: () => import('../views/business/DailyTrainStationView.vue')
            }, {
                path: 'dailyTrainCarriage',
                component: () => import('../views/business/DailyTrainCarriageView.vue')
            }, {
                path: 'dailyTrainSeat',
                component: () => import('../views/business/DailyTrainSeatView.vue')
            }, {
                path: 'dailyTrainTicket',
                component: () => import('../views/business/DailyTrainTicketView.vue')
            }, {
                path: 'confirmOrder',
                component: () => import('../views/business/ConfirmOrderView.vue')
            }]
        }, {
            path: 'member/',
            children: [{
                path: 'ticket',
                component: () => import('../views/member/TicketView.vue')
            }]
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
