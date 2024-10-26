<template>
<ContentField>
        <div class="game-table">
            <div>
                <span style="font-size: 130%">我的Bot</span>
                <button type="button" style="float: right" @click="show_add_modal_handler(true)">
                    创建Bot
                </button>

                <!-- Modal -->
                <div class="game-modal" id="add-bot-btn" tabindex="-1" v-if="show_add_modal">
                    <div>
                        <h5 style="margin: 2px;">创建Bot</h5>
                    </div>
                    <div>
                        <div>
                            <label for="add-bot-title">名称</label>
                            <input style="width: 85%" v-model="botadd.title" type="text" id="add-bot-title" placeholder="请输入Bot名称">
                        </div>
                        <div>
                            <label for="add-bot-description">简介</label>
                            <textarea style="width: 85%; margin-top: 10px" v-model="botadd.description" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                        </div>
                        <div>
                            <label for="add-bot-code">代码</label>
                            <VAceEditor
                                v-model:value="botadd.content"
                                @init="editorInit"
                                lang="c_cpp"
                                theme="textmate"
                                style="height: 300px" />
                        </div>
                    </div>
                    <div>
                        <div class="error-message">{{ botadd.error_message }}</div>
                        <button type="button" @click="add_bot">创建</button>
                        <button type="button" @click="show_add_modal_handler(false)">取消</button>
                    </div>
                </div>
                <table>
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
                                <button type="button" style="margin-right: 10px;" @click="show_update_modal_handler(bot.id, true)">修改</button>
                                <button type="button" @click="remove_bot(bot.id)">删除</button>

                                <div class="game-modal" :id="'update-bot-modal-' + bot.id" tabindex="-1" v-if="bot.show_update_modal">
                                    <div>
                                        <h5 style="margin: 2px;">修改Bot</h5>
                                    </div>
                                    <div>
                                        <div>
                                            <label for="add-bot-title">名称</label>
                                            <input style="width: 85%" v-model="botupdate.title" type="text" id="add-bot-title" placeholder="请输入Bot名称">
                                        </div>
                                        <div>
                                            <label for="add-bot-description" >简介</label>
                                            <textarea style="width: 85%; margin-top: 10px" v-model="botupdate.description" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                        </div>
                                        <div>
                                            <label for="add-bot-code" class="form-label">代码</label>
                                            <VAceEditor
                                                v-model:value="botupdate.content"
                                                @init="editorInit"
                                                lang="c_cpp"
                                                theme="textmate"
                                                style="height: 300px" />
                                        </div>
                                    </div>
                                    <div>
                                        <div class="error-message">{{ botupdate.error_message }}</div>
                                        <button type="button" @click="update_bot(bot)">保存修改</button>
                                        <button type="button" @click="show_update_modal_handler(bot.id, false)">取消</button>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </ContentField>
</template>
<script setup>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
import { reactive, ref } from "vue";
import $ from "jquery";
import { VAceEditor } from "vue3-ace-editor";
import ace from "ace-builds";
import "ace-builds/src-noconflict/mode-json";
import "ace-builds/src-noconflict/theme-chrome";
import "ace-builds/src-noconflict/ext-language_tools";

ace.config.set(
  "basePath",
  "https://cdn.jsdelivr.net/npm/ace-builds@" +
    require("ace-builds").version +
    "/src-noconflict/"
);

let show_add_modal = ref(false);
const store = useStore();
let bots = ref([]); // 存储所有的bots
let botadd = reactive({
  title: "",
  description: "",
  content: "",
  error_message: "",
});
let botupdate = reactive({
  title: "",
  description: "",
  content: "",
  error_message: "",
});

//从云端拉取Bot信息
const getBots = () => {
  $.ajax({
    url: "https://app6053.acapp.acwing.com.cn/api/user/bot/getlist/",
    type: "get",
    headers: {
      Authorization: "Bearer " + store.state.user.token,
    },
    success(resp) {
      bots.value = resp;
      for (const bot of resp) {
        bot.show_update_model = false;
      }
    },
  });
};

getBots();

// 添加bot
const add_bot = () => {
  $.ajax({
    url: "https://app6053.acapp.acwing.com.cn/api/user/bot/add/",
    type: "post",
    headers: {
      Authorization: "Bearer " + store.state.user.token,
    },
    data: {
      title: botadd.title,
      description: botadd.description,
      content: botadd.content,
    },
    success(resp) {
      // 当我们成功创建bot后关闭模态框
      if (resp.error_message === "success") {
        botadd.title = "";
        botadd.description = "";
        botadd.content = "";
        botadd.error_message = "";
        show_add_modal.value = false;
        getBots();
      } else {
        botadd.error_message = resp.error_message;
      }
    },
  });
};

// 删除bot
const remove_bot = (botId) => {
  $.ajax({
    url: "https://app6053.acapp.acwing.com.cn/api/user/bot/delete/",
    type: "post",
    headers: {
      Authorization: "Bearer " + store.state.user.token,
    },
    data: {
      bot_id: botId,
    },
    success() {
      getBots();
    },
  });
};

// 更新bot
const update_bot = (bot) => {
  $.ajax({
    url: "https://app6053.acapp.acwing.com.cn/api/user/bot/update/",
    type: "post",
    headers: {
      Authorization: "Bearer " + store.state.user.token,
    },
    data: {
      bot_id: bot.id,
      title: botupdate.title,
      description: botupdate.description,
      content: botupdate.content,
    },
    success(resp) {
        console.log(resp.error_message);
        
      if (resp.error_message === "success") {
        getBots(); // 重新获取数据
      } else {
        botupdate.error_message = resp.error_message;
      }
    }
  });
};
const show_add_modal_handler = is_show => {
    show_add_modal.value = is_show;
}
const show_update_modal_handler = (bot_id, is_show) => {
    const new_bots = [];
    for (const bot of bots.value) {
        if (bot_id === bot.id) {
            bot.show_update_modal = is_show;
        }
        new_bots.push(bot);
    }
    bots.value = new_bots;
}
</script>

<style>
div.error_message {
  color: red;
}
div.game-table {
  display: flex;
  justify-content: center;
  padding: 5vh;
  width: 100%;
  height: 100%;
}
div.game-table table {
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 5px;
  width: 35vw;
}
td {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 10vw;
  max-width: 10vw;
  text-align: center;
}
th {
  text-align: center;
}
.game-modal {
    background-color: white;
    padding: 10px;
    border-radius: 10px;
    position: absolute;
    width: 40vw;
    height: 50vh;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    margin: auto;
    text-align: left;
}
</style>
