package mediamine.m5;

import java.util.*;

/**
 * Vertex Cover — minimum set of vertices touching every edge.
 * Implements brute-force search over subsets by cardinality.
 * For small graphs only (|V| ≤ 15-20).
 *
 * Complexity: exponential (brute force educational demo).
 */
public class VertexCover {

    public final Set<String> nodes = new LinkedHashSet<>();
    public final List<String[]> edges = new ArrayList<>();

    public void addNode(String n) { nodes.add(n); }
    public void addEdge(String a, String b) {
        addNode(a); addNode(b);
        edges.add(new String[]{a, b});
    }

    public Set<String> findMinCover() {
        List<String> nodeList = new ArrayList<>(nodes);
        int n = nodeList.size();
        // check all subsets in order of increasing size
        for (int size = 0; size <= n; size++) {
            Optional<Set<String>> s = subsetsOfSize(nodeList, size, edges);
            if (s.isPresent()) return s.get();
        }
        return new LinkedHashSet<>(nodeList); // should never reach with valid graph
    }

    private Optional<Set<String>> subsetsOfSize(List<String> nodeList, int size, List<String[]> edges) {
        int n = nodeList.size();
        long total = 1L << n;
        for (long mask = 0; mask < total; mask++) {
            if (Long.bitCount(mask) != size) continue;
            Set<String> cover = new LinkedHashSet<>();
            for (int i = 0; i < n; i++) if ((mask & (1L << i)) != 0) cover.add(nodeList.get(i));
            if (coversAll(cover, edges)) return Optional.of(cover);
        }
        return Optional.empty();
    }

    private boolean coversAll(Set<String> c, List<String[]> es) {
        for (String[] e : es) if (!c.contains(e[0]) && !c.contains(e[1])) return false;
        return true;
    }

    /** Build canonical conflict graph:
     *  T-P, Sports-F, Finance-Environment
     */
    public static VertexCover buildCanonical() {
        VertexCover vc = new VertexCover();
        vc.addEdge("Technology", "Politics");
        vc.addEdge("Sports", "Finance");
        vc.addEdge("Finance", "Environment");
        return vc;
    }

    public static boolean smokeTest() {
        VertexCover vc = buildCanonical();
        Set<String> min = vc.findMinCover();
        // must include Finance (covers 2 edges) and T or P
        return min.size() == 2
            && coversAll(vc.edges, min);
    }

    private static boolean coversAll(List<String[]> es, Set<String> c) {
        for (String[] e : es) if (!c.contains(e[0]) && !c.contains(e[1])) return false;
        return true;
    }
}
