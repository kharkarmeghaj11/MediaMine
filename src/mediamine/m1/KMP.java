package mediamine.m1;

import java.util.ArrayList;
import java.util.List;

public class KMP {

    public int[] buildLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public List<Integer> search(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        if (pattern.isEmpty() || text.length() < pattern.length()) {
            return positions;
        }
        String t = text.toLowerCase();
        String p = pattern.toLowerCase();
        int[] lps = buildLPS(p);
        int n = t.length();
        int m = p.length();
        int i = 0;
        int j = 0;
        while (i < n) {
            if (t.charAt(i) == p.charAt(j)) {
                i++;
                j++;
            }
            if (j == m) {
                positions.add(i - j);
                j = lps[j - 1];
            } else if (i < n && t.charAt(i) != p.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return positions;
    }

    public String getTimeComplexity() {
        return "O(n + m), where n = text length, m = pattern length";
    }

    public String getSpaceComplexity() {
        return "O(m) for the LPS array";
    }

    public String getExplanation() {
        return "KMP (Knuth-Morris-Pratt) is an efficient pattern matching algorithm. It preprocesses the pattern " +
               "to create an LPS (Longest Prefix Suffix) array. This array helps skip unnecessary comparisons " +
               "by using information about repeating prefixes in the pattern.";
    }
}
