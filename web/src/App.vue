<template>
    <NavBar />
    <router-view />
</template>

<script setup>
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap';
import NavBar from './components/NavBar.vue';
</script>

<style>
body {
    background-image: url('./assets/images/背景.png');
    background-size: cover;
}
</style>
