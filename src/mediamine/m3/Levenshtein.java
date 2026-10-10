package mediamine.m3;

import java.util.ArrayList;
import java.util.List;

public class Levenshtein {

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
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                                  Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[n][m];
    }

    public String suggestCorrection(String query, List<String> dictionary) {
        if (dictionary == null || dictionary.isEmpty()) return query;
        String best = dictionary.get(0);
        int minDist = Integer.MAX_VALUE;
        for (String word : dictionary) {
            int d = distance(query, word);
            if (d < minDist) {
                minDist = d;
                best = word;
            }
        }
        return best;
    }

    public CorrectionResult correctQuery(String query, List<String> knownTerms) {
        String[] parts = query.split("\\s+");
        List<String> corrected = new ArrayList<>();
        int totalDist = 0;
        for (String part : parts) {
            String sug = suggestCorrection(part, knownTerms);
            corrected.add(sug);
            totalDist += distance(part, sug);
        }
        String correctedQuery = String.join(" ", corrected);
        return new CorrectionResult(query, correctedQuery, totalDist);
    }

    public static class CorrectionResult {
        public final String original;
        public final String suggested;
        public final int editDistance;

        public CorrectionResult(String o, String s, int d) {
            this.original = o;
            this.suggested = s;
            this.editDistance = d;
        }
    }

    public String getTimeComplexity() {
        return "O(n * m), where n, m = lengths of the two strings";
    }

    public String getSpaceComplexity() {
        return "O(n * m) for DP table";
    }

    public String getExplanation() {
        return "Levenshtein Distance counts the minimum single-character edits (insertion, deletion, substitution) " +
               "needed to change one string into another. It is the foundation for query auto-correction and fuzzy search.";
    }
}
