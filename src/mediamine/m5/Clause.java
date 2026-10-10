package mediamine.m5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Boolean clause = a disjunction (OR) of literals.
 * A clause is SATISFIED if at least one literal evaluates true.
 */
public class Clause {
    public final List<Literal> literals;

    public Clause() { this.literals = new ArrayList<>(); }
    public Clause(List<Literal> ls) { this.literals = new ArrayList<>(ls); }

    public Clause add(Literal l) { literals.add(l); return this; }
    public Clause addPos(String v) { return add(Literal.pos(v)); }
    public Clause addNeg(String v) { return add(Literal.neg(v)); }
    public int size() { return literals.size(); }

    public boolean evaluate(Map<String, Boolean> assignment) {
        if (literals.isEmpty()) return false;
        for (Literal l : literals) if (l.evaluate(assignment)) return true;
        return false;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < literals.size(); i++) {
            if (i > 0) sb.append(" OR ");
            sb.append(literals.get(i));
        }
        sb.append(")");
        return sb.toString();
    }

    public static boolean smokeTest() {
        Clause c = new Clause().addPos("A").addPos("B").addNeg("C");
        Map<String, Boolean> a1 = new java.util.HashMap<>();
        a1.put("A", false); a1.put("B", true); a1.put("C", false);
        Map<String, Boolean> a2 = new java.util.HashMap<>();
        a2.put("A", false); a2.put("B", false); a2.put("C", true);
        return c.evaluate(a1) && !c.evaluate(a2);
    }
}
