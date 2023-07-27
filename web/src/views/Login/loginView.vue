<template>
    <ContentField v-if="!$store.state.user.pulling_info">
        <div class="row justify-content-md-center">
            <div class="col-5">
                <ContentField>
                    <form @submit.prevent="login">
                        <div class="mb-3 leavetop">
                            <label for="username" class="form-label">用户名</label>
                            <input
                                v-model="username"
                                type="text"
                                class="form-control"
                                id="username"
                                aria-describedby="emailHelp"
                                placeholder="请输入用户名"
                            />
                        </div>
                        <div class="mb-3 leavetop">
                            <label for="password" class="form-label" placeholder="请输入密码">密码</label>
                            <input
                                v-model="password"
                                type="password"
                                class="form-control"
                                id="exampleInputPassword1"
                                placeholder="请输入密码"
                            />
                        </div>
                        <div class="error_message">{{ error_message }}</div>
                        <button type="submit" class="btn btn-primary leavetop">提交</button>
                    </form>
                </ContentField>
            </div>
        </div>
    </ContentField>
</template>

<script setup>
import ContentField from '@/components/ContentField.vue';
import { ref } from 'vue';
import router from '@/router';
import { useStore } from 'vuex';

const store = useStore();

let username = ref('');
let password = ref('');
let error_message = ref('');

// 判断是否存在jwt_token实现持久化登录
const jwt_token = localStorage.getItem('jwt_token');
if (jwt_token) {
    // 对state进行更新
    store.commit('updateUserToken', jwt_token);
    store.dispatch('getinfo', {
        // 从云端拉取用户信息
        success() {
            router.push({ name: 'home' });
            store.commit('update_pulling_info', false);
        },
        error() {
            store.commit('update_pulling_info', false);
        },
    });
} else {
    store.commit('update_pulling_info', false);
}

// 调用后端的登录函数
const login = () => {
    error_message.value = '';
    if (username.value === '' || password.value === '') {
        error_message.value = '请输入用户名或者密码';
    } else {
        // 进行登录
        store.dispatch('login', {
            username: username.value,
            password: password.value,
            success() {
                // 如果登录成功那么调用获取信息函数
                store.dispatch('getinfo', {
                    success() {
                        // 如果登录并成功获取用户信息那么进行跳转
                        router.push({ name: 'home' });
                        console.log(store.state.user);
                    },
                    error() {
                        error_message.value = '获取信息失败';
                    },
                });
            },
            error() {
                error_message.value = '用户名或密码错误!';
            },
        });
    }
};
</script>

<style>
.leavetop {
    margin-top: 10px;
}
button {
    width: 100%;
}
.error_message {
    color: red;
}
</style>
