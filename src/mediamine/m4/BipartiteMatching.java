package mediamine.m4;

import java.util.*;

/**
 * Bipartite Matching for MEDIA -> CHANNEL assignment.
 * Implemented as a simple DFS-augmenting-path per left node (Ford-Fulkerson
 * style on bipartite graph via reduction to unweighted max-flow).
 *
 * Time: O(V·E) where V = |left| + |right|, E = eligibility edges.
 * Space: O(V + E) adjacency.
 */
public class BipartiteMatching {

    public static class MatchingResult {
        public final int size;
        public final Map<String, String> mediaToChannel;
        public final List<String> unassignedMedia;
        public MatchingResult(int s, Map<String,String> m, List<String> un) {
            this.size = s; this.mediaToChannel = m; this.unassignedMedia = un;
        }
    }

    public final List<String> leftMedia;
    public final List<String> rightChannels;
    public final Map<String, Set<String>> eligibility; // media -> allowed channels

    public BipartiteMatching(List<String> left, List<String> right) {
        this.leftMedia = new ArrayList<>(left);
        this.rightChannels = new ArrayList<>(right);
        this.eligibility = new LinkedHashMap<>();
        for (String m : left) eligibility.put(m, new LinkedHashSet<>());
    }

    public void addEligibility(String media, String channel) {
        eligibility.computeIfAbsent(media, k -> new LinkedHashSet<>()).add(channel);
    }

    public MatchingResult solve() {
        // index channels
        Map<String, Integer> chIdx = new LinkedHashMap<>();
        for (int i = 0; i < rightChannels.size(); i++) chIdx.put(rightChannels.get(i), i);

        // adjacency list: leftMedia-index -> list of right indices
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < leftMedia.size(); i++) {
            Set<String> allowed = eligibility.get(leftMedia.get(i));
            List<Integer> row = new ArrayList<>();
            if (allowed != null) for (String c : allowed) {
                Integer ii = chIdx.get(c);
                if (ii != null) row.add(ii);
            }
            adj.add(row);
        }

        int[] matchL = new int[leftMedia.size()]; Arrays.fill(matchL, -1);
        int[] matchR = new int[rightChannels.size()]; Arrays.fill(matchR, -1);
        boolean[] seen = new boolean[rightChannels.size()];

        int matches = 0;
        for (int u = 0; u < leftMedia.size(); u++) {
            Arrays.fill(seen, false);
            if (dfs(u, adj, seen, matchL, matchR)) matches++;
        }

        Map<String, String> assign = new LinkedHashMap<>();
        List<String> unassigned = new ArrayList<>();
        for (int u = 0; u < leftMedia.size(); u++) {
            String media = leftMedia.get(u);
            if (matchL[u] == -1) unassigned.add(media);
            else assign.put(media, rightChannels.get(matchL[u]));
        }
        return new MatchingResult(matches, assign, unassigned);
    }

    private boolean dfs(int u, List<List<Integer>> adj, boolean[] seen,
                        int[] matchL, int[] matchR) {
        for (int v : adj.get(u)) {
            if (seen[v]) continue;
            seen[v] = true;
            if (matchR[v] == -1 || dfs(matchR[v], adj, seen, matchL, matchR)) {
                matchL[u] = v;
                matchR[v] = u;
                return true;
            }
        }
        return false;
    }

    /** Build the canonical MediaMine bipartite instance per spec. */
    public static BipartiteMatching buildCanonical() {
        List<String> left = Arrays.asList(
            "AI in Education",
            "Climate Change Report",
            "Football Championship",
            "Global Economy Update",
            "Space Exploration",
            "AI Healthcare"
        );
        List<String> right = Arrays.asList(
            "Technology Channel",
            "Environment Channel",
            "Sports Channel",
            "Finance Channel",
            "Science Channel"
        );
        BipartiteMatching bm = new BipartiteMatching(left, right);
        bm.addEligibility("AI in Education", "Technology Channel");
        bm.addEligibility("AI in Education", "Science Channel");
        bm.addEligibility("Climate Change Report", "Environment Channel");
        bm.addEligibility("Climate Change Report", "Science Channel");
        bm.addEligibility("Football Championship", "Sports Channel");
        bm.addEligibility("Global Economy Update", "Finance Channel");
        bm.addEligibility("Space Exploration", "Science Channel");
        bm.addEligibility("Space Exploration", "Technology Channel");
        bm.addEligibility("AI Healthcare", "Technology Channel");
        bm.addEligibility("AI Healthcare", "Science Channel");
        return bm;
    }

    public static boolean smokeTest() {
        BipartiteMatching bm = buildCanonical();
        MatchingResult r = bm.solve();
        return r.size == 5 && r.unassignedMedia.size() == 1;
    }
}
