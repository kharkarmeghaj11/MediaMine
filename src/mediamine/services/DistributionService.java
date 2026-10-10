package mediamine.services;

import mediamine.m4.*;
import mediamine.m4.BipartiteMatching.MatchingResult;
import mediamine.m4.MinCut.MinCutResult;

import java.util.*;

/**
 * Distribution Service:
 *   - builds FlowNetwork (CDN graph)
 *   - runs max flow via AlgorithmRouter.auto
 *   - runs bipartite matching for Media→Channel assignment.
 *   - runs Min-Cut to flag critical distribution links.
 */
public class DistributionService {

    public static class DistributionReport {
        public final int networkNodes;
        public final int networkConnections;
        public final long maxFlowUnits;
        public final Map<String, Long> channelFlows; // Channel -> capacity used
        public final double utilizationPct;
        public final MatchingResult assignment;
        public final MinCutResult minCut;
        public final String flowAlgorithmUsed;
        public final String routingReason;

        public DistributionReport(int n, int e, long mf, Map<String, Long> cf, double u,
                                  MatchingResult ar, MinCutResult mc,
                                  String algo, String reason) {
            this.networkNodes = n;
            this.networkConnections = e;
            this.maxFlowUnits = mf;
            this.channelFlows = cf;
            this.utilizationPct = u;
            this.assignment = ar;
            this.minCut = mc;
            this.flowAlgorithmUsed = algo;
            this.routingReason = reason;
        }
    }

    public DistributionReport analyze() {
        // (a) Max flow
        FlowNetwork g = FlowNetwork.buildChannelGraph();
        AlgorithmRouter.FlowChoice choice = AlgorithmRouter.chooseFlowAlgorithm(g.vertexCount(), g.edgeCount());
        long maxFlow = AlgorithmRouter.runMaxFlow(g);

        // Read delivered flow from the channel-to-sink edges after max flow.
        Map<String, Long> channelFlows = new LinkedHashMap<>();
        for (int v = 4; v <= 9; v++) {
            String name = g.nameOf(v);
            long f = 0;
            for (Edge e : g.edgesFrom(v)) {
                if (e.from == v && e.to == g.sink) { f = e.flow; break; }
            }
            channelFlows.put(name, f);
        }

        long serverCapacity = 0;
        for (Edge e : g.edgesFrom(g.source)) {
            if (e.from == g.source && e.capacity > 0) serverCapacity += e.capacity;
        }
        double util = serverCapacity == 0 ? 0 : (100.0 * maxFlow / serverCapacity);

        // (b) Bipartite matching
        BipartiteMatching bm = BipartiteMatching.buildCanonical();
        MatchingResult match = bm.solve();

        // (c) Min cut
        MinCut mc = new MinCut();
        MinCutResult cut = mc.compute(g);

        return new DistributionReport(g.vertexCount(), g.edgeCount(),
            maxFlow, channelFlows, util, match, cut,
            choice.algorithmName, choice.reason);
    }
}
