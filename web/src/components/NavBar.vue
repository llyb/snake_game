<template>
    <nav class="navbar navbar-expand-lg bg-body-tertiary navbar-dark bg-dark">
        <div class="container">
            <router-link :to="{ name: 'home' }" class="navbar-brand">King Of Bots</router-link>
            <button
                class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarText"
                aria-controls="navbarText"
                aria-expanded="false"
                aria-label="Toggle navigation"
            >
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarText">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <router-link :to="{ name: 'pkindex' }" class="nav-link" aria-current="page">对战</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link :to="{ name: 'record' }" class="nav-link">对局列表</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link :to="{ name: 'ranklist' }" class="nav-link">排行榜</router-link>
                    </li>
                </ul>
                <ul class="navbar-nav mb-2 mb-lg-0" v-if="$store.state.user.is_login">
                    <li class="nav-item dropdown">
                        <router-link
                            :to="{ name: 'user' }"
                            class="nav-link dropdown-toggle"
                            role="button"
                            data-bs-toggle="dropdown"
                            aria-expanded="false"
                        >
                            {{ $store.state.user.username }}
                        </router-link>
                        <ul class="dropdown-menu">
                            <li><router-link :to="{ name: 'userbot' }" class="dropdown-item">我的Bots</router-link></li>
                            <li><a class="dropdown-item" @click="logout">退出</a></li>
                        </ul>
                    </li>
                </ul>
                <!-- 只有当从云端拉取完用户信息后才进行显示 -->
                <ul class="navbar-nav" v-else-if="!$store.state.user.pulling_info">
                    <li class="nav-item">
                        <router-link :to="{ name: 'register' }" class="nav-link" aria-current="page">注册</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link :to="{ name: 'login' }" class="nav-link" aria-current="page">登录</router-link>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>

<script setup>
import { useStore } from 'vuex';

const store = useStore();

const logout = () => {
    store.dispatch('logout');
};
</script>

<style></style>
