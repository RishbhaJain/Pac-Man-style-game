package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    //Constants
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private static final int MAXCOUNT = 30;
    private static final int MINCOUNT = 10;
    private static final int MINSIZE = 4;
    private static final int MAXSIZE = 20;




    //Default seed
    private static final long SEED = 2873123;

    //Global variables
    public int countRooms;
    public int[][] worldArr;
    public long seed;
    public Random r;
    public int countSpaces;
    public List<Rectangle> rooms;
    public List<Rectangle> spaces;

    public World() {
        seed = SEED;
        r = new Random(seed);
        countRooms = (int) (MINCOUNT + (MAXCOUNT-MINCOUNT) * r.nextDouble());
        worldArr = new int[WIDTH][HEIGHT];
        rooms = new ArrayList<>();
        spaces = new ArrayList<>();
    }

    public World(long seed) {
        this.seed = seed;
        r = new Random(seed);
        countRooms = (int) (MINCOUNT + (MAXCOUNT-MINCOUNT) * r.nextDouble());
        worldArr = new int[WIDTH][HEIGHT];
        rooms = new ArrayList<>();
        spaces = new ArrayList<>();
    }

    public class Rectangle {
        public int x;
        public int y;
        public int width;
        public int height;
        Rectangle (int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this. width = width;
            this.height = height;
        }
    }

    public void createRooms() {
        int n = 0;
        while (n < countRooms) {
            int x = r.nextInt(WIDTH - MAXSIZE);
            int y = r.nextInt(HEIGHT - MAXSIZE);
            int width = (int) (r.nextDouble()*(MAXSIZE - MINSIZE) + MINSIZE);
            int height = (int) (r.nextDouble()*(MAXSIZE - MINSIZE) + MINSIZE);
            boolean dist = true;
            for (Rectangle room : rooms) {
                double centerDist = (float) Math.sqrt(Math.pow(room.x - x,2) + Math.pow(room.y - y,2));
                double minCenterDist = Math.sqrt(Math.pow((double)(room.width/2+width/2+1),2)
                        + Math.pow((double)(room.height/2+height/2+1),2));
                if (centerDist <= minCenterDist){
                    dist = false;
                    break;
                }
            }
            if(dist) {
                rooms.add(new Rectangle(x,y,width,height));
                n++;
            }
        }
    }
    public void fillWithRandomTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = randomTile();
            }
        }
    }

    /**
     * Picks a RANDOM tile with a 33% change of being
     * a wall, 33% chance of being a flower, and 33%
     * chance of being empty space.
     */
    private TETile randomTile() {
        // The following call to nextInt() uses a bound of 3 (this is not a seed!) so
        // the result is bounded between 0, inclusive, and 3, exclusive. (0, 1, or 2)
        int tileNum = r.nextInt(3);
        return switch (tileNum) {
            case 0 -> Tileset.WALL;
            case 1 -> Tileset.FLOWER;
            default -> Tileset.NOTHING;
        };
    }
}
