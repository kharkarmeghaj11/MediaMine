package mediamine.m5;

/**
 * Boolean literal: a variable, optionally negated.
 * Examples: Literal("T", false) means "Technology is selected".
 *           Literal("T", true)  means "!T" -> Technology is NOT selected.
 */
public class Literal {
    public final String var;
    public final boolean negated;

    public Literal(String var, boolean negated) {
        this.var = var;
        this.negated = negated;
    }

    public static Literal pos(String v) { return new Literal(v, false); }
    public static Literal neg(String v) { return new Literal(v, true); }

    public Literal negate() { return new Literal(var, !negated); }

    public boolean evaluate(java.util.Map<String, Boolean> assignment) {
        Boolean v = assignment.get(var);
        if (v == null) return false;
        return negated ? !v : v;
    }

    @Override public String toString() {
        return negated ? "!" + var : var;
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof Literal)) return false;
        Literal x = (Literal) o;
        return negated == x.negated && var.equals(x.var);
    }
    @Override public int hashCode() { return var.hashCode() * 31 + (negated ? 1 : 0); }

    public static boolean smokeTest() {
        Literal p = Literal.pos("T");
        Literal n = Literal.neg("T");
        java.util.Map<String, Boolean> a = new java.util.HashMap<>();
        a.put("T", true);
        return p.evaluate(a) && !n.evaluate(a);
    }
}
