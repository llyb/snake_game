import { createRouter, createWebHistory } from 'vue-router';
import NotFoundIndexView from '@/views/error/NotFoundIndexView.vue';
import PkIndexView from '@/views/pk/PkIndexView.vue';
import RankListIndexView from '@/views/ranklist/RankListIndexView.vue';
import RecordIndexView from '@/views/record/RecordIndexView.vue';
import UserBotIndexView from '@/views/user/bot/UserBotIndexView.vue';
import UserView from '@/views/user/account/userView.vue';
import LoginView from '@/views/Login/loginView.vue';
import RegisterView from '@/views/Register/registerView.vue';
import RecordContentView from '@/views/record/RecordContentView.vue';
import store from '@/store';

const routes = [
    {
        path: '/',
        name: 'home',
        redirect: '/pkindex',
        meta: {
            requestAuth: false,
        },
    },
    {
        path: '/404',
        name: 'notfound',
        component: NotFoundIndexView,
        meta: {
            requestAuth: false,
        },
    },
    {
        path: '/pkindex',
        name: 'pkindex',
        component: PkIndexView,
        meta: {
            requestAuth: true,
        },
    },
    {
        path: '/ranklist',
        name: 'ranklist',
        component: RankListIndexView,
        meta: {
            requestAuth: true,
        },
    },
    {
        path: '/record',
        name: 'record',
        component: RecordIndexView,
        meta: {
            requestAuth: true,
        },
    },
    {
        path: '/record/:recordId',
        name: 'record_content',
        component: RecordContentView,
        meta: {
            requestAuth: false,
        },
    },
    {
        path: '/user',
        name: 'user',
        component: UserView,
        meta: {
            requestAuth: true,
        },
    },
    {
        path: '/user/bot',
        name: 'userbot',
        component: UserBotIndexView,
        meta: {
            requestAuth: true,
        },
    },
    {
        path: '/login',
        name: 'login',
        component: LoginView,
        meta: {
            requestAuth: false,
        },
    },
    {
        path: '/register',
        name: 'register',
        component: RegisterView,
        meta: {
            requestAuth: false,
        },
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    // 实现前端界面的授权
    if (!store.state.user.is_login && to.meta.requestAuth) {
        // 如果没有登录并且当前界面是授权界面重定向到登录界面
        next({ name: 'login' });
    } else {
        next();
    }
});

export default router;
