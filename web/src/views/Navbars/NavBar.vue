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
                        <router-link :to="{ name: 'pkindex' }" class="nav-link active" aria-current="page"
                            >对战</router-link
                        >
                    </li>
                    <li class="nav-item">
                        <router-link :to="{ name: 'record' }" class="nav-link">对局列表</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link :to="{ name: 'ranklist' }" class="nav-link">排行榜</router-link>
                    </li>
                </ul>
                <ul class="navbar-nav mb-2 mb-lg-0">
                    <li class="nav-item dropdown">
                        <router-link
                            :to="{ name: 'user' }"
                            class="nav-link dropdown-toggle"
                            role="button"
                            data-bs-toggle="dropdown"
                            aria-expanded="false"
                        >
                            lyb
                        </router-link>
                        <ul class="dropdown-menu">
                            <li><router-link :to="{ name: 'userbot' }" class="dropdown-item">我的Bots</router-link></li>
                            <li><a class="dropdown-item">退出</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>

<script setup></script>

<style></style>
