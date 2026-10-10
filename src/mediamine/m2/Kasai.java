package mediamine.m2;

public class Kasai {

    public int[] buildLCPArray(String s, int[] sa) {
        int n = s.length();
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }
        int[] lcp = new int[n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] == n - 1) {
                k = 0;
                continue;
            }
            int j = sa[rank[i] + 1];
            while (i + k < n && j + k < n && s.charAt(i + k) == s.charAt(j + k)) {
                k++;
            }
            lcp[rank[i]] = k;
            if (k > 0) k--;
        }
        lcp[n - 1] = 0;
        return lcp;
    }

    public KasaiResult analyze(String s) {
        SuffixArray saObj = new SuffixArray();
        int[] sa = saObj.buildSuffixArray(s);
        int[] lcp = buildLCPArray(s, sa);
        int maxLcp = 0;
        int maxIdx = 0;
        for (int i = 0; i < lcp.length - 1; i++) {
            if (lcp[i] > maxLcp) {
                maxLcp = lcp[i];
                maxIdx = i;
            }
        }
        String repeatedSubstring = "";
        if (maxLcp > 0) {
            repeatedSubstring = s.substring(sa[maxIdx], sa[maxIdx] + maxLcp);
        }
        return new KasaiResult(s, sa, lcp, maxLcp, repeatedSubstring);
    }

    public static class KasaiResult {
        public final String original;
        public final int[] suffixArray;
        public final int[] lcpArray;
        public final int maxLcpValue;
        public final String longestRepeatedSubstring;

        public KasaiResult(String s, int[] sa, int[] lcp, int maxLcp, String repeat) {
            this.original = s;
            this.suffixArray = sa;
            this.lcpArray = lcp;
            this.maxLcpValue = maxLcp;
            this.longestRepeatedSubstring = repeat;
        }
    }

    public String getTimeComplexity() {
        return "O(n), where n = string length";
    }

    public String getSpaceComplexity() {
        return "O(n) for SA, rank, and LCP arrays";
    }

    public String getExplanation() {
        return "Kasai algorithm builds the LCP (Longest Common Prefix) array from a Suffix Array in linear time. " +
               "The LCP array stores the length of the longest common prefix between consecutive suffixes in the suffix array. " +
               "It helps find the longest repeated substring.";
    }
}
