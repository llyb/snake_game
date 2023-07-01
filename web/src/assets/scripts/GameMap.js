import GameObject from './GameObject';

export default class GameMap extends GameObject {
    constructor(ctx, parent) {
        super();
        this.ctx = ctx;
        this.parent = parent;
    }
}
