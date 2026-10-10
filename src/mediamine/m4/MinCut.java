package mediamine.m4;

import java.util.*;

/**
 * Min-Cut: after max flow is computed, BFS the residual graph from the source
 * (only edges with residual capacity > 0). Nodes reachable from source are in
 * set S; the S/T cut capacity is the sum of capacities of original edges that
 * start in S and land outside S. These edges are the CRITICAL DISTRIBUTION
 * LINKS — upgrading their capacity increases max flow.
 *
 * Complexity: O(V + E).
 * Space: O(V) visited mask.
 */
public class MinCut {

    public static class MinCutResult {
        public final long cutCapacity;
        public final Set<Integer> sSet; // reachable from source via residual edges
        public final List<String> criticalLinks; // human-readable "A -> B" strings
        public MinCutResult(long c, Set<Integer> s, List<String> links) {
            this.cutCapacity = c; this.sSet = s; this.criticalLinks = links;
        }
    }

    public MinCutResult compute(FlowNetwork g) {
        int n = g.vertexCount();
        int s = g.source;
        boolean[] reach = new boolean[n];
        Queue<Integer> q = new ArrayDeque<>();
        q.offer(s);
        reach[s] = true;
        while (!q.isEmpty()) {
            int v = q.poll();
            for (Edge e : g.edgesFrom(v)) {
                int w = (e.to == v) ? e.from : e.to;
                long res = e.residualTo(w);
                if (!reach[w] && res > 0) {
                    reach[w] = true;
                    q.offer(w);
                }
            }
        }

        Set<Integer> sSet = new HashSet<>();
        for (int i = 0; i < n; i++) if (reach[i]) sSet.add(i);

        long cut = 0;
        Set<String> linkSet = new LinkedHashSet<>();
        for (int v = 0; v < n; v++) {
            if (!sSet.contains(v)) continue;
            for (Edge e : g.edgesFrom(v)) {
                // only consider ORIGINAL forward edges (cap > 0) where to is not in S
                if (e.from == v && e.capacity > 0 && !sSet.contains(e.to)) {
                    cut += e.capacity;
                    String fromName = g.nameOf(v);
                    String toName = g.nameOf(e.to);
                    linkSet.add(fromName + " -> " + toName);
                }
            }
        }
        List<String> criticalLinks = new ArrayList<>(linkSet);
        Collections.sort(criticalLinks);
        return new MinCutResult(cut, sSet, criticalLinks);
    }

    public static boolean smokeTest() {
        FlowNetwork g = FlowNetwork.buildChannelGraph();
        EdmondsKarp ek = new EdmondsKarp();
        ek.maxFlow(g);
        MinCut mc = new MinCut();
        MinCutResult r = mc.compute(g);
        // for canonical graph, cut capacity == max flow by max-flow/min-cut theorem:
        return r.cutCapacity == 1080L && !r.criticalLinks.isEmpty();
    }
}
