package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;

import java.awt.*;

public class Game {

    private static final Font TITLE = new Font("Monaco", Font.BOLD, 50);
    private static final Font MENUTEXT = new Font("Monaco", Font.PLAIN, 40);
    private static final Font ENV = new Font("Monaco", Font.PLAIN, 30);
    private static final Font MENUSEED = new Font("Monaco", Font.PLAIN, 32);

    public void createMenu() {
        TERenderer ter = new TERenderer();
        ter.initialize(World.WIDTH, World.HEIGHT);

        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(TITLE);
        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT, "Save the Josh");

        StdDraw.setFont(MENUTEXT);
        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0 + 2, "New Game (N)");
        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0, "Load Game (L)");
        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0 - 2, "Quit (Q)");

        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char chr = Character.toUpperCase(StdDraw.nextKeyTyped());

                if (chr == 'N') {

                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.setFont(ENV);
                    StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0 + 5, "Enter your game seed:");
                    StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0, "Press S when done");
                    StdDraw.show();

                    StringBuilder seedStr = new StringBuilder();
                    boolean input = true;

                    while (input) {
                        if (!StdDraw.hasNextKeyTyped()) continue;
                        char c = Character.toUpperCase(StdDraw.nextKeyTyped());

                        if (c == 'S') {
                            input = false;
                            break;
                        }

                        if (Character.isDigit(c)) {
                            seedStr.append(c);
                        }

                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);

                        StdDraw.setFont(TITLE);
                        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT - 3, "Save the Josh");

                        StdDraw.setFont(MENUSEED);
                        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0 + 2, "Enter your game seed:");
                        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0, seedStr.toString());
                        StdDraw.text(World.WIDTH / 2.0, World.HEIGHT / 2.0 - 2, "Press S when done");

                        StdDraw.show();
                    }

                    long seed = 0;
                    if (!seedStr.isEmpty()) {
                        seed = Long.parseLong(seedStr.toString());
                    }

                    World w = new World(seed);
                    TETile[][] worldTiles = w.generateWorld();
                    ter.renderFrame(worldTiles);
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
}

