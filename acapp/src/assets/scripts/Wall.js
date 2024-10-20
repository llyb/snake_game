import GameObject from './GameObject';

export default class Wall extends GameObject {
    constructor(r, c, gamemap) {
        super();

        // 墙的左上角的坐标
        this.r = r;
        this.c = c;
        this.gamemap = gamemap;
        this.color = '#B6782E'; // 墙的颜色
    }

    update() {
        this.render();
    }

    // 画墙
    render() {
        // 墙的大小和画布在传入的父元素中读取
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        // 画墙的主要逻辑
        ctx.fillStyle = this.color;
        // canvas中的坐标系通常位于左上角，x轴向右延伸，y轴向左延伸
        // 这和数组的x和y是相反的，所以这里要反着写
        ctx.fillRect(this.c * L, this.r * L, L, L);
    }
}
