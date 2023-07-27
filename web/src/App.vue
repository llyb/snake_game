<template>
    <router-view />
</template>

<script setup>
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap';
// import $ from 'jquery';

// 测试注册接口
// $.ajax({
//     url: 'http://localhost:3000/user/account/register/',
//     type: 'post',
//     data: {
//         username: 'zs',
//         password: 'pzs',
//         confirmedPassoword: 'pzs',
//     },
//     success(resp) {
//         console.log(resp);
//     },
//     error(resp) {
//         console.log(resp);
//     },
// });
</script>

<style>
body {
    background-image: url('./assets/images/背景.png');
    background-size: cover;
}
</style>
