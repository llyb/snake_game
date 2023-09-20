import $ from 'jquery';

export default {
    // 定义用户的状态,用户的密码不进行存储，忘记密码只能重新更改密码
    state: {
        id: '',
        username: '',
        photo: '',
        token: '',
        is_login: false,
        pulling_info: true, // 当从云端拉取完信息后才进行渲染
    },
    getters: {},
    mutations: {
        updateUser(state, user) {
            // 对用户的信息进行更新
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateUserToken(state, token) {
            state.token = token;
        },
        update_pulling_info(state, pulling_info) {
            state.pulling_info = pulling_info;
        },
        logout(state) {
            (state.id = ''), (state.username = '');
            state.photo = '';
            state.token = '';
            state.is_login = false;
        },
    },
    actions: {
        login(context, data) {
            // 用户登录的逻辑
            $.ajax({
                url: 'https://app3979.acapp.acwing.com.cn/api/user/account/token/',
                type: 'post',
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    // 执行登录成功的逻辑
                    if (resp.error_message === 'success') {
                        localStorage.setItem('jwt_token', resp.token); // 将token存储在浏览器中实现持久化
                        context.commit('updateUserToken', resp.token); // 当用户登录成功后更新token
                        data.success();
                    } else {
                        data.error();
                    }
                },
                error() {},
            });
        },

        getinfo(context, data) {
            // 测试信息接口
            $.ajax({
                url: 'https://app3979.acapp.acwing.com.cn/api/user/account/info/',
                type: 'get',
                headers: {
                    Authorization: 'Bearer ' + context.state.token,
                },
                success(resp) {
                    if (resp.error_message === 'success') {
                        context.commit('updateUser', {
                            // 如果成功得到用户信息则进行更新
                            ...resp,
                            is_login: true,
                        });
                        data.success(resp); // 交互函数
                    }
                },
                error(resp) {
                    data.error(resp);
                },
            });
        },
        logout(context) {
            localStorage.removeItem('jwt_token');
            context.commit('logout');
        },
    },
    modules: {},
};
