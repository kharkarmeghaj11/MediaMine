package mediamine.m4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ford-Fulkerson with DFS-based augmenting paths.
 *
 * Complexity: O(F·E) where F = max flow value and E = number of edges.
 * Space: O(V + E) residual graph + O(V) visited array per DFS.
 */
public class FordFulkerson {

    public static class FlowResult {
        public final long maxFlow;
        public final List<List<Integer>> augmentingPaths;
        public FordFulkerson source;
        public FlowResult(long f, List<List<Integer>> p) { this.maxFlow = f; this.augmentingPaths = p; }
    }

    public FlowResult maxFlow(FlowNetwork g) {
        int n = g.vertexCount();
        int s = g.source, t = g.sink;
        long flow = 0;
        boolean[] visited = new boolean[n];
        List<List<Integer>> paths = new ArrayList<>();

        while (true) {
            Arrays.fill(visited, false);
            List<Integer> path = new ArrayList<>();
            long pushed = dfs(g, s, t, Long.MAX_VALUE, visited, path);
            if (pushed <= 0) break;
            flow += pushed;
            paths.add(new ArrayList<>(path));
        }
        return new FlowResult(flow, paths);
    }

    private long dfs(FlowNetwork g, int v, int t, long bottleneck,
                     boolean[] visited, List<Integer> pathOut) {
        if (v == t) {
            pathOut.add(t);
            return bottleneck;
        }
        visited[v] = true;
        pathOut.add(v);
        for (Edge e : g.edgesFrom(v)) {
            int w = (e.to == v) ? e.from : e.to; // other end
            long res = e.residualTo(w);
            if (!visited[w] && res > 0) {
                long pushed = dfs(g, w, t, Math.min(bottleneck, res), visited, pathOut);
                if (pushed > 0) {
                    e.addFlowTo(w, pushed);
                    return pushed;
                }
            }
        }
        pathOut.remove(pathOut.size() - 1);
        return 0;
    }

    public static boolean smokeTest() {
        // Classic 2-node graph: source -> sink, cap 5.
        FlowNetwork g = new FlowNetwork(0, 1, 2);
        g.addEdge(0, 1, 5L);
        FordFulkerson ff = new FordFulkerson();
        if (ff.maxFlow(g).maxFlow != 5L) return false;

        FlowNetwork rerouting = reroutingNetwork();
        return new FordFulkerson().maxFlow(rerouting).maxFlow == 2L;
    }

    private static FlowNetwork reroutingNetwork() {
        FlowNetwork g = new FlowNetwork(0, 5, 6);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(1, 4, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 5, 1);
        g.addEdge(4, 5, 1);
        return g;
    }
}
