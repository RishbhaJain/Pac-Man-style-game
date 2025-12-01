package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    //Constants
    public static final int WIDTH = 100;
    public static final int HEIGHT = 60;
    private static final int MAXCOUNT = 30;
    private static final int MINCOUNT = 10;
    private static final int MINSIZE = 4;
    private static final int MAXSIZE = 20;

    //Default seed
    private static final long SEED = 2873123;

    //Global variables
    public int countRooms;
    public long seed;
    public Random r;
    public List<Room> rooms;

    public World() {
        seed = SEED;
        r = new Random(seed);
        countRooms = (int) (MINCOUNT + (MAXCOUNT - MINCOUNT) * r.nextDouble());
        rooms = new ArrayList<>();
    }

    public World(long seed) {
        this.seed = seed;
        r = new Random(seed);
        countRooms = (int) (MINCOUNT + (MAXCOUNT - MINCOUNT) * r.nextDouble());
        rooms = new ArrayList<>();
    }

    public Room getRandomRoom() {
        return rooms.get(1 + r.nextInt(countRooms-1));
    }

    public void createRooms() {
        int n = 0;
        while (n < countRooms) {
            int x = r.nextInt(WIDTH - MAXSIZE);
            int y = r.nextInt(HEIGHT - MAXSIZE);
            int width = (int) (r.nextDouble() * (MAXSIZE - MINSIZE) + MINSIZE);
            int height = (int) (r.nextDouble() * (MAXSIZE - MINSIZE) + MINSIZE);
            boolean dist = true;
            for (Room room : rooms) {
                double centerDist = Math.sqrt(Math.pow(room.x - x, 2) + Math.pow(room.y - y, 2));
                double minCenterDist = Math.sqrt(Math.pow((double) (room.width / 2 + width / 2 + 1), 2)
                        + Math.pow((double) (room.height / 2 + height / 2 + 1), 2));
                if (centerDist <= minCenterDist) {
                    dist = false;
                    break;
                }
            }
            if (dist) {
                rooms.add(new Room(x, y, width, height));
                n++;
            }
        }
    }

    public TETile[][] fillRoomsWithTiles(TETile[][] tiles) {
        for (Room room : rooms) {
            for (int i = room.x; i < room.x + room.width; i++) {
                for (int j = room.y; j < room.y + room.height; j++) {
                    if (i == room.x || i == (room.x + room.width - 1) || j == room.y || j == (room.y + room.height - 1)) {
                        tiles[i][j] = Tileset.WALL;
                    } else {
                        tiles[i][j] = Tileset.FLOOR;
                    }
                }
            }
        }
        return tiles;
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

    public TETile[][] generateWorld() {

        createRooms();

        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }

        // Draws rooms inside empty grid
        fillRoomsWithTiles(tiles);

        HallwayGenerator hallwayGen = new HallwayGenerator();
        List<Edge> edges = hallwayGen.generateEdges(rooms);
        List<Edge> mst = hallwayGen.findMST(edges, rooms.size());

        for (Edge edge : mst) {
            connectHallway(tiles, rooms.get(edge.roomPrev), rooms.get(edge.roomNext));
        }

        return tiles;
    }

    private void connectHallway(TETile[][] tiles, Room roomPrev, Room roomNext) {

        // Door tiles at room centers
        tiles[roomPrev.centerRoomX()][roomPrev.centerRoomY()] = Tileset.FLOOR;
        tiles[roomNext.centerRoomX()][roomNext.centerRoomY()] = Tileset.FLOOR;

        // Computes the xy position limits for room centers
        int x1 = Math.min(roomPrev.centerRoomX(), roomNext.centerRoomX());
        int x2 = Math.max(roomPrev.centerRoomX(), roomNext.centerRoomX());
        int y1 = Math.min(roomPrev.centerRoomY(), roomNext.centerRoomY());
        int y2 = Math.max(roomPrev.centerRoomY(), roomNext.centerRoomY());

        // Randomly go either frist in x direction then y, or other way around
        if (r.nextBoolean()) {

            for (int x = x1; x <= x2; x++) {
                createHallway(tiles, x, roomPrev.centerRoomY());
            }

            for (int y = y1; y <= y2; y++) {
                createHallway(tiles, roomNext.centerRoomX(), y);
            }

        } else {

            for (int y = y1; y <= y2; y++) {
                createHallway(tiles, roomPrev.centerRoomX(), y);
            }

            for (int x = x1; x <= x2; x++) {
                createHallway(tiles, x, roomNext.centerRoomY());
            }
        }
    }

    private void createHallway(TETile[][] tiles, int x, int y) {

        // checks out of bound
        if (x <= 0 || x >= WIDTH-1 || y <= 0 || y >= HEIGHT-1) return;

        if (tiles[x][y] == Tileset.FLOOR) return;

        tiles[x][y] = Tileset.FLOOR;

        // Design world around hallway
        if (tiles[x][y + 1] == Tileset.NOTHING)
            tiles[x][y + 1] = Tileset.WALL;

        if (tiles[x][y - 1] == Tileset.NOTHING)
            tiles[x][y - 1] = Tileset.WALL;

        if (tiles[x + 1][y] == Tileset.NOTHING)
            tiles[x + 1][y] = Tileset.WALL;

        if (tiles[x - 1][y] == Tileset.NOTHING)
            tiles[x - 1][y] = Tileset.WALL;
    }

}