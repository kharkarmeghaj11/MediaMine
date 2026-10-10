package mediamine.services;

import mediamine.m4.*;
import mediamine.m6.RandomizedQuickSort;

/**
 * AlgorithmRouter — automatic internal algorithm selection.
 * Users NEVER pick algorithms; MediaMine features request an operation and the
 * router dispatches to the best-suited implementation.
 *
 * Routing rules:
 *  - Distribution Network flow: nodes < 20 → Ford-Fulkerson; 20..100 → Edmonds-Karp; >100 → Dinic.
 *  - Search: single keyword → KMP; multi-keyword → Aho-Corasick; typo-heavy → Levenshtein correction.
 *  - Transcript Analysis: similarity → SuffixArray+LCP+Kasai; substring exists → SuffixAutomaton.
 *  - Content Constraints: arbitrary clauses → SATSolver; 3-literal max → ThreeSAT; graph analysis → Clique/VertexCover.
 *  - Ranking: Randomized QuickSort (always).
 *  - Streaming sampling: Reservoir Sampling (always).
 *  - Large numeric verification: Miller-Rabin primality test.
 */
public class AlgorithmRouter {

    public static class FlowChoice {
        public final String algorithmName;
        public final String reason;
        FlowChoice(String n, String r) { algorithmName = n; reason = r; }
    }

    /** Pick max-flow algorithm by graph size. */
    public static FlowChoice chooseFlowAlgorithm(int nodes, int edges) {
        int complexity = nodes + edges;
        if (complexity < 30) {
            return new FlowChoice("Ford-Fulkerson", "Small network (<30 total nodes+edges): DFS-augmenting-path is simplest and fastest");
        }
        if (complexity < 300) {
            return new FlowChoice("Edmonds-Karp", "Medium network: BFS shortest-augmenting-path gives O(VE²) guaranteed bound");
        }
        return new FlowChoice("Dinic", "Larger network: Dinic level-graph + blocking-flow ptr is optimal");
    }

    /** Run max flow on given FlowNetwork using auto-chosen algorithm. */
    public static <R> long runMaxFlow(FlowNetwork g) {
        FlowChoice c = chooseFlowAlgorithm(g.vertexCount(), g.edgeCount());
        switch (c.algorithmName) {
            case "Ford-Fulkerson": return new FordFulkerson().maxFlow(g).maxFlow;
            case "Edmonds-Karp":   return new EdmondsKarp().maxFlow(g).maxFlow;
            case "Dinic":          return new Dinic().maxFlow(g).maxFlow;
            default:               return new EdmondsKarp().maxFlow(g).maxFlow;
        }
    }

    /** Media ranking sort implementation (always randomized quicksort). */
    public static <T extends Comparable<? super T>> void sortRanking(java.util.List<T> list) {
        RandomizedQuickSort.sort(list);
    }
}
