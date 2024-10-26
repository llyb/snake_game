<template>
    <div></div>
</template>

<script setup>
import router from "@/router/index";
import { useRoute } from "vue-router";
import $ from 'jquery';
import { useStore } from "vuex";

const myRoute = useRoute();
const store = useStore();

$.ajax({
    url: "https://app6053.acapp.acwing.com.cn/api/user/account/acwing/web/receive_code/",
    type: "get",
    data: {
        code: myRoute.query.code, // 查询url中的参数
        state: myRoute.query.state
    },
    success: resp => {
        if (resp.result === "success") {
            localStorage.setItem('jwt_token', resp.jwt_token);
            store.commit("updateUserToken", resp.jwt_token);
            router.push({ name: "home" });
            store.commit('update_pulling_info', false);
        } else {
            router.push({ name: "login" });
        }
    }
});
</script>

<style>

</style>