<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-5">
                <ContentField>
                    <form @submit.prevent="register">
                        <div class="mb-3 leavetop">
                            <label for="username" class="form-label">用户名</label>
                            <input
                                type="text"
                                class="form-control"
                                id="username"
                                aria-describedby="emailHelp"
                                placeholder="请输入用户名"
                                v-model="username"
                            />
                        </div>
                        <div class="mb-3 leavetop">
                            <label for="password" class="form-label">密码</label>
                            <input
                                type="password"
                                class="form-control"
                                id="password"
                                placeholder="请输入密码"
                                v-model="password"
                            />
                        </div>
                        <div class="mb-3 leavetop">
                            <label for="confirmedpassword" class="form-label">确认密码</label>
                            <input
                                type="password"
                                class="form-control"
                                id="confirmedpassword"
                                placeholder="请确认密码"
                                v-model="confirmedPassoword"
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
import $ from 'jquery';
import { ref } from 'vue';
import router from '@/router';

let username = ref('');
let password = ref('');
let confirmedPassoword = ref('');
let error_message = ref('');

const register = () => {
    // 这里不会涉及到对state状态的修改，所以直接调用Api即可
    $.ajax({
        url: 'https://app3979.acapp.acwing.com.cn/api/user/account/register/',
        type: 'post',
        data: {
            username: username.value,
            password: password.value,
            confirmedPassoword: confirmedPassoword.value,
        },
        success(resp) {
            // 注册成功跳转到登录界面进行登录
            if (resp.error_message === 'success') {
                router.push({ name: 'login' });
            } else {
                error_message.value = resp.error_message;
            }
        },
        error() {
            error_message.value = '注册失败！';
        },
    });
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
