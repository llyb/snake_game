<template>
    <div class="content-field">
        <div class="playground">
            <GameMap />
        </div>
        <div class="go-back" @click="click_go_back_handler">
            返回
        </div>
    </div>
</template>

<script setup>
import GameMap from './GameMap.vue';
import { useStore } from 'vuex';
const store = useStore();

const click_go_back_handler = () => {
    store.commit("updateRouterName", "record");
}
</script>

<style scoped>
.content-field {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
}
.playground {
    width: 60vw;
    height: 70vh;
}
div.go-back {
    position: absolute;
    right: 5vh;
    bottom: 5vh;
    color: white;
    font-size: 24px;
    font-style: italic;
    font-weight: 600;
    cursor: pointer;
    user-select: none;
}
div.go-back:hover {
    scale: 1.2;
    transition: 200ms;
}
</style>
