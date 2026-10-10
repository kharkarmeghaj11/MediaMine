package mediamine.m5;

import java.util.*;

/**
 * CLIQUE solver using Bron–Kerbosch backtracking on small graphs.
 * Also supports findCliqueOfSize(k) to stop early.
 *
 * Complexity: O(3^(n/3)) worst-case (tight bound for BK); n ≤ 25 works.
 * Space: O(n + m).
 */
public class Clique {

    // adjacency as node -> set of neighbors (undirected)
    public final Map<String, Set<String>> adj = new LinkedHashMap<>();

    public void addNode(String name) { adj.computeIfAbsent(name, k -> new LinkedHashSet<>()); }
    public void addEdge(String a, String b) {
        addNode(a); addNode(b);
        adj.get(a).add(b); adj.get(b).add(a);
    }

    public boolean hasEdge(String a, String b) {
        return adj.containsKey(a) && adj.get(a).contains(b);
    }

    /** Find largest clique (Bron–Kerbosch pivot-free). */
    public List<String> findMaxClique() {
        List<String> best = new ArrayList<>();
        List<String> p = new ArrayList<>(adj.keySet());
        LinkedList<String> x = new LinkedList<>();
        LinkedList<String> current = new LinkedList<>();
        bk(current, p, x, best);
        return best;
    }

    /** Find a clique of size exactly k. Returns Optional.empty() if no such clique. */
    public Optional<List<String>> findCliqueOfSize(int k) {
        List<String> p = new ArrayList<>(adj.keySet());
        List<String> found = new ArrayList<>();
        bkK(new LinkedList<>(), p, new ArrayList<>(), k, found);
        return found.isEmpty() ? Optional.empty() : Optional.of(found);
    }

    private void bk(List<String> r, List<String> p, List<String> x, List<String> best) {
        if (p.isEmpty() && x.isEmpty()) {
            if (r.size() > best.size()) { best.clear(); best.addAll(r); }
            return;
        }
        List<String> pp = new ArrayList<>(p);
        for (String v : pp) {
            r.add(v);
            List<String> p2 = intersect(p, neighbors(v));
            List<String> x2 = intersect(x, neighbors(v));
            bk(r, p2, x2, best);
            r.remove(r.size() - 1);
            p.remove(v);
            x.add(v);
        }
    }

    private boolean bkK(List<String> r, List<String> p, List<String> x, int k, List<String> found) {
        if (r.size() == k) { found.addAll(r); return true; }
        if (r.size() + p.size() < k) return false; // prune
        if (p.isEmpty()) return false;
        List<String> pp = new ArrayList<>(p);
        for (String v : pp) {
            r.add(v);
            List<String> p2 = intersect(p, neighbors(v));
            List<String> x2 = intersect(x, neighbors(v));
            if (bkK(r, p2, x2, k, found)) return true;
            r.remove(r.size() - 1);
            p.remove(v);
            x.add(v);
        }
        return false;
    }

    private Set<String> neighbors(String v) { return adj.getOrDefault(v, Collections.emptySet()); }

    private List<String> intersect(List<String> a, Set<String> b) {
        List<String> out = new ArrayList<>();
        for (String s : a) if (b.contains(s)) out.add(s);
        return out;
    }

    /** Build a topic compatibility graph where topics are nodes and edges mean "compatible"
     *  (all 4 Technology/AI/Education/Healthcare pairwise compatible except AI/Education with Healthcare)
     */
    public static Clique buildCanonicalTopicGraph() {
        Clique g = new Clique();
        String[] names = {"Technology", "AI", "Education", "Healthcare", "Environment"};
        for (String n : names) g.addNode(n);
        g.addEdge("Technology", "AI");
        g.addEdge("Technology", "Education");
        g.addEdge("Technology", "Healthcare");
        g.addEdge("Technology", "Environment");
        g.addEdge("AI", "Education");
        g.addEdge("AI", "Healthcare");
        g.addEdge("Education", "Environment");
        // Healthcare not with Education (clashes in content focus), so max clique size = 3
        return g;
    }

    public static boolean smokeTest() {
        Clique g = buildCanonicalTopicGraph();
        List<String> max = g.findMaxClique();
        if (max.size() < 3) return false;
        // verify it's actually a clique
        for (int i = 0; i < max.size(); i++)
            for (int j = i + 1; j < max.size(); j++)
                if (!g.hasEdge(max.get(i), max.get(j))) return false;
        // k=3 search
        if (!g.findCliqueOfSize(3).isPresent()) return false;
        return true;
    }
}
