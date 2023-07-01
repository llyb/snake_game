import { createRouter, createWebHistory } from 'vue-router';
import NotFoundIndexView from '@/views/error/NotFoundIndexView.vue';
import PkIndexView from '@/views/pk/PkIndexView.vue';
import RankListIndexView from '@/views/ranklist/RankListIndexView.vue';
import RecordIndexView from '@/views/record/RecordIndexView.vue';
import UserBotIndexView from '@/views/user/bot/UserBotIndexView.vue';
import UserView from '@/views/user/account/userView.vue';
import LayoutView from '@/views/Layout/LayoutView.vue';
import LoginView from '@/views/Login/loginView.vue';
import RegisterView from '@/views/Register/registerView.vue';

const routes = [
    {
        path: '/',
        name: 'home',
        component: LayoutView,
        children: [
            {
                path: '',
                name: 'notfound',
                component: NotFoundIndexView,
            },
            {
                path: '/pkindex',
                name: 'pkindex',
                component: PkIndexView,
            },
            {
                path: '/ranklist',
                name: 'ranklist',
                component: RankListIndexView,
            },
            {
                path: '/record',
                name: 'record',
                component: RecordIndexView,
            },
            {
                path: '/user',
                name: 'user',
                component: UserView,
            },
            {
                path: '/user/bot',
                name: 'userbot',
                component: UserBotIndexView,
            },
        ],
    },
    {
        path: '/login',
        name: 'login',
        component: LoginView,
    },
    {
        path: '/register',
        name: 'register',
        component: RegisterView,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
