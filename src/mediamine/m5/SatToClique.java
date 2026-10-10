package mediamine.m5;

import java.util.*;

/**
 * 3-SAT to CLIQUE reduction:
 *  - One vertex per literal occurrence (3 literals * C clauses = 3C vertices).
 *  - Add edge between (clause-i, literal-x) and (clause-j, literal-y) when:
 *      (a) clauses i != j, and
 *      (b) literal x is not the negation of literal y (compatible assignment).
 *  - There exists a k-clique (where k = number of clauses) IFF formula is satisfiable.
 *
 * Complexity: O(k²) vertices + O(k^4) edge construction for k clauses. Clique search
 * done with Clique.findCliqueOfSize(k) -> O(3^(C)).
 * Space: O(k^2).
 */
public class SatToClique {

    public static class ReductionResult {
        public final int vertices;
        public final int clauses;
        public final int requiredCliqueSize;
        public final boolean cliqueFound;
        public final List<String> cliqueLiterals; // non-empty only if found
        public ReductionResult(int v, int c, int k, boolean f, List<String> lits) {
            vertices = v; clauses = c; requiredCliqueSize = k;
            cliqueFound = f; cliqueLiterals = lits;
        }
    }

    public ReductionResult reduce(List<Clause> clauses) {
        int k = clauses.size();
        // Vertex id = clauseIdx*3 + literalIdxInClause (clause padded with dummies to 3)
        Clique g = new Clique();
        List<Literal> vertexLiteral = new ArrayList<>();
        List<Integer> vertexClause = new ArrayList<>();

        // Add vertices
        int vCount = 0;
        for (int ci = 0; ci < clauses.size(); ci++) {
            Clause c = clauses.get(ci);
            for (int li = 0; li < 3; li++) {
                Literal l = (li < c.size()) ? c.literals.get(li) : null;
                vertexLiteral.add(l);
                vertexClause.add(ci);
                String name = "c" + ci + "_L" + li + (l == null ? "_NULL" : "(" + l + ")");
                g.addNode(name);
                vCount++;
            }
        }

        // Add edges
        for (int a = 0; a < vCount; a++) {
            if (vertexLiteral.get(a) == null) continue;
            for (int b = a + 1; b < vCount; b++) {
                if (vertexLiteral.get(b) == null) continue;
                if (vertexClause.get(a).intValue() == vertexClause.get(b).intValue()) continue;
                Literal la = vertexLiteral.get(a), lb = vertexLiteral.get(b);
                if (la.var.equals(lb.var) && la.negated != lb.negated) continue; // conflicting
                // else compatible: add edge
                String na = "c" + vertexClause.get(a) + "_L" + clauseLitIndex(clauses, vertexClause.get(a), la)
                    + "(" + la + ")";
                String nb = "c" + vertexClause.get(b) + "_L" + clauseLitIndex(clauses, vertexClause.get(b), lb)
                    + "(" + lb + ")";
                g.addEdge(na, nb);
            }
        }

        Optional<List<String>> clique = g.findCliqueOfSize(k);
        List<String> lits = new ArrayList<>();
        if (clique.isPresent()) for (String vv : clique.get()) {
            int open = vv.indexOf('('); int close = vv.indexOf(')');
            if (open >= 0 && close > open) lits.add(vv.substring(open + 1, close));
        }
        return new ReductionResult(vCount, k, k, clique.isPresent(), lits);
    }

    private int clauseLitIndex(List<Clause> clauses, int ci, Literal l) {
        Clause c = clauses.get(ci);
        for (int i = 0; i < c.size(); i++) if (c.literals.get(i).equals(l)) return i;
        return 0;
    }

    public static boolean smokeTest() {
        ThreeSAT s = ThreeSAT.buildCanonical();
        SatToClique sc = new SatToClique();
        ReductionResult r = sc.reduce(s.clauses);
        return r.clauses == 3 && r.requiredCliqueSize == 3
            && r.cliqueFound; // formula is satisfiable, so clique size 3 exists
    }
}
