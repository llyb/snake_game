<template>
    <div ref="parent" class="gamemap">
        <!-- 使得canvas能够聚焦 -->
        <canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>

<script setup>
import GameMap from '@/assets/scripts/GameMap';
import { ref, onMounted } from 'vue';
import { useStore } from 'vuex';

const store = useStore();

// 使用两个标签绑定我们的地图和画布
let canvas = ref('');
let parent = ref('');

onMounted(() => {
    store.commit('updategameObject', new GameMap(canvas.value.getContext('2d'), parent.value, store));
});
</script>

<style scoped>
.gamemap {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
