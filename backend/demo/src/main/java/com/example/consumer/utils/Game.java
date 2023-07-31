package com.example.consumer.utils;

import java.util.Random;

public class Game {
    private int [][] g; // 地图对象
    private int rows, cols; // 地图的长和宽
    private int inner_walls_count; // 墙的数量
    private static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    public Game(int rows, int cols, int inner_walls_count) {
        this.cols = cols;
        this.rows = rows;
        this.g = new int[rows][cols];
        this.inner_walls_count = inner_walls_count;
    }

    public int[][] getG() {
        return this.g;
    }

    private boolean check_connectivity(int sx, int sy, int ex, int ey) {
        if(sx == ex && sy == ey) return true;
        g[sx][sy] = 1; // 从起点开始走
        for(int i = 0; i < 4; i ++) {
            int a = sx + dx[i], b = sy + dy[i];
            if(a < 0 || a >= this.rows || b < 0 || b >= this.cols || this.g[a][b] == 1) continue;
            if(check_connectivity(a, b, ex, ey)) {
                g[sx][sy] = 0;
                return true;
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    private boolean draw() {
        for(int i = 0; i < this.rows; i ++) {
            for(int j = 0; j < this.cols; j ++) {
                g[i][j] = 0;
            }
        }

        // 给四周加上墙
        for(int i = 0; i < this.rows; i ++) {
            g[i][0] = g[i][this.cols - 1] = 1;
        }
        for(int i = 0; i < this.cols; i ++) {
            g[0][i] = g[this.rows - 1][i] = 1;
        }

        // 随机生成墙
        Random random = new Random();
        for(int i = 0; i < this.inner_walls_count / 2; i ++) {
            for(int j = 0; j < 1000; j ++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if(g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue;
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        // 检验地图的连通性
        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createGameMap() {
        for(int i = 0; i < 1000; i ++) {
            if(this.draw()) {
                break;
            }
        }
    }
}
