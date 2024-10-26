<template>
    <ContentField>
        <div class="game-table">
            <div>
                <table style="text-align: center">
                <thead>
                    <tr>
                        <th>玩家姓名</th>
                        <th>天梯积分</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="user in users" :key="user.id">
                        <td class="game-table-username">
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
            <nav>
                <ul style="padding: 0;">
                    <li class="game-page-item" @click="click_page(-2)">
                        <a class="game-page-link" href="#">上一页</a>
                    </li>
                    <li
                        :class="'game-page-item ' + page.is_active"
                        v-for="page in pages"
                        :key="page.number"
                        @click="click_page(page.number)"
                    >
                        <a class="game-page-link" href="#">{{ page.number }}</a>
                    </li>
                    <li class="game-page-item" @click="click_page(-1)">
                        <a class="game-page-link" href="#">下一页</a>
                    </li>
                </ul>
            </nav>
            </div>
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
    });
};

pull_page(current_page);
</script>

<style scoped>
img {
    width: 5vh;
    border-radius: 50%;
}
div.game-table {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
}
div.game-table table {
    background-color: rgba(255, 255, 255, 0.5);
    border-radius: 5px;
}
.game-table-username {
    text-align: left;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 7.5vw;
}
td {
    width: 15vw;
}
th {
    text-align: center;
}
.game-page-item {
    background-color: white;
    display: inline-block;
    padding: 8px 12px;
    border: 1px solid #dee2e6;
    cursor: pointer;
    user-select: none;
}
.game-page-item:hover {
   background-color: #E9ECEF; 
}
.game-page-item.active {
    background-color: #0d6efd;
}
.game-page-item.active > a {
    color: white;
}
.game-page-link {
    color: #0d6efd;
    text-decoration: none;
}
nav {
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
