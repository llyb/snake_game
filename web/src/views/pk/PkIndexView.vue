<template>
    <PlayGroundVue v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
</template>

<script setup>
import PlayGroundVue from '@/components/PlayGround.vue';
import MatchGround from '@/components/MatchGround.vue';
import { useStore } from 'vuex';
import { onMounted, onUnmounted } from 'vue';

const store = useStore();
const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}`;

let socket = null;

onMounted(() => {
    store.commit('updateOpponent', {
        opponent_username: '我的对手',
        opponent_photo: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
    });

    store.commit('updateStatus', 'matching');

    socket = new WebSocket(socketUrl); // 创建WebSocket对象建立连接

    socket.onopen = () => {
        console.log('connected!');
        store.commit('updateSocket', socket);
    };

    socket.onmessage = (msg) => {
        // 接收消息的方式
        const data = JSON.parse(msg.data);
        if (data.event === 'start-matching') {
            // 表示匹配成功
            store.commit('updateOpponent', {
                username: data.opponent_username,
                photo: data.opponent_photo,
            });
            setTimeout(() => {
                store.commit('updateStatus', 'playing');
            }, 2000);
            store.commit('updateGameMap', data.gamemap);
            console.log(store.state.pk.gamemap);
        }
    };

    socket.onclose = () => {
        console.log('disconnected!');
    };
});

onUnmounted(() => {
    socket.close();
    store.commit('updateStatus', 'matching');
});
</script>

<style></style>
