package core;

import tileengine.TETile;
import tileengine.Tileset;

public class Enemy {
    public int x;
    public int y;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveAroundMap(TETile[][] world, World w) {
        world[x][y] = Tileset.FLOOR;
        int xr;
        int yr;
        do {
            xr = -1 + w.r.nextInt(3);
            yr = -1 + w.r.nextInt(3);
            xr = x + xr;
            yr = y + yr;
        } while (world[xr][yr] != Tileset.FLOOR && world[xr][yr] != Tileset.AVATAR);
        x = xr;
        y = yr;
        world[x][y] = Tileset.ENEMY;
    }
}