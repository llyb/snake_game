export default class Cell {
    // 该类的作用是得到canvas中圆心的坐标
    constructor(r, c) {
        this.r = r;
        this.c = c;
        this.x = c + 0.5;
        this.y = r + 0.5;
    }
}
