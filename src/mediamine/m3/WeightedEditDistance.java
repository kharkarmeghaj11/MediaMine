package mediamine.m3;

public class WeightedEditDistance {

    public static final int DEFAULT_INSERT_COST = 1;
    public static final int DEFAULT_DELETE_COST = 1;
    public static final int DEFAULT_SUBSTITUTE_COST = 2;

    private int insertCost;
    private int deleteCost;
    private int substituteCost;

    public WeightedEditDistance() {
        this(DEFAULT_INSERT_COST, DEFAULT_DELETE_COST, DEFAULT_SUBSTITUTE_COST);
    }

    public WeightedEditDistance(int insertCost, int deleteCost, int substituteCost) {
        this.insertCost = insertCost;
        this.deleteCost = deleteCost;
        this.substituteCost = substituteCost;
    }

    public int distance(String a, String b) {
        String s1 = a.toLowerCase();
        String s2 = b.toLowerCase();
        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i * deleteCost;
        for (int j = 0; j <= m; j++) dp[0][j] = j * insertCost;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j] + deleteCost,
                               Math.min(dp[i][j - 1] + insertCost,
                                        dp[i - 1][j - 1] + substituteCost));
                }
            }
        }
        return dp[n][m];
    }

    public WeightedResult correctTranscript(String original, String corrected) {
        int cost = distance(original, corrected);
        return new WeightedResult(original, corrected, cost,
                                  insertCost, deleteCost, substituteCost);
    }

    public static class WeightedResult {
        public final String original;
        public final String corrected;
        public final int totalCost;
        public final int insertCost;
        public final int deleteCost;
        public final int substituteCost;

        public WeightedResult(String o, String c, int t, int ic, int dc, int sc) {
            this.original = o;
            this.corrected = c;
            this.totalCost = t;
            this.insertCost = ic;
            this.deleteCost = dc;
            this.substituteCost = sc;
        }
    }

    public int getInsertCost() { return insertCost; }
    public int getDeleteCost() { return deleteCost; }
    public int getSubstituteCost() { return substituteCost; }

    public String getTimeComplexity() {
        return "O(n * m), where n, m = lengths of the two strings";
    }

    public String getSpaceComplexity() {
        return "O(n * m) for DP table";
    }

    public String getExplanation() {
        return "Weighted Edit Distance assigns different costs to insertion, deletion, and substitution. " +
               "This is useful when some operations are more expensive than others (e.g., in transcript error fixing, " +
               "substituting a word might be costlier than inserting one).";
    }
}
