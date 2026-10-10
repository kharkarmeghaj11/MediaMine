package mediamine.m4;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * Dinic's algorithm: level graph (BFS) + blocking flow DFS with current-edge pointer.
 *
 * Complexity: O(V² · E). Much faster in practice for unit-cap networks or layered DAGs.
 * Space: O(V + E) for graph, O(V) for level/ptr arrays.
 */
public class Dinic {

    public static class FlowResult {
        public final long maxFlow;
        public final List<List<Integer>> augmentingPaths;
        public FlowResult(long f, List<List<Integer>> p) { this.maxFlow = f; this.augmentingPaths = p; }
    }

    public FlowResult maxFlow(FlowNetwork g) {
        int n = g.vertexCount();
        int s = g.source, t = g.sink;
        long flow = 0;
        int[] level = new int[n];
        int[] ptr = new int[n];
        List<List<Integer>> paths = new ArrayList<>();

        while (bfs(g, s, t, level)) {
            Arrays.fill(ptr, 0);
            while (true) {
                List<Integer> path = new ArrayList<>();
                long pushed = dfs(g, s, t, Long.MAX_VALUE, level, ptr, path);
                if (pushed <= 0) break;
                flow += pushed;
                paths.add(path);
            }
        }
        return new FlowResult(flow, paths);
    }

    private boolean bfs(FlowNetwork g, int s, int t, int[] level) {
        Arrays.fill(level, -1);
        level[s] = 0;
        Queue<Integer> q = new ArrayDeque<>();
        q.offer(s);
        while (!q.isEmpty()) {
            int v = q.poll();
            for (Edge e : g.edgesFrom(v)) {
                int w = (e.to == v) ? e.from : e.to;
                long res = e.residualTo(w);
                if (level[w] == -1 && res > 0) {
                    level[w] = level[v] + 1;
                    q.offer(w);
                }
            }
        }
        return level[t] != -1;
    }

    private long dfs(FlowNetwork g, int v, int t, long bottleneck,
                     int[] level, int[] ptr, List<Integer> pathOut) {
        if (v == t) {
            pathOut.add(t);
            return bottleneck;
        }
        pathOut.add(v);
        List<Edge> edges = g.edgesFrom(v);
        int edgesN = edges.size();
        for (; ptr[v] < edgesN; ptr[v]++) {
            Edge e = edges.get(ptr[v]);
            int w = (e.to == v) ? e.from : e.to;
            long res = e.residualTo(w);
            if (level[w] == level[v] + 1 && res > 0) {
                long pushed = dfs(g, w, t, Math.min(bottleneck, res), level, ptr, pathOut);
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
        FlowNetwork g = FlowNetwork.buildChannelGraph();
        Dinic d = new Dinic();
        if (d.maxFlow(g).maxFlow != 1080L) return false;

        FlowNetwork rerouting = new FlowNetwork(0, 5, 6);
        rerouting.addEdge(0, 1, 1);
        rerouting.addEdge(0, 2, 1);
        rerouting.addEdge(1, 3, 1);
        rerouting.addEdge(1, 4, 1);
        rerouting.addEdge(2, 3, 1);
        rerouting.addEdge(3, 5, 1);
        rerouting.addEdge(4, 5, 1);
        return new Dinic().maxFlow(rerouting).maxFlow == 2L;
    }
}
