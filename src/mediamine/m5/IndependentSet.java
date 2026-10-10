package mediamine.m5;

import java.util.*;

/**
 * Independent Set (IS) on graph G.
 *
 * Relationship (classical): Clique in complement(G)  <==>  Independent Set in G.
 * We compute it by running Clique.findMaxClique on the complement graph.
 *
 * Complexity: same as Clique — O(3^(n/3)). For small demo graphs only.
 * Space: O(n + m).
 */
public class IndependentSet {

    private final Set<String> nodes = new LinkedHashSet<>();
    private final Set<String> edgeSet = new LinkedHashSet<>(); // "a|b" with a<b

    public void addNode(String n) { nodes.add(n); }
    public void addEdge(String a, String b) {
        nodes.add(a); nodes.add(b);
        String k = key(a, b);
        edgeSet.add(k);
    }

    public List<String> findMaxIndependentSet() {
        Clique complement = new Clique();
        List<String> nodeList = new ArrayList<>(nodes);
        for (String n : nodeList) complement.addNode(n);
        for (int i = 0; i < nodeList.size(); i++)
            for (int j = i + 1; j < nodeList.size(); j++) {
                String a = nodeList.get(i), b = nodeList.get(j);
                if (!edgeSet.contains(key(a, b))) complement.addEdge(a, b); // only add if original missing
            }
        return complement.findMaxClique();
    }

    /** Count actual edges among these nodes (should be 0 for an IS) */
    public int countEdgesWithin(Collection<String> set) {
        List<String> list = new ArrayList<>(set);
        int c = 0;
        for (int i = 0; i < list.size(); i++)
            for (int j = i + 1; j < list.size(); j++)
                if (edgeSet.contains(key(list.get(i), list.get(j)))) c++;
        return c;
    }

    private String key(String a, String b) {
        int cmp = a.compareTo(b);
        return (cmp <= 0) ? (a + "|" + b) : (b + "|" + a);
    }

    public static boolean smokeTest() {
        IndependentSet g = new IndependentSet();
        // triangle graph: nodes A-B, B-C, A-C -> max independent set = 1
        g.addNode("A"); g.addNode("B"); g.addNode("C");
        g.addEdge("A","B"); g.addEdge("B","C"); g.addEdge("A","C");
        List<String> mis = g.findMaxIndependentSet();
        return mis.size() == 1 && g.countEdgesWithin(mis) == 0;
    }
}
