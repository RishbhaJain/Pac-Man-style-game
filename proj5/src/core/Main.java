package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

public class Main {
    public static void main(String[] args) {

        World w = new World(300);

        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(World.WIDTH, World.HEIGHT);

        TETile[][] world = w.generateWorld();

        ter.renderFrame(world);

    }
}
