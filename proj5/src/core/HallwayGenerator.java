package core;

import edu.princeton.cs.algs4.Edge;

import java.util.ArrayList;
import java.util.List;

public class HallwayGenerator {

    private List<Edge> mst = new ArrayList<Edge>();

    public HallwayGenerator() {

    }

    public void connectRooms(int x, int y){
        int

    }

    public void connectHallway(){

    }

    private int centerX(Room r) {
        return r.x + r.width / 2;
    }

    private int centerY(Room r) {
        return r.y + r.height / 2;
    }

    private int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

}

