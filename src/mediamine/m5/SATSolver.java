package mediamine.m5;

import java.util.*;

/**
 * SAT solver for arbitrary (not necessarily 3-SAT) small Boolean formulas
 * using brute force over 2^V assignments. Educational use only.
 *
 * Complexity: O(2^V · C) where V = #vars, C = #clauses. Fine for V <= 10 demos.
 * Space: O(V + C).
 */
public class SATSolver {
    public final List<Clause> clauses;
    public final Set<String> vars;

    public SATSolver() {
        clauses = new ArrayList<>();
        vars = new LinkedHashSet<>();
    }
    public void addClause(Clause c) {
        clauses.add(c);
        for (Literal l : c.literals) vars.add(l.var);
    }

    /** Build clauses for MediaMine Content Compatibility canonical constraints:
     * 1. Technology must be selected                 (T)
     * 2. Sports and Finance cannot both be selected  (!S OR !F)
     * 3. Science if Environment                      (!E OR Sc)
     * 4. Technology + Politics conflict              (!T OR !P)
     */
    public static SATSolver buildMediaMineConstraints() {
        SATSolver s = new SATSolver();
        s.addClause(new Clause().addPos("T"));                                         // T
        s.addClause(new Clause().addNeg("Sports").addNeg("Finance"));                  // !Sports OR !Finance
        s.addClause(new Clause().addNeg("Environment").addPos("Science"));             // !Environment OR Science
        s.addClause(new Clause().addNeg("T").addNeg("Politics"));                      // !T OR !Politics
        return s;
    }

    public Optional<Map<String, Boolean>> solve() {
        List<String> varList = new ArrayList<>(vars);
        int n = varList.size();
        long total = 1L << n;
        for (long mask = 0; mask < total; mask++) {
            Map<String, Boolean> a = new LinkedHashMap<>();
            for (int i = 0; i < n; i++) a.put(varList.get(i), (mask & (1L << i)) != 0);
            boolean ok = true;
            for (Clause c : clauses) if (!c.evaluate(a)) { ok = false; break; }
            if (ok) return Optional.of(a);
        }
        return Optional.empty();
    }

    public boolean allSatisfied(Map<String, Boolean> assignment) {
        for (Clause c : clauses) if (!c.evaluate(assignment)) return false;
        return true;
    }

    public static boolean smokeTest() {
        SATSolver s = buildMediaMineConstraints();
        Optional<Map<String, Boolean>> r = s.solve();
        if (!r.isPresent()) return false;
        Map<String, Boolean> a = r.get();
        // Verify T=true and Sports+Finance not both true, etc.
        if (!Boolean.TRUE.equals(a.get("T"))) return false;
        if (Boolean.TRUE.equals(a.get("Sports")) && Boolean.TRUE.equals(a.get("Finance"))) return false;
        return true;
    }
}
