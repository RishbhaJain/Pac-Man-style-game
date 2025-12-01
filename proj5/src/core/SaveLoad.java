package core;

import edu.princeton.cs.algs4.In;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * This demo shows how to save and load game state.
 * In this demo, we create and use a "name.txt" file to save and load an inputted name.
 */
public class SaveLoad {
    private static final String filename = "Saved_Game.txt";

    public static void save(GameState gameState) throws FileNotFoundException {
        Long seed = gameState.seed;
        HashMap<Point, Collectible> collectible = gameState.collectibles;
        Enemy[] enemies = gameState.enemies;;
        User user = gameState.user;
        PrintWriter out = new PrintWriter(filename);
        out.println(seed);
        out.println(user.x + "," + user.y + "," + user.health);
        out.println(enemies.length);
        for (Enemy enemy : enemies) {
            out.println(enemy.x + "," + enemy.y);
        }
        out.println(collectible.size());
        for (Collectible coll : collectible.values()) {
            out.println(coll.x + "," + coll.y + "," + coll.health);
        }
        out.close();
    }

    public static GameState load() {
        In in = new In(filename);
        long seed = in.readLong();
        in.readLine();
        String[] parts = in.readLine().split(",");

        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        int health = Integer.parseInt(parts[2]);

        User user = new User(x,y,health);
        int countEnemies = Integer.parseInt(in.readLine());
        Enemy[] enemies = new Enemy[countEnemies];
        for (int i = 0; i < countEnemies; i ++ ) {
            parts = in.readLine().split(",");
            x = Integer.parseInt(parts[0]);
            y = Integer.parseInt(parts[1]);
            enemies[i] = new Enemy(x,y);
        }
        HashMap<Point,Collectible> collectibles = new HashMap<>();
        int countCollectibles = Integer.parseInt(in.readLine());
        for (int i = 0; i < countCollectibles; i ++ ) {
            parts = in.readLine().split(",");
            x = Integer.parseInt(parts[0]);
            y = Integer.parseInt(parts[1]);
            health = Integer.parseInt(parts[2]);
            collectibles.put(new Point(x,y), new Collectible(x,y,health));
        }
        return new GameState(user, enemies, collectibles, seed);
    }


}