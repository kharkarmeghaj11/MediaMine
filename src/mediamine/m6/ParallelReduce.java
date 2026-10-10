package mediamine.m6;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Parallel Reduce using ForkJoinPool + RecursiveTask<Long>.
 * Balanced binary-tree split at threshold = SEQUENTIAL_THRESHOLD = 512.
 *
 * Time (work): O(n). Span: O(log n / log threshold).
 * Actual parallelism depends on JVM ForkJoinPool parallelism (common pool default = cores).
 *
 * We implement sum, max, min as three separate tasks.
 */
public class ParallelReduce {

    public static final int SEQUENTIAL_THRESHOLD = 512;
    private static final ForkJoinPool POOL = ForkJoinPool.commonPool();

    public static long sum(long[] arr) {
        if (arr.length == 0) return 0L;
        return POOL.invoke(new SumTask(arr, 0, arr.length));
    }

    public static long max(long[] arr) {
        if (arr.length == 0) return 0L;
        return POOL.invoke(new MaxTask(arr, 0, arr.length));
    }

    public static long min(long[] arr) {
        if (arr.length == 0) return 0L;
        return POOL.invoke(new MinTask(arr, 0, arr.length));
    }

    private static final class SumTask extends RecursiveTask<Long> {
        private static final long serialVersionUID = 1L;
        final long[] arr; final int lo; final int hi;
        SumTask(long[] a, int l, int h) { arr = a; lo = l; hi = h; }
        @Override protected Long compute() {
            if (hi - lo <= SEQUENTIAL_THRESHOLD) {
                long s = 0L;
                for (int i = lo; i < hi; i++) s += arr[i];
                return s;
            }
            int mid = (lo + hi) >>> 1;
            SumTask left = new SumTask(arr, lo, mid);
            SumTask right = new SumTask(arr, mid, hi);
            left.fork();
            long r = right.compute();
            return left.join() + r;
        }
    }

    private static final class MaxTask extends RecursiveTask<Long> {
        private static final long serialVersionUID = 1L;
        final long[] arr; final int lo; final int hi;
        MaxTask(long[] a, int l, int h) { arr = a; lo = l; hi = h; }
        @Override protected Long compute() {
            if (hi - lo <= SEQUENTIAL_THRESHOLD) {
                long m = Long.MIN_VALUE;
                for (int i = lo; i < hi; i++) if (arr[i] > m) m = arr[i];
                return m;
            }
            int mid = (lo + hi) >>> 1;
            MaxTask left = new MaxTask(arr, lo, mid);
            left.fork();
            long r = new MaxTask(arr, mid, hi).compute();
            long l = left.join();
            return Math.max(l, r);
        }
    }

    private static final class MinTask extends RecursiveTask<Long> {
        private static final long serialVersionUID = 1L;
        final long[] arr; final int lo; final int hi;
        MinTask(long[] a, int l, int h) { arr = a; lo = l; hi = h; }
        @Override protected Long compute() {
            if (hi - lo <= SEQUENTIAL_THRESHOLD) {
                long m = Long.MAX_VALUE;
                for (int i = lo; i < hi; i++) if (arr[i] < m) m = arr[i];
                return m;
            }
            int mid = (lo + hi) >>> 1;
            MinTask left = new MinTask(arr, lo, mid);
            left.fork();
            long r = new MinTask(arr, mid, hi).compute();
            long l = left.join();
            return Math.min(l, r);
        }
    }

    public static boolean smokeTest() {
        // sum 1..10000 = 50005000
        long[] a = new long[10000];
        for (int i = 0; i < a.length; i++) a[i] = i + 1;
        return sum(a) == 50005000L && max(a) == 10000L && min(a) == 1L;
    }
}
