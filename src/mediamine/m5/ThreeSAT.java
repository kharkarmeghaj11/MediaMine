package mediamine.m5;

import java.util.*;

/**
 * 3-SAT solver: educational brute force over 2^V assignments.
 * Validates that all clauses have at most 3 literals; throws if violated.
 *
 * Complexity: O(2^V · C). Works for V <= 12 demos.
 * Space: O(V + C).
 */
public class ThreeSAT {

    public final List<Clause> clauses;
    public final Set<String> vars;

    public ThreeSAT() { clauses = new ArrayList<>(); vars = new LinkedHashSet<>(); }

    public void addClause(Clause c) {
        if (c.size() > 3) throw new IllegalArgumentException(
            "3-SAT: clause has " + c.size() + " literals (expected <= 3): " + c);
        clauses.add(c);
        for (Literal l : c.literals) vars.add(l.var);
    }

    public int clauseCount() { return clauses.size(); }
    public int varCount() { return vars.size(); }

    public Optional<Map<String, Boolean>> solve() {
        List<String> vl = new ArrayList<>(vars);
        int n = vl.size();
        long total = 1L << n;
        for (long mask = 0; mask < total; mask++) {
            Map<String, Boolean> a = new LinkedHashMap<>();
            for (int i = 0; i < n; i++) a.put(vl.get(i), (mask & (1L << i)) != 0);
            boolean ok = true;
            for (Clause c : clauses) if (!c.evaluate(a)) { ok = false; break; }
            if (ok) return Optional.of(a);
        }
        return Optional.empty();
    }

    /** Canonical spec 3-SAT: (T∨S∨F) ∧ (!T∨E∨Sc) ∧ (!F∨E∨T)
     *  Variables T, Sports(Sports), F(Finance), E(Environment), Sc(Science).
     */
    public static ThreeSAT buildCanonical() {
        ThreeSAT s = new ThreeSAT();
        s.addClause(new Clause().addPos("T").addPos("Sports").addPos("Finance"));
        s.addClause(new Clause().addNeg("T").addPos("Environment").addPos("Science"));
        s.addClause(new Clause().addNeg("Finance").addPos("Environment").addPos("T"));
        return s;
    }

    public static boolean smokeTest() {
        ThreeSAT s = buildCanonical();
        Optional<Map<String, Boolean>> r = s.solve();
        if (!r.isPresent()) return false;
        Map<String, Boolean> a = r.get();
        // check every clause satisfied
        for (Clause c : s.clauses) if (!c.evaluate(a)) return false;
        return s.varCount() == 5 && s.clauseCount() == 3;
    }
}
