package mediamine.m4;

import java.util.ArrayList;
import java.util.List;

/**
 * FlowNetwork = Graph + designated source / sink + nodeNames for pretty output.
 *
 * buildChannelGraph() creates the exact MediaMine spec graph:
 *   Source(0) -> CDN1(1), CDN2(2), CDN3(3)
 *   CDN1 -> Technology Channel, Education Channel
 *   CDN2 -> Finance Channel, News Channel
 *   CDN3 -> Sports Channel, Science Channel
 *   each Channel -> Sink
 *
 * Complexity: Build is O(1) fixed graph. Storage O(V+E).
 */
public class FlowNetwork extends Graph {
    public final int source;
    public final int sink;
    public final List<String> nodeNames;

    public FlowNetwork(int source, int sink, int expectedVertices) {
        super(expectedVertices);
        this.source = source;
        this.sink = sink;
        this.nodeNames = new ArrayList<>();
        ensureVertices(expectedVertices);
        while (nodeNames.size() < expectedVertices) nodeNames.add("?");
    }

    public void nameNode(int v, String name) {
        ensureVertices(v + 1);
        while (nodeNames.size() <= v) nodeNames.add("?");
        nodeNames.set(v, name);
    }

    public String nameOf(int v) {
        if (v < 0 || v >= nodeNames.size()) return "node" + v;
        String n = nodeNames.get(v);
        return (n == null || n.equals("?")) ? "node" + v : n;
    }

    /** Build the MediaMine canonical distribution graph. */
    public static FlowNetwork buildChannelGraph() {
        FlowNetwork g = new FlowNetwork(0, 10, 11);

        g.nameNode(0, "Source (Media Server)");
        g.nameNode(1, "CDN 1");
        g.nameNode(2, "CDN 2");
        g.nameNode(3, "CDN 3");
        g.nameNode(4, "Technology Channel");
        g.nameNode(5, "Education Channel");
        g.nameNode(6, "Finance Channel");
        g.nameNode(7, "News Channel");
        g.nameNode(8, "Sports Channel");
        g.nameNode(9, "Science Channel");
        g.nameNode(10, "Sink");

        g.addEdge(0, 1, 500L);
        g.addEdge(0, 2, 400L);
        g.addEdge(0, 3, 300L);

        g.addEdge(1, 4, 250L);
        g.addEdge(1, 5, 200L);

        g.addEdge(2, 6, 200L);
        g.addEdge(2, 7, 150L);

        g.addEdge(3, 8, 180L);
        g.addEdge(3, 9, 100L);

        g.addEdge(4, 10, 250L);
        g.addEdge(5, 10, 200L);
        g.addEdge(6, 10, 200L);
        g.addEdge(7, 10, 150L);
        g.addEdge(8, 10, 180L);
        g.addEdge(9, 10, 100L);

        return g;
    }

    public static boolean smokeTest() {
        FlowNetwork g = buildChannelGraph();
        return g.source == 0 && g.sink == 11
            && g.vertexCount() == 11
            && g.edgeCount() == 15
            && g.nameOf(0).startsWith("Source");
    }
}
