<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style="width: 100%" />
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 20px">
                    <div class="card-header">
                        <span style="font-size: 130%">我的Bot</span>
                        <button
                            type="button"
                            class="btn btn-primary float-end"
                            style="width: 15%; margin-right: 10px"
                            data-bs-toggle="modal"
                            data-bs-target="#bot-create"
                        >
                            创建Bot
                        </button>
                        <!-- Modal -->
                        <div class="modal fade" id="bot-create" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h1 class="modal-title fs-5" id="exampleModalLabel">创建Bot</h1>
                                        <button
                                            type="button"
                                            class="btn-close"
                                            data-bs-dismiss="modal"
                                            aria-label="Close"
                                        ></button>
                                    </div>
                                    <div class="modal-body">
                                        <form>
                                            <div class="mb-3">
                                                <label for="exampleInputEmail1" class="form-label">名称</label>
                                                <input
                                                    type="email"
                                                    class="form-control"
                                                    id="exampleInputEmail1"
                                                    aria-describedby="emailHelp"
                                                    placeholder="请输入Bot名称"
                                                    v-model="botadd.title"
                                                />
                                            </div>
                                            <div class="mb-3">
                                                <label for="exampleInputEmail1" class="form-label">简介</label>
                                                <textarea
                                                    class="form-control"
                                                    rows="4"
                                                    id="comment"
                                                    name="text"
                                                    placeholder="请输入Bot简介"
                                                    v-model="botadd.description"
                                                ></textarea>
                                            </div>
                                            <div class="mb-3">
                                                <label for="exampleInputEmail1" class="form-label">代码</label>
                                                <VAceEditor
                                                    v-model:value="botadd.content"
                                                    @init="editorInit"
                                                    lang="c_cpp"
                                                    theme="textmate"
                                                    style="height: 300px"
                                                    :options="{
                                                        enableBasicAutocompletion: true, //启用基本自动完成
                                                        enableSnippets: true, // 启用代码段
                                                        enableLiveAutocompletion: true, // 启用实时自动完成
                                                        fontSize: 20, //设置字号
                                                        tabSize: 2, // 标签大小
                                                        showPrintMargin: false, //去除编辑器里的竖线
                                                        highlightActiveLine: true,
                                                    }"
                                                />
                                            </div>
                                        </form>
                                    </div>
                                    <div class="modal-footer">
                                        <div class="error_message">{{ botadd.error_message }}</div>
                                        <button
                                            type="button"
                                            class="btn btn-primary float-end"
                                            style="width: 15%"
                                            @click="addbot"
                                        >
                                            创建
                                        </button>
                                        <button
                                            type="button"
                                            class="btn btn-secondary float-end"
                                            style="width: 15%"
                                            data-bs-dismiss="modal"
                                        >
                                            取消
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createtime }}</td>
                                    <td>
                                        <button
                                            type="button"
                                            class="btn btn-warning"
                                            style="width: 20%; margin-right: 10px"
                                            data-bs-toggle="modal"
                                            :data-bs-target="'#bot-update-' + bot.id"
                                        >
                                            修改
                                        </button>

                                        <!-- Modal -->
                                        <div class="modal fade" :id="'bot-update-' + bot.id" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h1 class="modal-title fs-5" id="exampleModalLabel">修改Bot</h1>
                                                        <button
                                                            type="button"
                                                            class="btn-close"
                                                            data-bs-dismiss="modal"
                                                            aria-label="Close"
                                                        ></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <form>
                                                            <div class="mb-3">
                                                                <label for="exampleInputEmail1" class="form-label"
                                                                    >名称</label
                                                                >
                                                                <input
                                                                    type="email"
                                                                    class="form-control"
                                                                    id="exampleInputEmail1"
                                                                    aria-describedby="emailHelp"
                                                                    placeholder="请输入Bot名称"
                                                                    v-model="botupdate.title"
                                                                />
                                                            </div>
                                                            <div class="mb-3">
                                                                <label for="exampleInputEmail1" class="form-label"
                                                                    >简介</label
                                                                >
                                                                <textarea
                                                                    class="form-control"
                                                                    rows="4"
                                                                    id="comment"
                                                                    name="text"
                                                                    placeholder="请输入Bot简介"
                                                                    v-model="botupdate.description"
                                                                ></textarea>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label for="exampleInputEmail1" class="form-label"
                                                                    >代码</label
                                                                >
                                                                <VAceEditor
                                                                    v-model:value="botupdate.content"
                                                                    @init="editorInit"
                                                                    lang="c_cpp"
                                                                    theme="textmate"
                                                                    style="height: 300px"
                                                                    :options="{
                                                                        enableBasicAutocompletion: true, //启用基本自动完成
                                                                        enableSnippets: true, // 启用代码段
                                                                        enableLiveAutocompletion: true, // 启用实时自动完成
                                                                        fontSize: 20, //设置字号
                                                                        tabSize: 2, // 标签大小
                                                                        showPrintMargin: false, //去除编辑器里的竖线
                                                                        highlightActiveLine: true,
                                                                    }"
                                                                />
                                                            </div>
                                                        </form>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <div class="error_message">{{ botupdate.error_message }}</div>
                                                        <button
                                                            type="button"
                                                            class="btn btn-primary float-end"
                                                            style="width: 15%"
                                                            @click="updatebot(bot)"
                                                        >
                                                            保存修改
                                                        </button>
                                                        <button
                                                            type="button"
                                                            class="btn btn-secondary float-end"
                                                            style="width: 15%"
                                                            data-bs-dismiss="modal"
                                                        >
                                                            取消
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <button
                                            type="button"
                                            class="btn btn-danger"
                                            style="width: 20%; margin-right: 10px"
                                            @click="botdelete(bot.id)"
                                        >
                                            删除
                                        </button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { useStore } from 'vuex';
