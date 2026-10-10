package mediamine.services;

import mediamine.data.SampleData;
import mediamine.m5.*;
import mediamine.m5.SATSolver;
import mediamine.model.ContentConstraint;

import java.util.*;

/**
 * Content Compatibility Service.
 *   - SAT solver for canonical Content Constraints (T, Sports^!Finance, E→Sci, T^!Politics)
 *   - 3-SAT solver
 *   - 3-SAT to CLIQUE reduction
 *   - Independent Set (conflict-free group)
 *   - Vertex Cover + 2-Approx
 */
public class ContentCompatibilityService {

    public static class CompatibilityReport {
        public final List<Clause> clauses;
        public final int clauseCount;
        public final int varCount;
        public final Map<String, Boolean> satAssignment; // null if UNSAT
        public final List<String> constraintsStatus; // each entry "Description" + ✓/✗
        public final SatToClique.ReductionResult cliqueResult;
        public final List<String> independentSet;
        public final int independentSetConflicts;
        public final Set<String> minVertexCover;
        public final int minVertexCoverSize;
        public final VertexCoverApprox.ApproxResult approxCover;
        public final String overallResult;  // VALID / INVALID

        public CompatibilityReport(List<Clause> c, Map<String, Boolean> ass,
                                   List<String> status, SatToClique.ReductionResult cr,
                                   List<String> iset, int isetConflicts,
                                   Set<String> mvc, VertexCoverApprox.ApproxResult apr,
                                   String result) {
            this.clauses = c;
            this.clauseCount = c.size();
            Set<String> vars = new LinkedHashSet<>();
            for (Clause cl : c) for (Literal l : cl.literals) vars.add(l.var);
            this.varCount = vars.size();
            this.satAssignment = ass;
            this.constraintsStatus = status;
            this.cliqueResult = cr;
            this.independentSet = iset;
            this.independentSetConflicts = isetConflicts;
            this.minVertexCover = mvc;
            this.minVertexCoverSize = mvc == null ? 0 : mvc.size();
            this.approxCover = apr;
            this.overallResult = result;
        }
    }

    public CompatibilityReport analyze() {
        // SAT
        SATSolver sat = SATSolver.buildMediaMineConstraints();
        Optional<Map<String, Boolean>> r = sat.solve();
        Map<String, Boolean> assignment = r.orElse(null);

        // human-readable constraint checks against assignment (if SAT)
        List<String> status = new ArrayList<>();
        List<ContentConstraint> ccList = SampleData.getContentConstraints();
        for (ContentConstraint c : ccList) {
            String desc = describe(c);
            boolean ok = true;
            if (assignment != null) {
                switch (c.kind) {
                    case MUST_SELECT: ok = Boolean.TRUE.equals(assignment.get(variableFor(c.a))); break;
                    case CANNOT_BOTH_BE_TRUE:
                        ok = !(Boolean.TRUE.equals(assignment.get(variableFor(c.a)))
                            && Boolean.TRUE.equals(assignment.get(variableFor(c.b))));
                        break;
                    case REQUIRES:
                        ok = !(Boolean.TRUE.equals(assignment.get(variableFor(c.a)))
                            && !Boolean.TRUE.equals(assignment.get(variableFor(c.b))));
                        break;
                    case INCOMPATIBLE:
                        ok = !(Boolean.TRUE.equals(assignment.get(variableFor(c.a)))
                            && Boolean.TRUE.equals(assignment.get(variableFor(c.b))));
                        break;
                }
            }
            status.add(desc + "   " + (ok ? "PASS" : "FAIL"));
        }

        // 3-SAT -> CLIQUE
        ThreeSAT tsat = ThreeSAT.buildCanonical();
        SatToClique s2c = new SatToClique();
        SatToClique.ReductionResult cr = s2c.reduce(tsat.clauses);

        // Independent Set on compatibility conflict graph (edges = conflict between topic vars)
        IndependentSet is = buildConflictGraphIS();
        List<String> iset = is.findMaxIndependentSet();
        int isConflicts = is.countEdgesWithin(iset);

        // Vertex Cover on same conflict graph
        VertexCover vc = buildConflictGraphVC();
        Set<String> mvc = vc.findMinCover();
        VertexCoverApprox ap = new VertexCoverApprox();
        VertexCoverApprox.ApproxResult apr = ap.solve(vc);

        String overall = (assignment != null) ? "VALID CONTENT SELECTION" : "UNSATISFIABLE";
        return new CompatibilityReport(sat.clauses, assignment, status, cr, iset, isConflicts, mvc, apr, overall);
    }

    private IndependentSet buildConflictGraphIS() {
        IndependentSet g = new IndependentSet();
        // conflicts from spec: (Technology↔Politics), (Sports↔Finance), (Finance↔Environment)
        g.addNode("Technology");
        g.addNode("Politics");
        g.addNode("Sports");
        g.addNode("Finance");
        g.addNode("Environment");
        g.addNode("Science");
        g.addEdge("Technology", "Politics");
        g.addEdge("Sports", "Finance");
        g.addEdge("Finance", "Environment");
        return g;
    }

    private VertexCover buildConflictGraphVC() {
        VertexCover g = new VertexCover();
        g.addEdge("Technology", "Politics");
        g.addEdge("Sports", "Finance");
        g.addEdge("Finance", "Environment");
        return g;
    }

    private String describe(ContentConstraint c) {
        switch (c.kind) {
            case MUST_SELECT:          return c.a + " must be selected";
            case CANNOT_BOTH_BE_TRUE:  return c.a + " + " + c.b + " conflict";
            case REQUIRES:             return c.a + " requires " + c.b;
            case INCOMPATIBLE:         return c.a + " + " + c.b + " incompatible";
        }
        return c.a + " ? " + c.b;
    }

    private String variableFor(String category) {
        return "Technology".equals(category) ? "T" : category;
    }
}
