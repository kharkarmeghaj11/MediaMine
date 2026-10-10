package mediamine.m6;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Reservoir Sampling — Algorithm R.
 * Maintains a uniform random sample of size k from a stream of unknown length.
 *
 * Time: O(n) — every element processed exactly once.
 * Space: O(k) — reservoir of size k only.
 *
 * For i-th element (1-indexed): with probability k/i replace a random reservoir slot.
 */
public class ReservoirSampling<T> {

    private final Random rng;
    private final int k;
    private final ArrayList<T> reservoir;
    private long count = 0;

    public ReservoirSampling(int k) { this(k, new Random(12345L)); }
    public ReservoirSampling(int k, Random seed) {
        this.k = k;
        this.reservoir = new ArrayList<>(k);
        this.rng = seed;
    }

    public void accept(T item) {
        count++;
        if (reservoir.size() < k) {
            reservoir.add(item);
        } else {
            // long version for large streams
            long j = 1L + (Math.abs(rng.nextLong()) % count);
            if (j <= k) {
                reservoir.set((int) (j - 1), item);
            }
        }
    }

    public void acceptAll(Iterator<T> it) {
        while (it.hasNext()) accept(it.next());
    }

    public List<T> getSample() { return new ArrayList<>(reservoir); }
    public int sampleSize() { return reservoir.size(); }
    public long processed() { return count; }

    public static boolean smokeTest() {
        ReservoirSampling<Integer> rs = new ReservoirSampling<>(10);
        for (int i = 0; i < 10000; i++) rs.accept(i);
        List<Integer> sample = rs.getSample();
        if (sample.size() != 10) return false;
        for (int v : sample) if (v < 0 || v >= 10000) return false;
        return true;
    }
}
