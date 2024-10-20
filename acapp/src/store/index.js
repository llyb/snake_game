import { createStore } from 'vuex';
import MoudleUser from './user.js';
import MoudelPk from './pk.js';
import MoudleRecord from './record.js';
import ModuleRouter from './router.js'

export default createStore({
    state: {},
    getters: {},
    mutations: {},
    actions: {},
    modules: {
        user: MoudleUser,
        pk: MoudelPk,
        record: MoudleRecord,
        router: ModuleRouter
    },
});
