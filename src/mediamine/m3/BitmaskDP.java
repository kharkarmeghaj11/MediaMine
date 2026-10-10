package mediamine.m3;

import java.util.ArrayList;
import java.util.List;

public class BitmaskDP {

    public SelectionResult selectCategories(String[] categoryNames, int[] scores, int maxCategories) {
        int n = categoryNames.length;
        int bestMask = 0;
        int bestScore = 0;

        for (int mask = 0; mask < (1 << n); mask++) {
            int bitCount = Integer.bitCount(mask);
            if (bitCount <= maxCategories && bitCount > 0) {
                int total = 0;
                for (int i = 0; i < n; i++) {
                    if ((mask & (1 << i)) != 0) {
                        total += scores[i];
                    }
                }
                if (total > bestScore) {
                    bestScore = total;
                    bestMask = mask;
                }
            }
        }

        List<String> selected = new ArrayList<>();
        List<Integer> selectedScores = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if ((bestMask & (1 << i)) != 0) {
                selected.add(categoryNames[i]);
                selectedScores.add(scores[i]);
            }
        }

        return new SelectionResult(categoryNames, scores, maxCategories,
                                   selected, selectedScores, bestScore, bestMask);
    }

    public static class SelectionResult {
        public final String[] allCategories;
        public final int[] allScores;
        public final int maxAllowed;
        public final List<String> selectedCategories;
        public final List<Integer> selectedScores;
        public final int totalScore;
        public final int bitmask;

        public SelectionResult(String[] cats, int[] sc, int max,
                               List<String> sel, List<Integer> ssc,
                               int total, int mask) {
            this.allCategories = cats;
            this.allScores = sc;
            this.maxAllowed = max;
            this.selectedCategories = sel;
            this.selectedScores = ssc;
            this.totalScore = total;
            this.bitmask = mask;
        }

        public String bitmaskAsBinary() {
            return Integer.toBinaryString(bitmask);
        }
    }

    public String getTimeComplexity() {
        return "O(2^n * n), where n = number of items (categories)";
    }

    public String getSpaceComplexity() {
        return "O(1) additional space";
    }

    public String getExplanation() {
        return "Bitmask DP uses a bitmask (integer) where each bit represents whether an item is selected. " +
               "By iterating over all 2^n possible subsets, it finds the best combination under a constraint " +
               "(e.g., select at most 3 categories with maximum total score).";
    }
}
