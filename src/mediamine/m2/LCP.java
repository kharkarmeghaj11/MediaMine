package mediamine.m2;

public class LCP {

    public int longestCommonPrefixLength(String a, String b) {
        int minLen = Math.min(a.length(), b.length());
        for (int i = 0; i < minLen; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return i;
            }
        }
        return minLen;
    }

    public String longestCommonPrefix(String a, String b) {
        int len = longestCommonPrefixLength(a.toLowerCase(), b.toLowerCase());
        if (len == 0) return "";
        return a.substring(0, len);
    }

    public LCResult compareTranscripts(String t1, String title1, String t2, String title2) {
        String common = longestCommonPrefix(t1, t2);
        int length = common.length();
        double similarity = 0.0;
        int totalLen = t1.length() + t2.length();
        if (totalLen > 0) {
            similarity = (2.0 * length) / totalLen * 100;
        }
        return new LCResult(title1, title2, common, length, similarity);
    }

    public static class LCResult {
        public final String title1;
        public final String title2;
        public final String commonPrefix;
        public final int length;
        public final double similarityPercent;

        public LCResult(String t1, String t2, String prefix, int len, double sim) {
            this.title1 = t1;
            this.title2 = t2;
            this.commonPrefix = prefix;
            this.length = len;
            this.similarityPercent = sim;
        }
    }

    public String getTimeComplexity() {
        return "O(min(n, m)) for pairwise LCP";
    }

    public String getSpaceComplexity() {
        return "O(1) additional space";
    }

    public String getExplanation() {
        return "LCP (Longest Common Prefix) finds the longest string that is a prefix of two input strings. " +
               "It is used to compare transcript similarity and detect how two media contents start similarly.";
    }
}
