package core;

public class Edge implements Comparable<Edge> {
    public int roomPrev;
    public int roomNext;
    public int weight;

    public Edge(int roomPrev, int roomNext, int weight) {
        this.roomPrev = roomPrev;
        this.roomNext = roomNext;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return this.weight - other.weight;
    }
}
