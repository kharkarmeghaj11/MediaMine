package mediamine.m3;

public class MatrixChain {

    public MatrixChainResult optimize(int[] dimensions, String[] operationNames) {
        int n = dimensions.length - 1;
        int[][] dp = new int[n][n];
        int[][] bracket = new int[n][n];

        for (int len = 2; len <= n; len++) {
            for (int i = 0; i < n - len + 1; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j]
                             + dimensions[i] * dimensions[k + 1] * dimensions[j + 1];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        bracket[i][j] = k;
                    }
                }
            }
        }

        String order = buildOrder(bracket, 0, n - 1, operationNames);
        return new MatrixChainResult(dimensions, operationNames,
                                     dp[0][n - 1], order, dp, bracket);
    }

    private String buildOrder(int[][] bracket, int i, int j, String[] names) {
        if (i == j) return names[i];
        int k = bracket[i][j];
        return "(" + buildOrder(bracket, i, k, names)
             + " x " + buildOrder(bracket, k + 1, j, names) + ")";
    }

    public static class MatrixChainResult {
        public final int[] dimensions;
        public final String[] operationNames;
        public final int minOperations;
        public final String optimalOrder;
        public final int[][] dpTable;
        public final int[][] bracketTable;

        public MatrixChainResult(int[] dims, String[] names, int min,
                                 String order, int[][] dp, int[][] br) {
            this.dimensions = dims;
            this.operationNames = names;
            this.minOperations = min;
            this.optimalOrder = order;
            this.dpTable = dp;
            this.bracketTable = br;
        }
    }

    public String getTimeComplexity() {
        return "O(n^3), where n = number of matrices";
    }

    public String getSpaceComplexity() {
        return "O(n^2) for DP and bracket tables";
    }

    public String getExplanation() {
        return "Matrix Chain Multiplication finds the optimal parenthesization for multiplying a chain of " +
               "matrices so that the total number of scalar operations is minimized. Applied here to order " +
               "media-processing operations (decode → generate → search → compare) efficiently.";
    }
}
