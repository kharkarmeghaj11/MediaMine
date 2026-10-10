package mediamine.m3;

public class DamerauLevenshtein {

    public int distance(String a, String b) {
        String s1 = a.toLowerCase();
        String s2 = b.toLowerCase();
        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(dp[i - 1][j] + 1,
                           Math.min(dp[i][j - 1] + 1,
                                    dp[i - 1][j - 1] + cost));
                if (i > 1 && j > 1
                    && s1.charAt(i - 1) == s2.charAt(j - 2)
                    && s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + cost);
                }
            }
        }
        return dp[n][m];
    }

    public DamerauResult analyze(String typo, String correct) {
        int d = distance(typo, correct);
        String operation = "Insertion/Deletion/Substitution";
        if (typo.length() == correct.length()) {
            if (d == 1 && isTransposition(typo.toLowerCase(), correct.toLowerCase())) {
                operation = "Transposition (adjacent swap)";
            } else if (d == 1) {
                operation = "Substitution";
            }
        } else if (correct.length() == typo.length() + 1) {
            operation = "Insertion";
        } else if (typo.length() == correct.length() + 1) {
            operation = "Deletion";
        }
        return new DamerauResult(typo, correct, d, operation);
    }

    private boolean isTransposition(String a, String b) {
        int diff = -1;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                if (diff == -1) diff = i;
                else {
                    return (diff == i - 1
                            && a.charAt(diff) == b.charAt(i)
                            && a.charAt(i) == b.charAt(diff));
                }
            }
        }
        return false;
    }

    public static class DamerauResult {
        public final String typo;
        public final String correct;
        public final int distance;
        public final String operation;

        public DamerauResult(String t, String c, int d, String op) {
            this.typo = t;
            this.correct = c;
            this.distance = d;
            this.operation = op;
        }
    }

    public String getTimeComplexity() {
        return "O(n * m), where n, m = lengths of the two strings";
    }

    public String getSpaceComplexity() {
        return "O(n * m) for DP table";
    }

    public String getExplanation() {
        return "Damerau-Levenshtein extends Levenshtein distance by adding transposition (swapping two adjacent " +
               "characters) as a valid operation. It better handles common typing mistakes like 'teh' → 'the'.";
    }
}
