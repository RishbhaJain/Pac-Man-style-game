package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Game {

    private static final Font TITLE = new Font("Monaco", Font.BOLD, 50);
    private static final Font MENUTEXT = new Font("Monaco", Font.PLAIN, 40);
    private static final Font MENUSEED = new Font("Monaco", Font.PLAIN, 32);
    private static final Integer MAXCOLLECTIBLES = 10;
    private static final Integer MAXENEMIES = 10;
    private static final Integer HUDHEIGHT = 5;
    private static final Integer XOFFSET = 1;
    private static final Integer YOFFSET = 2;

    Enemy[] enemies;
    private static final Integer MAXHEALTH = 10;
    HashMap<Point,Collectible> collectibles;
    private int countEnemies;
    World world;
    TETile[][] worldTiles;
    private static final long ENEMY_UPDATE_INTERVAL = 500;  // Enemy moves every 500ms
    User user;
    TERenderer ter;

    public void createMenu() throws FileNotFoundException {
        GameAudio.playMenuMusic();
        TERenderer ter = new TERenderer();
        ter.initialize(World.WIDTH, World.HEIGHT + HUDHEIGHT);
        this.ter = ter;

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
                    boolean enteringSeed = true;

                    while (enteringSeed) {
                        if (!StdDraw.hasNextKeyTyped()) {
                            StdDraw.pause(10);  // Small pause to prevent tight loop
                            continue;
                        }

                        char c = Character.toUpperCase(StdDraw.nextKeyTyped());

                        if (c == 'S') {
                            if (seedStr.length() > 0) {
                                enteringSeed = false;
                                break;
                            }
                            // If seed is empty, ignore 'S' and continue waiting for input
                            continue;
                        }

                        if (Character.isDigit(c)) {
                            seedStr.append(c);

                            // Update display after each digit
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
                    }

                    long seed = Long.parseLong(seedStr.toString());

                    //Create world
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

                    ter.resetFont();
                    ter.renderFrame(worldTiles);
                    drawHUD(user, ter);
                    interactivity(user, ter);
                    return;

                }

                else if (chr == 'L') {
                    createWorld(SaveLoad.load());
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

    public void interactivity(User user, TERenderer ter) throws FileNotFoundException {
        GameAudio.stopMusic();  // Stop menu music
        GameAudio.playExplorationMusic();
        long lastEnemyUpdate = System.currentTimeMillis();
        char lastChar = '\0';  // Track the previous character
        while (true) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastEnemyUpdate >= ENEMY_UPDATE_INTERVAL) {
                updateEnemies();
                lastEnemyUpdate = currentTime;
                ter.renderFrame(worldTiles);
                drawHUD(user, ter);
            }

            if (!StdDraw.hasNextKeyTyped()) {
                ter.renderFrame(worldTiles);
                drawHUD(user, ter);
                StdDraw.pause(10);
                continue;
            }

            char c = Character.toUpperCase(StdDraw.nextKeyTyped());

            int x = user.x;
            int y = user.y;

            if (c == 'Q' && lastChar == ':') {
                SaveLoad.save(new GameState(
                        user,
                        enemies,
                        collectibles,
                        world.seed
                ));
                System.exit(0);
            }
            if (c == 'W') {
                y++;
                GameAudio.playWalkSound();
            }
            if (c == 'S') {
                y--;
                GameAudio.playWalkSound();
            }
            if (c == 'A') {
                x--;
                GameAudio.playWalkSound();
            }
            if (c == 'D') {
                x++;
                GameAudio.playWalkSound();
            }

            Point playerPos = new Point(x, y);


            if (worldTiles[x][y] == Tileset.WALL) {
                continue;
            }

            if (worldTiles[x][y] == Tileset.ENEMY) {
                user.health -= 1;
                GameAudio.playHitSound();
                if (user.health == 0) {
                    System.exit(0);//check this
                }
            }

            if (worldTiles[x][y] == Tileset.FLOWER) {
                Collectible currColl = collectibles.get(playerPos);
                user.health += currColl.health;
                collectibles.remove(playerPos);
                worldTiles[x][y] = Tileset.FLOOR;
                GameAudio.playCollectSound();
            }

            user.moveAroundMap(x, y, worldTiles);

            ter.resetFont();
            ter.renderFrame(worldTiles);
            drawHUD(user, ter);

            lastChar = c;  // Update lastChar for next iteration
        }
    }
    public void drawHUD(User user, TERenderer ter) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(World.WIDTH / 2.0, World.HEIGHT - 0.5,
                World.WIDTH / 2.0, 2);

        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();

        String desc = "nothing";
        if (mouseX >= 0 && mouseX < World.WIDTH && mouseY >= 0 && mouseY < World.HEIGHT) {
            desc = worldTiles[mouseX][mouseY].description();
        }


        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textLeft(1, World.HEIGHT - 0.5, "Health: " + user.health);
        StdDraw.textLeft(20, World.HEIGHT - 0.5, "Tile: " + desc);

        StdDraw.show();
    }

    public void createWorld(GameState gameState) throws FileNotFoundException {
        this.world = new World(gameState.seed);
        this.worldTiles = world.generateWorld();

        this.user = new User(
                gameState.user.x,
                gameState.user.y,
                gameState.user.health
        );
        worldTiles[user.x][user.y] = Tileset.AVATAR;

        // Generate Collectibles
        this.collectibles = gameState.collectibles;

        for (Collectible coll : collectibles.values() ) {
            worldTiles[coll.x][coll.y] = Tileset.FLOWER;
        }
        // Generate enemies
        this.enemies = gameState.enemies;
        this.countEnemies = enemies.length;
        for (Enemy en :enemies ) {
            worldTiles[en.x][en.y] = Tileset.ENEMY;
        }
        ter.resetFont();
        ter.renderFrame(worldTiles);
        drawHUD(user, ter);
        interactivity(user, ter);
    }
}