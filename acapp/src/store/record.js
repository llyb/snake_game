export default {
    state: {
        is_record: false,
        a_steps: '',
        b_steps: '',
        record_loser: '',
    },
    getters: {},
    mutations: {
        update_IsRecord(state, is_record) {
            state.is_record = is_record;
        },
        update_steps(state, data) {
            state.a_steps = data.a_steps;
            state.b_steps = data.b_steps;
        },
        update_record_loser(state, loser) {
            state.record_loser = loser;
        },
    },
    actions: {},
    modules: {},
};
