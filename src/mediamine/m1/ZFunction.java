package mediamine.m1;

import java.util.ArrayList;
import java.util.List;

public class ZFunction {

    public int[] computeZ(String s) {
        int n = s.length();
        int[] z = new int[n];
        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i <= r) {
                z[i] = Math.min(r - i + 1, z[i - l]);
            }
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        z[0] = n;
        return z;
    }

    public List<RepeatedPhrase> findRepeatedPhrases(String text) {
        List<RepeatedPhrase> result = new ArrayList<>();
        if (text == null || text.isEmpty()) return result;
        String s = text.toLowerCase();
        int[] z = computeZ(s);
        int n = s.length();
        boolean[] counted = new boolean[n];
        for (int len = 3; len <= n / 2; len++) {
            int count = 1;
            int i = len;
            while (i < n) {
                if (z[i] >= len && !counted[i]) {
                    count++;
                    counted[i] = true;
                }
                i += len;
            }
            if (count >= 2) {
                String phrase = s.substring(0, len);
                if (phrase.trim().length() >= 3) {
                    result.add(new RepeatedPhrase(phrase, count));
                }
            }
        }
        return result;
    }

    public RepeatedPhrase findBestRepeatedPhrase(String text) {
        if (text == null || text.isEmpty()) return null;
        String s = text.toLowerCase();
        int[] z = computeZ(s);
        int n = s.length();
        int bestLen = 0;
        int bestCount = 0;
        for (int len = 3; len <= n / 2; len++) {
            int count = 1;
            for (int i = len; i < n; i++) {
                if (z[i] >= len) count++;
            }
            if (count >= 2 && len > bestLen) {
                bestLen = len;
                bestCount = count;
            }
        }
        if (bestLen > 0) {
            return new RepeatedPhrase(s.substring(0, bestLen), bestCount);
        }
        return null;
    }

    public static class RepeatedPhrase {
        public final String phrase;
        public final int occurrences;
        public RepeatedPhrase(String phrase, int occurrences) {
            this.phrase = phrase;
            this.occurrences = occurrences;
        }
    }

    public String getTimeComplexity() {
        return "O(n), where n = string length";
    }

    public String getSpaceComplexity() {
        return "O(n) for the Z-array";
    }

    public String getExplanation() {
        return "Z-Function computes for each position i the length of the longest substring starting at i " +
               "that is also a prefix of the string. It is used to find repeated patterns and for pattern matching.";
    }
}
