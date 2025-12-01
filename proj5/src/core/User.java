package core;

import tileengine.TETile;
import tileengine.Tileset;

public class User {
    public int x;
    public int y;
    public int health;

    public User(int x, int y) {
        this.x = x;
        this.y = y;
        this.health = 1;
    }

    public void moveAroundMap(int xCoord, int yCoord, TETile[][] world) {
        world[x][y] = Tileset.FLOOR;
        x = xCoord;
        y = yCoord;
        world[x][y] = Tileset.AVATAR;
    }

}

