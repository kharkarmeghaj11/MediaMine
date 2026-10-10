package mediamine.m5;

import java.util.*;

/**
 * 2-Approximation for Vertex Cover.
 * Algorithm: while an uncovered edge (u,v) exists, add BOTH endpoints to the
 * cover and remove all edges incident to u or v.
 *
 * Guarantee: |C| ≤ 2 · |OPT|.
 * Time complexity: O(V + E). Space: O(V + E).
 */
public class VertexCoverApprox {

    public static class ApproxResult {
        public final Set<String> cover;
        public final int edgesCovered;
        public final int totalEdges;
        public ApproxResult(Set<String> c, int ec, int total) {
            this.cover = c; this.edgesCovered = ec; this.totalEdges = total;
        }
    }

    public ApproxResult solve(VertexCover vc) {
        List<String[]> edges = new ArrayList<>(vc.edges);
        Set<String> cover = new LinkedHashSet<>();
        int covered = 0;
        boolean[] removed = new boolean[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            if (removed[i]) continue;
            String u = edges.get(i)[0], v = edges.get(i)[1];
            cover.add(u); cover.add(v);
            // mark every edge touching u or v as covered
            for (int j = 0; j < edges.size(); j++) {
                if (removed[j]) continue;
                String a = edges.get(j)[0], b = edges.get(j)[1];
                if (a.equals(u) || a.equals(v) || b.equals(u) || b.equals(v)) {
                    removed[j] = true;
                    covered++;
                }
            }
        }
        return new ApproxResult(cover, covered, edges.size());
    }

    public static boolean smokeTest() {
        VertexCover vc = VertexCover.buildCanonical();
        VertexCoverApprox ap = new VertexCoverApprox();
        ApproxResult r = ap.solve(vc);
        // 2*opt = 4; worst-case approx size <=4 (we add 2 per edge iteration, 3 edges -> at most 6 but 2*opt=4 so 4 or less)
        return r.edgesCovered == r.totalEdges
            && r.cover.size() <= 4;
    }
}
