<template>
    <ContentField>
        <PlayGroundVue v-if="$store.state.pk.status === 'playing'" />
        <MatchGround v-if="$store.state.pk.status === 'matching'" />
        <ResultBoard v-if="$store.state.pk.loser !== 'none'" />
        <div
            v-if="parseInt($store.state.user.id) === parseInt($store.state.pk.a_id) && $store.state.pk.status === 'playing'"
            class="user-location"
        >
            您出生在左下角
        </div>
        <div
            v-if="parseInt($store.state.user.id) === parseInt($store.state.pk.b_id) && $store.state.pk.status === 'playing'"
            class="user-location"
        >
            您出生在右上角
        </div>
    </ContentField>
</template>

<script setup>
import ContentField from '@/components/ContentField.vue';
import PlayGroundVue from '@/components/PlayGround.vue';
import MatchGround from '@/components/MatchGround.vue';
import ResultBoard from '@/components/ResultBoard.vue';
import { useStore } from 'vuex';
import { onMounted, onUnmounted } from 'vue';

const store = useStore();
const socketUrl = `wss://app6053.acapp.acwing.com.cn/websocket/${store.state.user.token}`;

store.commit('updateLoser', 'none'); // 每次进入页面前都要清空之前的对局状态

let socket = null;

onMounted(() => {
    store.commit('updateOpponent', {
        opponent_username: '我的对手',
        opponent_photo: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
    });

    store.commit('updateStatus', 'matching');

    socket = new WebSocket(socketUrl); // 创建WebSocket对象建立连接

    socket.onopen = () => {
        store.commit('updateSocket', socket);
    };

    socket.onmessage = (msg) => {
        // 接收消息的方式
        const data = JSON.parse(msg.data);
        if (data.event === 'start-matching') {
            // 表示匹配成功
            store.commit('updateOpponent', {
                opponent_username: data.opponent_username,
                opponent_photo: data.opponent_photo,
            });
            setTimeout(() => {
                // 等待0.2s后进入游戏
                store.commit('updateStatus', 'playing');
            }, 200);
            store.commit('updateGame', data.game);
        } else if (data.event === 'move') {
            // 游戏还在继续，设置前端两条蛇的移动
            const game = store.state.pk.gameObject;
            const [snake0, snake1] = game.snakes;
            // 先传左下，后传右上，这里和后端是对应的
            snake0.set_direction(data.a_direction);
            snake1.set_direction(data.b_direction);
        } else if (data.event === 'result') {
            // 游戏结束
            const game = store.state.pk.gameObject;
            const [snake0, snake1] = game.snakes;
            console.log(game.snakes);
            if (data.loser === 'all' || data.loser === 'A') {
                snake0.status = 'die';
            }

            if (data.loser === 'all' || data.loser === 'B') {
                snake1.status = 'die';
            }

            store.commit('updateLoser', data.loser); // 更新对局情况
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

<style>
div.user-location {
    text-align: center;
    color: white;
    font-size: larger;
    font-weight: 600;
}
</style>
