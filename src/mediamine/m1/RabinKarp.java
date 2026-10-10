package mediamine.m1;

import java.util.ArrayList;
import java.util.List;

public class RabinKarp {

    private static final int PRIME = 101;
    private static final int BASE = 256;

    public List<Integer> search(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        if (pattern.isEmpty() || text.length() < pattern.length()) {
            return positions;
        }
        String t = text.toLowerCase();
        String p = pattern.toLowerCase();
        int n = t.length();
        int m = p.length();

        long patternHash = 0;
        long textHash = 0;
        long h = 1;

        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % PRIME;
        }

        for (int i = 0; i < m; i++) {
            patternHash = (BASE * patternHash + p.charAt(i)) % PRIME;
            textHash = (BASE * textHash + t.charAt(i)) % PRIME;
        }

        for (int i = 0; i <= n - m; i++) {
            if (patternHash == textHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (t.charAt(i + j) != p.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    positions.add(i);
                }
            }
            if (i < n - m) {
                textHash = (BASE * (textHash - t.charAt(i) * h) + t.charAt(i + m)) % PRIME;
                if (textHash < 0) {
                    textHash += PRIME;
                }
            }
        }
        return positions;
    }

    public List<String> searchIds(List<String> ids, String prefix) {
        List<String> result = new ArrayList<>();
        String p = prefix.toLowerCase();
        for (String id : ids) {
            if (id.toLowerCase().startsWith(p)) {
                result.add(id);
            }
        }
        return result;
    }

    public String getTimeComplexity() {
        return "O(n + m) average, O(n * m) worst case";
    }

    public String getSpaceComplexity() {
        return "O(1) additional space";
    }

    public String getExplanation() {
        return "Rabin-Karp uses rolling hash to compare patterns efficiently. It computes hash values for " +
               "the pattern and all windows of the text. When hashes match, it does a full verification to " +
               "avoid false positives from hash collisions.";
    }
}