import { reactive, ref } from 'vue';
import $ from 'jquery';
import { Modal } from 'bootstrap/dist/js/bootstrap';
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';

ace.config.set(
    'basePath',
    'https://cdn.jsdelivr.net/npm/ace-builds@' + require('ace-builds').version + '/src-noconflict/'
);

const store = useStore();
let bots = ref([]); // 存储所有的bots
let botadd = reactive({
    title: '',
    description: '',
    content: '',
    error_message: '',
});
let botupdate = reactive({
    title: '',
    description: '',
    content: '',
    error_message: '',
});

//从云端拉取Bot信息
const getBots = () => {
    $.ajax({
        url: 'http://localhost:3000/user/bot/getlist',
        type: 'get',
        headers: {
            Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
            bots.value = resp;
        },
        error(resp) {
            console.log(resp);
        },
    });
};

getBots();

// 添加bot
const addbot = () => {
    $.ajax({
        url: 'http://localhost:3000/user/bot/add/',
        type: 'post',
        headers: {
            Authorization: 'Bearer ' + store.state.user.token,
        },
        data: {
            title: botadd.title,
            description: botadd.description,
            content: botadd.content,
        },
        success(resp) {
            // 当我们成功创建bot后关闭模态框
            if (resp.error_message === 'success') {
                Modal.getInstance('#bot-create').hide();
                getBots();
            } else {
                botadd.error_message = resp.error_message;
            }
        },
        error(resp) {
            console.log(resp);
        },
    });
};

// 删除bot
const botdelete = (botId) => {
    $.ajax({
        url: 'http://localhost:3000/user/bot/delete',
        type: 'post',
        headers: {
            Authorization: 'Bearer ' + store.state.user.token,
        },
        data: {
            bot_id: botId,
        },
        success() {
            getBots();
        },
        error(resp) {
            console.log(resp);
        },
    });
};

// 更新bot
const updatebot = (bot) => {
    console.log(botupdate.title + '\n' + botupdate.content + '\n' + botupdate.description + '\n' + bot.id);
    $.ajax({
        url: 'http://localhost:3000/user/bot/update',
        type: 'post',
        headers: {
            Authorization: 'Bearer ' + store.state.user.token,
        },
        data: {
            bot_id: bot.id,
            title: botupdate.title,
            description: botupdate.description,
            content: botupdate.content,
        },
        success(resp) {
            if (resp.error_message === 'success') {
                // 如果能够成功关闭模态框
                Modal.getInstance('#bot-update-' + bot.id).hide();
                getBots(); // 重新获取数据
            } else {
                botupdate.error_message = resp.error_message;
            }
        },
        error(resp) {
            console.log(resp);
        },
    });
};
</script>

<style>
div.error_message {
    color: red;
}
</style>
