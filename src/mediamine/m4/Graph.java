package mediamine.m4;

import java.util.ArrayList;
import java.util.List;

/**
 * Adjacency-list directed (multi-)graph.
 * Every addEdge(u,v,cap) also adds the reverse 0-capacity edge for residual
 * networks and flow algorithms.
 *
 * Time: addEdge O(1), vertices lookup O(V), edge iteration O(1) per edge.
 * Space: O(V + E).
 */
public class Graph {
    protected final List<List<Edge>> adj;

    public Graph() {
        adj = new ArrayList<>();
    }

    public Graph(int n) {
        adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public int addVertex() {
        adj.add(new ArrayList<>());
        return adj.size() - 1;
    }

    public int vertexCount() {
        return adj.size();
    }

    public void ensureVertices(int n) {
        while (adj.size() < n) adj.add(new ArrayList<>());
    }

    /** Add forward edge + reverse edge, cross-linking rev indices. */
    public Edge addEdge(int from, int to, long capacity) {
        if (from < 0 || to < 0) throw new IllegalArgumentException("Vertex IDs must be non-negative");
        if (capacity < 0) throw new IllegalArgumentException("Capacity must be non-negative");
        ensureVertices(Math.max(from, to) + 1);
        Edge forward = new Edge(from, to, capacity);
        Edge back = new Edge(to, from, 0L);
        forward.rev = adj.get(to).size();
        back.rev = adj.get(from).size();
        forward.reverse = back;
        back.reverse = forward;
        adj.get(from).add(forward);
        adj.get(to).add(back);
        return forward;
    }

    public List<Edge> edgesFrom(int v) {
        if (v < 0 || v >= adj.size()) return new ArrayList<>();
        return adj.get(v);
    }

    public int edgeCount() {
        int c = 0;
        for (List<Edge> l : adj) c += l.size();
        return c / 2; // each direction counted twice (forward + rev residual pair)
    }

    public static boolean smokeTest() {
        Graph g = new Graph(2);
        g.addEdge(0, 1, 5);
        if (g.vertexCount() != 2) return false;
        long r = g.edgesFrom(0).get(0).residualTo(1);
        return r == 5;
    }
}
