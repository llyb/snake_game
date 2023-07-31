import { createStore } from 'vuex';
import MoudleUser from './user.js';
import MoudelPk from './pk.js';

export default createStore({
    state: {},
    getters: {},
    mutations: {},
    actions: {},
    modules: {
        user: MoudleUser,
        pk: MoudelPk,
    },
});
