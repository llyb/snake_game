<template>
    <ContentField> 我的Bot </ContentField>
</template>

<script setup>
import ContentField from '@/components/ContentField.vue';
// import $ from 'jquery';
// import { useStore } from 'vuex';

// const store = useStore();
// 测试添加bot接口
// $.ajax({
//     url: 'http://127.0.0.1:3000/user/bot/add/',
//     type: 'post',
//     data: {
//         title: '这是标题',
//         description: '这是描述',
//         content: '这是代码',
//     },
//     headers: {
//         Authorization: 'Bearer ' + store.state.user.token,
//     },
//     success(resp) {
//         console.log(resp);
//     },
//     error(resp) {
//         console.log(resp);
//     },
// });

// 测试查询Bot功能
// $.ajax({
//     url: 'http://127.0.0.1:3000/user/bot/getlist',
//     type: 'get',
//     headers: {
//         Authorization: 'Bearer ' + store.state.user.token,
//     },
//     success(resp) {
//         console.log(resp);
//     },
//     error(resp) {
//         console.log(resp);
//     },
// });

// 测试删除Bot功能
// $.ajax({
//     url: 'http://127.0.0.1:3000/user/bot/delete',
//     type: 'post',
//     data: {
//         bot_id: 1,
//     },
//     headers: {
//         Authorization: 'Bearer ' + store.state.user.token,
//     },
//     success(resp) {
//         console.log(resp);
//     },
//     error(resp) {
//         console.log(resp);
//     },
// });

// 测试修改Bot功能
// $.ajax({
//     url: 'http://127.0.0.1:3000/user/bot/update',
//     type: 'post',
//     data: {
//         bot_id: 1,
//         title: '这是标题1',
//         description: '这是描述1',
//         content: '这是内容1',
//     },
//     headers: {
//         Authorization: 'Bearer ' + store.state.user.token,
//     },
//     success(resp) {
//         console.log(resp);
//     },
//     error(resp) {
//         console.log(resp);
//     },
// });
</script>

<style></style>
