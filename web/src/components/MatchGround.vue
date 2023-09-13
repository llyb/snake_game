<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="" />
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-4">
                <div class="user-bot">
                    <select v-model="selected_bot" class="form-select" aria-label="Default select example">
                        <option value="-1" selected>亲自出马</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.title }}</option>
                    </select>
                </div>
            </div>
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="" />
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>
            <div class="col-12 container" style="text-align: center; padding-top: 15vh; width: 20%">
                <button @click="click_match_btn" type="button" class="btn btn-danger btn-lg">{{ btn_info }}</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';

let bots = ref([]);
let selected_bot = ref('-1');
let btn_info = ref('开始匹配');
const store = useStore();

const click_match_btn = () => {
    if (btn_info.value === '开始匹配') {
        btn_info.value = '取消';
        store.state.pk.socket.send(
            JSON.stringify({
                event: 'start-matching',
                bot_id: selected_bot.value,
            })
        );
    } else {
        btn_info.value = '开始匹配';
        store.state.pk.socket.send(
            JSON.stringify({
                event: 'stop-matching',
            })
        );
    }
};

//从云端拉取Bot信息
const getBots = () => {
    $.ajax({
        url: 'http://localhost:3000/user/bot/getlist',
        type: 'get',
        headers: {
            Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
            bots.value = resp;
        },
        error(resp) {
            console.log(resp);
        },
    });
};
getBots();
</script>

<style>
div.matchground {
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
    background-color: rgba(50, 50, 50, 0.5);
}

div.user-photo > img {
    border-radius: 50%;
    width: 25vh;
}

div.user-photo {
    text-align: center;
    margin-top: 10vh;
}

div.user-username {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: white;
    padding-top: 2vh;
}
div.user-bot {
    padding-top: 22vh;
}
div.user-bot > select {
    width: 70%;
    margin: 0 auto;
}
</style>
