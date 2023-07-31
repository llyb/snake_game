import GameObject from './GameObject';
import Wall from '@/assets/scripts/Wall';
import Snake from './Snake';

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
        this.ctx.canvas.focus(); // 使canvas聚焦

        const [snake1, snake2] = this.snakes;
        this.ctx.canvas.addEventListener('keydown', (e) => {
            console.log(e.key);
            if (e.key === 'w') snake1.set_direction(0);
            else if (e.key === 'd') snake1.set_direction(1);
            else if (e.key === 's') snake1.set_direction(2);
            else if (e.key === 'a') snake1.set_direction(3);
            else if (e.key === 'ArrowUp') snake2.set_direction(0);
            else if (e.key === 'ArrowRight') snake2.set_direction(1);
            else if (e.key === 'ArrowDown') snake2.set_direction(2);
            else if (e.key === 'ArrowLeft') snake2.set_direction(3);
        });
    }

    create_walls() {
        const g = this.store.state.pk.gamemap;
        console.log(g[0].length);
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
