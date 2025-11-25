package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class HallwayGenerator {

    public List<Edge> generateEdges(List<Room> rooms) {

        List<Edge> edges = new ArrayList<>();
        int n = rooms.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int weight = manhattanDistance(
                        rooms.get(i).centerRoomX(), rooms.get(i).centerRoomY(),
                        rooms.get(j).centerRoomX(), rooms.get(j).centerRoomY()
                );

                edges.add(new Edge(i, j, weight));
            }
        }

        Collections.sort(edges);
        return edges;
    }

    public List<Edge> findMST(List<Edge> edges, int numRooms) {
        DisjointSets ds = new DisjointSets(numRooms);
        List<Edge> mst = new ArrayList<>();

        for (Edge edge : edges) {
            if (!ds.isConnected(edge.roomPrev, edge.roomNext)) {
                ds.union(edge.roomPrev, edge.roomNext);
                mst.add(edge);
            }
        }

        return mst;
    }

    private int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}

