<template>
    <div class="game-body">
        <MenuView v-if="$store.state.router.router_name === 'menu'" />
        <PkIndexView v-else-if="$store.state.router.router_name === 'pk'"/>
        <RecordIndexView v-else-if="$store.state.router.router_name === 'record'"/>
        <RankListIndexView v-else-if="$store.state.router.router_name === 'ranklist'"/>
        <RecordContentView v-else-if="$store.state.router.router_name === 'record_content'"/>
        <UserBotIndexView v-else-if="$store.state.router.router_name === 'user_bot'"/>
    </div>
</template>

<script setup>
import { useStore } from 'vuex';
import MenuView from './views/MenuView.vue';
import PkIndexView from './views/pk/PkIndexView.vue';
import RecordIndexView from './views/record/RecordIndexView.vue';
import RecordContentView from './views/record/RecordContentView.vue';
import RankListIndexView from './views/ranklist/RankListIndexView.vue';
import UserBotIndexView from './views/user/bot/UserBotIndexView.vue';
import $ from 'jquery';

const store = useStore();

$.ajax({
    type: "get",
    url: "https://app6053.acapp.acwing.com.cn/api/user/account/acwing/acapp/apply_code/",
    success: function (resp) {
        if (resp.result === "success") {
            store.state.user.AcWingOS.api.oauth2.authorize(resp.appid, resp.redirect_uri, resp.scope, resp.state, resp => { // 这里的resp表示的是我们后端实现的receive_code函数返回的参数
                if (resp.result === "success") {
                    // 对state进行更新
                    // const jwt_token = resp.jwt_token;
                    store.commit('updateUserToken', resp.jwt_token);
                    store.dispatch('getinfo', {
                        // 从云端拉取用户信息
                        success() {
                            store.commit('update_pulling_info', false);
                        },
                        error() {
                            store.commit('update_pulling_info', false);
                        },
                    });
                } else {
                    store.state.user.AcWingOS.api.window.close(); // 关闭acapp窗口
                }
            });
        } else {
            store.state.user.AcWingOS.api.window.close();
        }
    }
});

// 判断是否存在jwt_token实现持久化登录
// const jwt_token = localStorage.getItem('jwt_token');
// const jwt_token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiNGEwY2IzODk1OWU0M2EwOGEzYmU4ZjdlMGE0YTQ0OCIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTcyOTM5MzE4MCwiZXhwIjoxNzMwNjAyNzgwfQ.UMDBUZYAs8b_CwdJRIdgGB5ffoqLQO_fdwe2vi3oVBk";
// if (jwt_token) {
//     // 对state进行更新
//     store.commit('updateUserToken', jwt_token);
//     store.dispatch('getinfo', {
//         // 从云端拉取用户信息
//         success() {
//             store.commit('update_pulling_info', false);
//         },
//         error() {
//             store.commit('update_pulling_info', false);
//         },
//     });
// } else {
//     store.commit('update_pulling_info', false);
// }
</script>

<style scoped>
body {
    margin: 0;
}
div.game-body {
    width: 100%;
    height: 100%;
    background-image: url('./assets/images/背景.png');
    background-size: cover;
}
div.window {
    width: 100vw;
    height: 100vh;
}
</style>
