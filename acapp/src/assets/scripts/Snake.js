import Cell from './Cell';
import GameObject from './GameObject';

export default class Snake extends GameObject {
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;

        this.gamemap = gamemap;

        this.cells = [new Cell(info.r, info.c)]; // 用于存储蛇的身体,先将蛇头放进去
        this.next_cell = null; // 下一步的目标

        this.speed = 5; // 蛇每秒移动5个格子
        this.direction = -1; // -1为没有指令，0123分别表示上右下左
        this.status = 'idle'; // 当前的状态为静止

        this.eye_direction = 0;
        if (this.id === 1) this.eye_direction = 2; // 左下角的蛇初始朝上，右上角的蛇朝下

        // 定义偏移数组
        this.dr = [-1, 0, 1, 0];
        this.dc = [0, 1, 0, -1];

        this.step = 0; // 回合数
        this.eps = 1e-2; // 如果小于这个数则认为已经走到终点了

        this.eye_dx = [
            // 蛇眼睛不同方向的x的偏移量
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1],
        ];
        this.eye_dy = [
            // 蛇眼睛不同方向的y的偏移量
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1],
        ];
    }

    start() {}

    set_direction(d) {
        this.direction = d;
    }

    check_tail_increasing() {
        if (this.step <= 10) return true;
        if (this.step % 3 === 1) return true;
        return false;
    }

    next_step() {
        // 更新蛇的状态
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.eye_direction = d;
        this.direction = -1; // 清空操作
        this.status = 'move';
        this.step++;

        const k = this.cells.length;
        for (let i = k; i > 0; i--) {
            // 将数组向后移，方便我们对蛇头进行移动
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }
    }

    update_move() {
        // 求出当前位置和目标位置的差距，进而得到每一帧的差距
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < this.eps) {
            // 这种情况我们认为走到了终点
            this.cells[0] = this.next_cell;
            this.next_cell = null; // 下一个蛇头置空
            this.status = 'idle'; // 将当前的状态置为静止

            if (!this.check_tail_increasing()) {
                this.cells.pop(); // 删除蛇尾，因为他已经走到前面去了
            }
        } else {
            const move_distance = (this.speed * this.timedelta) / 1000; // 每两帧之间走的距离
            // 更新数组数组中的蛇头的位置
            this.cells[0].x += (move_distance * dx) / distance;
            this.cells[0].y += (move_distance * dy) / distance;

            if (!this.check_tail_increasing()) {
                // 如果本回合不变长,蛇尾必须向前移动
                const k = this.cells.length;
                const tail = this.cells[k - 1],
                    tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += (move_distance * tail_dx) / distance;
                tail.y += (move_distance * tail_dy) / distance;
            }
        }
    }

    render() {
        // 将蛇的身体画出来
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        if (this.status === 'die') {
            ctx.fillStyle = 'white';
        }

        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, (L / 2) * 0.8, 0, Math.PI * 2);
            ctx.fill();
        }

        for (let i = 1; i < this.cells.length; i++) {
            const a = this.cells[i - 1],
                b = this.cells[i];
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) continue;
            if (Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }

        ctx.fillStyle = 'black';
        for (let i = 0; i < 2; i++) {
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;

            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }
    }

    update() {
        // 先进行逻辑上的移动
        if (this.status === 'move') {
            this.update_move();
        }
        // 将逻辑上的移动画出来
        this.render();
    }
}
