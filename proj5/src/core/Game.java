package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;

public class Game {

    private static final Font TITLE = new Font("Monaco", Font.BOLD, 50);
    private static final Font MENUTEXT = new Font("Monaco", Font.PLAIN, 40);
    private static final Font MENUSEED = new Font("Monaco", Font.PLAIN, 32);

    public void createMenu() {
        TERenderer ter = new TERenderer();
        ter.initialize(World.WIDTH, World.HEIGHT);

        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(TITLE);
        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT - 10, "Save the Josh");

        StdDraw.setFont(MENUTEXT);
        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0 + 4, "New Game (N)");
        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0, "Load Game (L)");
        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0 - 4, "Quit (Q)");

        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char chr = Character.toUpperCase(StdDraw.nextKeyTyped());

                if (chr == 'N') {

                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.setFont(TITLE);
                    StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0 - 10, "Enter your game seed:");
                    StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0, "Press S when done");
                    StdDraw.show();

                    StringBuilder seedStr = new StringBuilder();
                    boolean input = true;

                    while (input) {
                        if (!StdDraw.hasNextKeyTyped()) continue;
                        char c = Character.toUpperCase(StdDraw.nextKeyTyped());

                        if (c == 'S') {
                            break;
                        }

                        if (Character.isDigit(c)) {
                            seedStr.append(c);
                        }

                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);

                        StdDraw.setFont(TITLE);
                        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT - 10, "Save the Josh");

                        StdDraw.setFont(MENUSEED);
                        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0 + 4, "Enter your game seed:");
                        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0, seedStr.toString());
                        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0 - 4, "Press S when done");

                        StdDraw.show();
                    }

                    long seed = 0;
                    if (!seedStr.isEmpty()) {
                        seed = Long.parseLong(seedStr.toString());
                    }

                    World world = new World(seed);
                    TETile[][] worldTiles = world.generateWorld();

                    Room roomToSpawn = world.rooms.get(0);

                    User user = new User(roomToSpawn.centerRoomX(), roomToSpawn.centerRoomY());
                    worldTiles[roomToSpawn.centerRoomX()][roomToSpawn.centerRoomY()] = Tileset.AVATAR;

                    ter.renderFrame(worldTiles);
                    interactivity(user, worldTiles, ter);
                    return;

                }

                // Still needs to be implemented in Task 4 @Rishbha
                else if (chr == 'L') {
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.setFont(MENUTEXT);
                    StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0, "Load Game");
                    StdDraw.show();
                }

                else if (chr == 'Q') {
                    System.exit(0);
                }
            }
        }
    }

    public void interactivity(User user, TETile[][] world, TERenderer ter) {

        boolean input = true;

        while (input) {
            if (!StdDraw.hasNextKeyTyped()) continue;

            char c = Character.toUpperCase(StdDraw.nextKeyTyped());

            int x = user.x;
            int y = user.y;

            if (c == 'Q') return;
            if (c == 'W') y++;
            if (c == 'S') y--;
            if (c == 'A') x--;
            if (c == 'D') x++;


            if (world[x][y] == Tileset.WALL) {
                continue;
            }

            user.moveAroundMap(x, y, world);
            ter.renderFrame(world);
        }
    }
}