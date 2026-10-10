package mediamine.m4;

/**
 * Edge in a flow network.
 * Stores forward edge (from, to) with given capacity, plus a reverse index so
 * the residual graph can update the back-edge in O(1).
 *
 * Complexity: O(1) per operation (storage + residual lookup).
 * Space complexity per edge: O(1).
 */
public class Edge {
    public final int from;
    public final int to;
    public final long capacity;
    public long flow;
    public int rev;
    Edge reverse;

    public Edge(int from, int to, long capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.flow = 0;
        this.rev = -1;
    }

    public Edge(int from, int to, long capacity, int rev) {
        this(from, to, capacity);
        this.rev = rev;
    }

    public long residualTo(int target) {
        return target == to ? capacity - flow : 0;
    }

    public void addFlowTo(int target, long delta) {
        if (target != to) throw new IllegalArgumentException("Flow must follow the edge direction");
        if (delta < 0 || delta > capacity - flow) {
            throw new IllegalArgumentException("Flow exceeds residual capacity");
        }
        flow += delta;
        if (reverse != null) reverse.flow -= delta;
    }

    public static boolean smokeTest() {
        Edge e = new Edge(0, 1, 10L);
        if (e.residualTo(1) != 10L) return false;
        e.addFlowTo(1, 3L);
        if (e.residualTo(1) != 7L) return false;
        return e.residualTo(0) == 0L;
    }
}
