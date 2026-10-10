package mediamine.m6;

import java.util.Arrays;

/**
 * Blelloch Scan — Parallel Exclusive Prefix Sum.
 *
 * Algorithm: two passes over the array viewed as a binary tree.
 *   Pass 1 (Up-Sweep / Reduce):   for each parent node: val = left + right.
 *   Pass 2 (Down-Sweep / Distribute): put 0 at root, then
 *     child.left  = parent.val
 *     child.right = parent.val + left.child.before
 * Finally swap root 0 and add last element for inclusive scan convenience.
 *
 * Requires array length to be a POWER OF 2. We pad with zeros and truncate.
 *
 * Complexity: WORK O(n). SPAN O(log n).
 */
public class BlellochScan {

    /** Exclusive scan: result[i] = sum of arr[0..i-1].
     *  Result has same length as arr.
     */
    public long[] exclusiveScan(long[] arr) {
        int n = arr.length;
        int N = nextPow2(n);
        long[] x = Arrays.copyOf(arr, N);

        // up-sweep
        for (int d = 1; d < N; d <<= 1) {
            for (int k = 0; k < N; k += 2 * d) {
                x[k + 2 * d - 1] += x[k + d - 1];
            }
        }
        x[N - 1] = 0L;
        // down-sweep
        for (int d = N / 2; d >= 1; d >>= 1) {
            for (int k = 0; k < N; k += 2 * d) {
                long t = x[k + d - 1];
                x[k + d - 1] = x[k + 2 * d - 1];
                x[k + 2 * d - 1] = t + x[k + 2 * d - 1];
            }
        }
        return Arrays.copyOf(x, n);
    }

    /** Inclusive scan: result[i] = sum arr[0..i]. */
    public long[] inclusiveScan(long[] arr) {
        long[] exc = exclusiveScan(arr);
        long[] inc = new long[arr.length];
        for (int i = 0; i < arr.length; i++)
            inc[i] = exc[i] + arr[i];
        return inc;
    }

    private static int nextPow2(int n) {
        int p = 1;
        while (p < n) p <<= 1;
        return p;
    }

    public static boolean smokeTest() {
        // Spec canonical input: [100, 150, 200, 250, 300]
        long[] arr = {100, 150, 200, 250, 300};
        BlellochScan b = new BlellochScan();
        long[] inc = b.inclusiveScan(arr);
        long[] expected = {100, 250, 450, 700, 1000};
        return Arrays.equals(inc, expected);
    }
}
