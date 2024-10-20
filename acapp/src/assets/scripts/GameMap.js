import GameObject from './GameObject';
import Wall from '@/assets/scripts/Wall';
import Snake from './Snake';
import { useStore } from 'vuex';

export default class GameMap extends GameObject {
    // 实现游戏的地图界面
    constructor(ctx, parent, store) {
        super();
        this.store = store;
        this.ctx = ctx;
        this.rows = 13;
        this.cols = 14;
        this.parent = parent;
        this.L = 0; // 初始每个方块的大小
        this.inner_walls_count = 20; // 障碍物的数量
        this.walls = []; // 存储墙的对象

        this.snakes = [
            new Snake({ id: 0, color: '#4876EC', r: this.rows - 2, c: 1 }, this),
            new Snake({ id: 1, color: '#F94848', r: 1, c: this.cols - 2 }, this),
        ];

        this.store = useStore();
    }

    check_valid(cell) {
        // 检测目标位置是否合法：没有撞到两条蛇的身体和障碍物
        for (const wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c) return false;
        }

        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) {
                // 当蛇尾会前进的时候，蛇尾不要判断
                k--;
            }
            for (let i = 0; i < k; i++) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) return false;
            }
        }

        return true;
    }

    next_step() {
        // 让两条蛇进入下一回合
        for (const snake of this.snakes) {
            snake.next_step();
        }
    }

    check_ready() {
        // 判断两条蛇是否都准备好下一回合了
        for (const snake of this.snakes) {
            if (snake.status !== 'idle') return false;
            if (snake.direction === -1) return false;
        }
        return true;
    }

    // 键盘监听事件
    addEventListener_keys() {
        if (this.store.state.record.is_record) {
            // 说明当前是录像
            const [snake0, snake1] = this.snakes;
            const a_steps = this.store.state.record.a_steps;
            const b_steps = this.store.state.record.b_steps;
            const loser = this.store.state.record.record_loser;
            console.log(loser);
            let k = 0; // 记录当前走到哪一步了
            // 每隔一段时间传入两条蛇的操作从而进行移动
            const interval_id = setInterval(() => {
                if (k >= a_steps.length - 1) {
                    console.log(loser);
                    // 说明走完了所有的操作
                    if (loser === 'all' || loser === 'A') {
                        snake0.status = 'die';
                    }
                    if (loser === 'all' || loser === 'B') {
                        snake1.status = 'die';
                    }
                    clearInterval(interval_id); // 任务结束，清除该定时器
                } else {
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }
                k++;
            }, 300);
            this.store.commit('set_record', false);
        } else {
            this.ctx.canvas.focus(); // 使canvas聚焦
            let d = -1;
            this.ctx.canvas.addEventListener('keydown', (e) => {
                if (e.key === 'w') d = 0;
                else if (e.key === 'd') d = 1;
                else if (e.key === 's') d = 2;
                else if (e.key === 'a') d = 3;

                // 将当前用户的移动情况传递给自己的socket
                if (d >= 0) {
                    this.store.state.pk.socket.send(
                        JSON.stringify({
                            event: 'move',
                            direction: d,
                        })
                    );
                }
            });
        }
    }

    create_walls() {
        const g = this.store.state.pk.gamemap;
        // 画障碍物
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }

        return true;
    }

    update_size() {
        // 初始化每个格子的大小和画布的宽高
        this.L = parseInt(Math.min(this.parent.clientHeight / this.rows, this.parent.clientWidth / this.cols));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    start() {
        for (let i = 0; i < 1000; i++) if (this.create_walls()) break;

        this.addEventListener_keys();
    }

    update() {
        this.update_size();
        if (this.check_ready()) {
            this.next_step();
        }

        this.render();
    }

    render() {
        // 绘制地图
        const color_even = '#AAD751',
            color_odd = '#A2D149';
        for (let i = 0; i < this.rows; i++) {
            for (let j = 0; j < this.cols; j++) {
                if ((i + j) % 2 == 0) {
                    // 偶数
                    this.ctx.fillStyle = color_even;
                } else {
                    // 奇数
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(i * this.L, j * this.L, this.L, this.L);
            }
        }
    }
}
