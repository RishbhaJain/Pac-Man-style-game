package core;

import java.awt.*;
import java.util.HashMap;

public class GameState {
    public User user;
    public Enemy[] enemies;
    public HashMap<Point, Collectible> collectibles;
    public long seed;

    public GameState(User user,
                     Enemy[] enemies,
                     HashMap<Point, Collectible> collectibles,
                     long seed) {
        this.user = user;
        this.enemies = enemies;
        this.collectibles = collectibles;
        this.seed = seed;
    }
}