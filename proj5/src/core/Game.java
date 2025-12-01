package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.HashMap;

public class Game {

    private static final Font TITLE = new Font("Monaco", Font.BOLD, 50);
    private static final Font MENUTEXT = new Font("Monaco", Font.PLAIN, 40);
    private static final Font MENUSEED = new Font("Monaco", Font.PLAIN, 32);
    private static final Integer MAXCOLLECTIBLES = 10;
    private static final Integer MAXENEMIES = 10;
    Enemy[] enemies;

    private static final Integer MAXHEALTH = 10;
    HashMap<Point,Collectible> collectibles;
    private int countEnemies;
    World world;
    TETile[][] worldTiles;
    private static final long ENEMY_UPDATE_INTERVAL = 500;  // Enemy moves every 500ms



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
                    this.world = world;
                    TETile[][] worldTiles = world.generateWorld();
                    this.worldTiles = worldTiles;

                    Room roomToSpawn = world.getRandomRoom();
                    User user = new User(roomToSpawn.centerRoomX(), roomToSpawn.centerRoomY());
                    worldTiles[roomToSpawn.centerRoomX()][roomToSpawn.centerRoomY()] = Tileset.AVATAR;

                    // Generate Collectibles
                    int countCollectibles = 1 + world.r.nextInt(MAXCOLLECTIBLES-1);
                    Room roomToCollect;
                    collectibles = new HashMap<>();
                    for (int i = 0; i < countCollectibles; i ++ ) {
                        //make sure collectible and user don't overlap
                        while (true) {
                            roomToCollect = world.getRandomRoom();
                            if (roomToCollect.equals(roomToSpawn)) {
                                continue;
                            }
                            break;
                        }
                        collectibles.put(new Point(roomToCollect.centerRoomX(), roomToCollect.centerRoomY()), new Collectible(roomToCollect.centerRoomX(), roomToCollect.centerRoomY(),1 + world.r.nextInt(MAXHEALTH - 1)));
                        worldTiles[roomToCollect.centerRoomX()][roomToCollect.centerRoomY()] = Tileset.FLOWER;
                    }

                    // Generate enemies
                    Room roomToEnemy;
                    countEnemies = 1 + world.r.nextInt(MAXENEMIES-1);
                    enemies = new Enemy[countEnemies];
                    for (int i = 0; i < countEnemies; i ++ ) {
                        //Make sure enemy and user don't start at the same position
                        while (true) {
                            roomToEnemy = world.getRandomRoom();
                            if (roomToEnemy.equals(roomToSpawn)) {
                                continue;
                            }
                            break;
                        }
                        enemies[i] = new Enemy(roomToEnemy.centerRoomX(), roomToEnemy.centerRoomY());
                        worldTiles[roomToEnemy.centerRoomX()][roomToEnemy.centerRoomY()] = Tileset.ENEMY;
                    }


                    ter.renderFrame(worldTiles);
                    interactivity(user, ter);
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

    public void updateEnemies() {
        for (int i = 0; i < countEnemies; i++) {
            enemies[i].moveAroundMap(worldTiles,world);
        }
    }

    public void interactivity(User user, TERenderer ter) {
        long lastEnemyUpdate = System.currentTimeMillis();
        while (true) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastEnemyUpdate >= ENEMY_UPDATE_INTERVAL) {
                updateEnemies();
                lastEnemyUpdate = currentTime;
                ter.renderFrame(worldTiles);
            }

            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }

            char c = Character.toUpperCase(StdDraw.nextKeyTyped());

            int x = user.x;
            int y = user.y;

            if (c == 'Q') return;
            if (c == 'W') y++;
            if (c == 'S') y--;
            if (c == 'A') x--;
            if (c == 'D') x++;

            Point playerPos = new Point(x, y);


            if (worldTiles[x][y] == Tileset.WALL) {
                continue;
            }

            if (worldTiles[x][y] == Tileset.ENEMY) {
                user.health -= 1;
                if (user.health == 0) {
                    System.exit(0);//check this
                }
            }

            if (worldTiles[x][y] == Tileset.FLOWER) {
                Collectible currColl = collectibles.get(playerPos);
                user.health += currColl.health;
                collectibles.remove(playerPos);
                worldTiles[x][y] = Tileset.FLOOR;
            }

            user.moveAroundMap(x, y, worldTiles);

            ter.renderFrame(worldTiles);
        }
    }
}