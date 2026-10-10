package mediamine.model;

/** A content-compatibility constraint (boils down to a SAT clause internally). */
public class ContentConstraint {

    public enum Kind {
        MUST_SELECT,      // var is required (clause: +var)
        CANNOT_BOTH_BE_TRUE, // !A OR !B
        REQUIRES,         // A REQUIRES B  =>  !A OR B
        INCOMPATIBLE      // A and B are incompatible: !A OR !B
    }

    public final Kind kind;
    public final String a;
    public final String b; // can be null for MUST_SELECT

    public ContentConstraint(Kind k, String a, String b) { this.kind = k; this.a = a; this.b = b; }
}
