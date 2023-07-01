<template>
    <div ref="parent" class="gamemap">
        <canvas ref="canvas"></canvas>
    </div>
</template>

<script setup>
import GameMap from '@/assets/scripts/GameMap';
import { ref, onMounted } from 'vue';
// 使用两个标签绑定我们的地图和画布
let canvas = ref('');
let parent = ref('');

onMounted(() => {
    new GameMap(canvas.value.getContext('2d'), parent.value);
});
</script>

<style scoped>
.gamemap {
    width: 60vw;
    height: 70vh;
    background-color: aqua;
    margin: 40px auto;
}
</style>
