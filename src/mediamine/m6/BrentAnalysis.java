package mediamine.m6;

/**
 * Brent's Theorem — Educational performance upper-bound.
 * Not a benchmark: purely theoretical analysis.
 *
 * Formula: T_p ≤ W/P + S
 *   W = total work (sum of all operations)
 *   S = span (critical path length, longest sequential dependency chain)
 *   P = number of processors
 *
 * We also compute speedup = W / T_p, efficiency = speedup / P.
 *
 * Complexity: O(1).
 */
public class BrentAnalysis {

    public final long workW;
    public final long spanS;
    public final int processorsP;
    public final long estimatedTimeTp; // W/P + S (integer division + S)
    public final double speedup;
    public final double efficiency;

    public BrentAnalysis(long W, long S, int P) {
        if (P < 1) P = 1;
        if (S < 0) S = 0;
        this.workW = Math.max(0L, W);
        this.spanS = S;
        this.processorsP = P;
        long wp = this.workW / P;
        this.estimatedTimeTp = wp + S;
        this.speedup = (estimatedTimeTp == 0) ? 1.0 : (1.0 * this.workW / estimatedTimeTp);
        this.efficiency = this.speedup / this.processorsP;
    }

    public String formula() {
        return "T_p = W/P + S = " + workW + "/" + processorsP + " + " + spanS
             + " = " + estimatedTimeTp + " units";
    }

    public static boolean smokeTest() {
        BrentAnalysis b = new BrentAnalysis(10000L, 500L, 4);
        // 10000/4 + 500 = 2500 + 500 = 3000
        return b.estimatedTimeTp == 3000L
            && b.workW == 10000L
            && b.spanS == 500L
            && b.processorsP == 4;
    }
}
