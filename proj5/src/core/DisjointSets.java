package core;

public class DisjointSets {

    private final int[] parent;

    public DisjointSets(int size) {
        parent = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = -1;
        }
    }

    private int find(int p) {
        while (parent[p] >= 0) {
            p = parent[p];
        }
        return p;
    }

    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        parent[rootP] = rootQ;
    }
}
