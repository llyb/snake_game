<template>
    <ContentField>
        <div>
            <table class="table table-striped table-hover" style="text-align: center">
                <thead>
                    <tr>
                        <th>玩家姓名</th>
                        <th>天梯积分</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="user in users" :key="user.id">
                        <td>
                            <img :src="user.photo" alt="" />
                            &nbsp;
                            <span>{{ user.username }}</span>
                        </td>
                        <td>
                            {{ user.rating }}
                        </td>
                    </tr>
                </tbody>
            </table>
            <nav aria-label="Page navigation example" style="float: right">
                <ul class="pagination">
                    <li class="page-item" @click="click_page(-2)">
                        <a class="page-link" href="#">上一页</a>
                    </li>
                    <li
                        :class="'page-item ' + page.is_active"
                        v-for="page in pages"
                        :key="page.number"
                        @click="click_page(page.number)"
                    >
                        <a class="page-link" href="#">{{ page.number }}</a>
                    </li>
                    <li class="page-item" @click="click_page(-1)">
                        <a class="page-link" href="#">下一页</a>
                    </li>
                </ul>
            </nav>
        </div>
    </ContentField>
</template>

<script setup>
import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';
import { ref } from 'vue';
import { useStore } from 'vuex';

let users = ref([]); // 从云端拉取当前页的对局信息
let current_page = 1; // 当前展示的页码
let total_users = 0; // 对局总数
let pages = ref([]); // 存储页码和当前是哪一页
const store = useStore();

const click_page = (page) => {
    if (page === -1) {
        // 下一页
        page = current_page + 1;
    } else if (page === -2) {
        // 上一页
        page = current_page - 1;
    }

    const max_pages = parseInt(Math.ceil(total_users / 10));

    if (page >= 1 && page <= max_pages) {
        // 如果更新后的页码在有效范围内
        pull_page(page); // 从云端拉取合法页的信息
    }
};

const update_pages = () => {
    // 页码的显示
    let max_pages = parseInt(Math.ceil(total_users / 10));
    let new_pages = [];
    for (let i = current_page - 1; i <= current_page + 1; i++) {
        if (i >= 1 && i <= max_pages) {
            new_pages.push({
                number: i,
                is_active: i === current_page ? 'active' : '',
            });
        }
    }
    pages.value = new_pages;
};

const pull_page = (page) => {
    // 从云端拉取当前页的对局记录
    current_page = page;
    $.ajax({
        url: 'https://app6053.acapp.acwing.com.cn/api/get/ranklist/',
        data: {
            page,
        },
        type: 'post',
        headers: {
            Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
            users.value = resp.users;
            total_users = resp.users_count;
            update_pages();
        },
        error(resp) {
            console.log(resp);
        },
    });
};

pull_page(current_page);
</script>

<style scoped>
img {
    width: 5vh;
    border-radius: 50%;
}
</style>
