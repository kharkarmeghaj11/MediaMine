package mediamine.m4;

import java.util.*;

/**
 * Edmonds-Karp: Ford-Fulkerson with BFS to find SHORTEST augmenting path.
 *
 * Time complexity: O(V · E²) — classic bound.
 * Space: O(V + E) residual graph + O(V) parent/edge pointers per BFS.
 */
public class EdmondsKarp {

    public static class FlowResult {
        public final long maxFlow;
        public final List<List<Integer>> augmentingPaths;
        public FlowResult(long f, List<List<Integer>> p) { this.maxFlow = f; this.augmentingPaths = p; }
    }

    public FlowResult maxFlow(FlowNetwork g) {
        int n = g.vertexCount();
        int s = g.source, t = g.sink;
        long flow = 0;
        List<List<Integer>> paths = new ArrayList<>();
        Edge[] parentEdge = new Edge[n];
        int[] parentNode = new int[n];

        while (true) {
            Arrays.fill(parentEdge, null);
            Arrays.fill(parentNode, -1);
            Queue<Integer> q = new ArrayDeque<>();
            q.offer(s);
            parentNode[s] = s;
            while (!q.isEmpty() && parentNode[t] == -1) {
                int v = q.poll();
                for (Edge e : g.edgesFrom(v)) {
                    int w = (e.to == v) ? e.from : e.to;
                    long res = e.residualTo(w);
                    if (parentNode[w] == -1 && res > 0) {
                        parentNode[w] = v;
                        parentEdge[w] = e;
                        q.offer(w);
                    }
                }
            }
            if (parentNode[t] == -1) break;

            // find bottleneck
            long bottleneck = Long.MAX_VALUE;
            for (int v = t; v != s; v = parentNode[v]) {
                Edge e = parentEdge[v];
                bottleneck = Math.min(bottleneck, e.residualTo(v));
            }
            // push flow
            List<Integer> path = new ArrayList<>();
            for (int v = t; v != s; v = parentNode[v]) {
                Edge e = parentEdge[v];
                e.addFlowTo(v, bottleneck);
                path.add(0, v);
            }
            path.add(0, s);
            paths.add(path);
            flow += bottleneck;
        }
        return new FlowResult(flow, paths);
    }

    public static boolean smokeTest() {
        FlowNetwork g = FlowNetwork.buildChannelGraph();
        EdmondsKarp ek = new EdmondsKarp();
        // for the canonical graph: 250+200+200+150+180+100 == 1080
        if (ek.maxFlow(g).maxFlow != 1080L) return false;

        FlowNetwork rerouting = new FlowNetwork(0, 5, 6);
        rerouting.addEdge(0, 1, 1);
        rerouting.addEdge(0, 2, 1);
        rerouting.addEdge(1, 3, 1);
        rerouting.addEdge(1, 4, 1);
        rerouting.addEdge(2, 3, 1);
        rerouting.addEdge(3, 5, 1);
        rerouting.addEdge(4, 5, 1);
        return new EdmondsKarp().maxFlow(rerouting).maxFlow == 2L;
    }
}
