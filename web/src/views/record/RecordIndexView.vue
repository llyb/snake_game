<template>
    <ContentField>
        <div>
            <table class="table table-striped table-hover" style="text-align: center">
                <thead>
                    <tr>
                        <th>玩家A</th>
                        <th>玩家B</th>
                        <th>对局结果</th>
                        <th>对战时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="record in records" :key="record.record.id">
                        <td>
                            <img :src="record.a_photo" alt="" />
                            &nbsp;
                            <span>{{ record.a_username }}</span>
                        </td>
                        <td>
                            <img :src="record.b_photo" alt="" />
                            &nbsp;
                            <span>{{ record.b_username }}</span>
                        </td>
                        <td>{{ record.result }}</td>
                        <td>{{ record.record.createtime }}</td>
                        <td>
                            <button
                                @click="open_record_content(record.record.id)"
                                type="button"
                                class="btn btn-info btn-sm"
                            >
                                查看录像
                            </button>
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
import router from '@/router';

let records = ref([]); // 从云端拉取当前页的对局信息
let current_page = 1; // 当前展示的页码
let total_records = 0; // 对局总数
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

    const max_pages = parseInt(Math.ceil(total_records / 10));

    if (page >= 1 && page <= max_pages) {
        // 如果更新后的页码在有效范围内
        pull_page(page); // 从云端拉取合法页的信息
    }
};

const update_pages = () => {
    // 页码的显示
    let max_pages = parseInt(Math.ceil(total_records / 10));
    let new_pages = [];
    for (let i = current_page - 2; i <= current_page + 2; i++) {
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
        url: 'http://127.0.0.1:3000/record/getList/',
        data: {
            page,
        },
        type: 'get',
        headers: {
            Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
            records.value = resp.records;
            total_records = resp.records_count;
            update_pages();
        },
        error(resp) {
            console.log(resp);
        },
    });
};

pull_page(current_page);

const StringTo2D = (g) => {
    let g_2d = [];
    for (let i = 0, k = 0; i < 13; i++) {
        let line = [];
        for (let j = 0; j < 14; j++, k++) {
            if (g[k] === '1') line.push(1);
            else line.push(0);
        }
        g_2d.push(line);
    }
    return g_2d;
};

// 打开当前id的录像
const open_record_content = (recordId) => {
    for (const record of records.value) {
        // 在当前页的记录中寻找id匹配的
        if (record.record.id === recordId) {
            store.commit('update_IsRecord', true);
            store.commit('updateGame', {
                gamemap: StringTo2D(record.record.map),
                a_id: record.record.aid,
                a_sx: record.record.asx,
                a_sy: record.record.asy,
                b_id: record.record.bid,
                b_sx: record.record.bsx,
                b_sy: record.record.bsy,
            });
            store.commit('update_steps', {
                a_steps: record.record.asteps,
                b_steps: record.record.bsteps,
            });
            store.commit('update_record_loser', record.record.loser);
            // 跳转到录像详情页，其实也就是对战页面
            router.push({
                name: 'record_content',
                params: {
                    recordId: record.record.id,
                },
            });
            break;
        }
    }
};
</script>

<style scoped>
img {
    width: 5vh;
    border-radius: 50%;
}
</style>
