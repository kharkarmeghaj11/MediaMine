package mediamine.m6;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Randomized QuickSort.
 *
 * Algorithm: pick RANDOM pivot from [lo..hi], swap to lo, Lomuto partition, recurse.
 * Expected time: O(n log n). Worst-case O(n²) — extremely unlikely.
 * Space: O(log n) recursion depth expected.
 */
public class RandomizedQuickSort {

    private static final Random RNG = new Random(42L);

    public static <T> void sort(List<T> arr, Comparator<? super T> cmp) {
        sort(arr, cmp, 0, arr.size() - 1);
    }

    public static <T extends Comparable<? super T>> void sort(List<T> arr) {
        sort(arr, Comparator.naturalOrder());
    }

    public static void sort(long[] arr) { sort(arr, 0, arr.length - 1); }
    public static void sort(int[] arr)  { sort(arr, 0, arr.length - 1); }

    private static <T> void sort(List<T> a, Comparator<? super T> cmp, int lo, int hi) {
        if (lo >= hi) return;
        int piv = lo + RNG.nextInt(hi - lo + 1);
        swap(a, lo, piv);
        T pivotVal = a.get(lo);
        int i = lo;
        for (int j = lo + 1; j <= hi; j++) {
            if (cmp.compare(a.get(j), pivotVal) < 0) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, lo, i);
        sort(a, cmp, lo, i - 1);
        sort(a, cmp, i + 1, hi);
    }

    private static void sort(long[] a, int lo, int hi) {
        if (lo >= hi) return;
        int piv = lo + RNG.nextInt(hi - lo + 1);
        swap(a, lo, piv);
        long pv = a[lo];
        int i = lo;
        for (int j = lo + 1; j <= hi; j++) {
            if (a[j] < pv) { i++; swap(a, i, j); }
        }
        swap(a, lo, i);
        sort(a, lo, i - 1);
        sort(a, i + 1, hi);
    }

    private static void sort(int[] a, int lo, int hi) {
        if (lo >= hi) return;
        int piv = lo + RNG.nextInt(hi - lo + 1);
        swap(a, lo, piv);
        int pv = a[lo];
        int i = lo;
        for (int j = lo + 1; j <= hi; j++) {
            if (a[j] < pv) { i++; swap(a, i, j); }
        }
        swap(a, lo, i);
        sort(a, lo, i - 1);
        sort(a, i + 1, hi);
    }

    private static <T> void swap(List<T> a, int i, int j) {
        T tmp = a.get(i); a.set(i, a.get(j)); a.set(j, tmp);
    }
    private static void swap(long[] a, int i, int j) {
        long t = a[i]; a[i] = a[j]; a[j] = t;
    }
    private static void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }

    public static boolean smokeTest() {
        long[] arr = new long[10000];
        Random r = new Random(1L);
        for (int i = 0; i < arr.length; i++) arr[i] = r.nextLong();
        sort(arr);
        for (int i = 1; i < arr.length; i++) if (arr[i] < arr[i-1]) return false;
        return true;
    }
}
