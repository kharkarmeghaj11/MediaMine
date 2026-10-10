package mediamine.m2;

import java.util.ArrayList;
import java.util.List;

public class SuffixArray {

    private static class Suffix implements Comparable<Suffix> {
        int index;
        String suffix;
        Suffix(int index, String suffix) {
            this.index = index;
            this.suffix = suffix;
        }
        @Override
        public int compareTo(Suffix other) {
            return this.suffix.compareTo(other.suffix);
        }
    }

    public int[] buildSuffixArray(String s) {
        int n = s.length();
        List<Suffix> suffixes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            suffixes.add(new Suffix(i, s.substring(i)));
        }
        suffixes.sort(null);
        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = suffixes.get(i).index;
        }
        return sa;
    }

    public int[] buildRankArray(int[] sa) {
        int n = sa.length;
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }
        return rank;
    }

    public String[] getSuffixes(String s) {
        int n = s.length();
        String[] result = new String[n];
        for (int i = 0; i < n; i++) {
            result[i] = s.substring(i);
        }
        return result;
    }

    public String[] getSortedSuffixes(String s, int[] sa) {
        int n = s.length();
        String[] result = new String[n];
        for (int i = 0; i < n; i++) {
            result[i] = s.substring(sa[i]);
        }
        return result;
    }

    public boolean containsSubstring(String s, int[] sa, String pattern) {
        int n = s.length();
        int m = pattern.length();
        int low = 0, high = n - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            String suf = s.substring(sa[mid], Math.min(sa[mid] + m, n));
            int cmp = suf.compareTo(pattern);
            if (cmp == 0) return true;
            if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }
        return false;
    }

    public List<Integer> findOccurrences(String s, int[] sa, String pattern) {
        List<Integer> positions = new ArrayList<>();
        int n = s.length();
        int m = pattern.length();
        for (int i = 0; i < n; i++) {
            int start = sa[i];
            if (start + m <= n && s.substring(start, start + m).equals(pattern)) {
                positions.add(start);
            }
        }
        return positions;
    }

    public String getTimeComplexity() {
        return "O(n^2 log n) for naive construction; O(n) for advanced (SA-IS)";
    }

    public String getSpaceComplexity() {
        return "O(n) for suffix and rank arrays";
    }

    public String getExplanation() {
        return "Suffix Array is a sorted array of all suffixes of a string. Each entry contains the starting " +
               "index of a suffix. It enables efficient substring searches and is the foundation of many string algorithms.";
    }
}
