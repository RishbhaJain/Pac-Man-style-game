package demo;

import core.World;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

/**
 *  Draws a world that is mostly empty except for a small region.
 */
public class BoringWorldDemo1 {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    public static void main(String[] args) {
        World w = new World(148622763);
        w.createRooms();
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        System.out.println("#");

        for (World.Rectangle room : w.rooms) {
            System.out.println(room.x + " " + room.y + " " + room.width + " " + room.height);
            for (int x = room.x; x < (room.x + room.width); x++) {
                for (int y = room.y; y < (room.y + room.height); y++) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }

        // draws the world to the screen
        ter.renderFrame(world);
    }


}
