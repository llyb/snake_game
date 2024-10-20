export default {
    state: {
        status: '',
        socket: null,
        opponent_username: '',
        opponent_photo: '',
        gamemap: null,
        // 存储两名玩家的位置信息
        a_id: 0,
        a_sx: 0,
        a_sy: 0,
        b_id: 0,
        b_sx: 0,
        b_sy: 0,
        gameObject: null, // 用来存储我们的游戏对象，进而对蛇的位置信息进行更新
        loser: 'none',
    },
    getters: {},
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateOpponent(state, opponent) {
            state.opponent_username = opponent.opponent_username;
            state.opponent_photo = opponent.opponent_photo;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateGame(state, game) {
            state.a_id = game.a_id;
            state.a_sx = game.a_sx;
            state.a_sy = game.a_sy;
            state.b_id = game.b_id;
            state.b_sx = game.b_sx;
            state.b_sy = game.b_sy;
            state.gamemap = game.gamemap;
        },
        updategameObject(state, gameObject) {
            state.gameObject = gameObject;
        },
        updateLoser(state, loser) {
            state.loser = loser;
        },
    },
    actions: {},
    modules: {},
};
