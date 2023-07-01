let gameobjects = []; // 存储所有的游戏对象

export default class GameObject {
    constructor() {
        gameobjects.push(this);
        this.has_called = false;
        this.time_delta = 0; // 得到每两帧之间的时间差
    }
    strat() { // 开始时执行一次
        
    }
    update() { // 每一帧执行一次

    }
    destroy() { // 销毁对象
        // filter函数是对数据中的每一个元素进行检查判断是否保留
        gameobjects = gameobjects.filter(function(obj) {
            return obj != this;
        });
    }
}

let last_timestamp; // 记录上一次的时间
let step = (timestamp) => {
    for(const obj in gameobjects) {
        if(!obj.has_called) {
            obj.has_called = true;
            obj.strat();
        } else {
            obj.time_delta = timestamp - last_timestamp;
            obj.update();
        }
    }
    last_timestamp = timestamp;
    requestAnimationFrame(step);
}

requestAnimationFrame(step);