package core;

import java.awt.*;
import java.util.HashMap;

public class GameState {
    public User user;
    public Enemy[] enemies;
    public HashMap<Point, Collectible> collectibles;
    public Long seed;


    public GameState(User user, Enemy[] enemies, HashMap<Point, Collectible> collectibles, Long seed) {
        this.user = user;
        this.enemies = enemies;
        this.collectibles = collectibles;
        this.seed = seed;
    }
}
