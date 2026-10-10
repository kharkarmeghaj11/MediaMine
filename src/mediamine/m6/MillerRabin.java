package mediamine.m6;

import java.math.BigInteger;

/**
 * Miller-Rabin probabilistic primality test, deterministic for 64-bit inputs using
 * the 12-base standard witness set: {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37}.
 *
 * Complexity: O(k · log³ n) where k = 12 bases, safe modular exponent with BigInteger.
 * Space: O(log n) bigints.
 *
 * Deterministic for all n < 2^64 per Pomerance/Zhang.
 */
public class MillerRabin {

    private static final long[] BASES = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};

    public boolean isPrime(long n) {
        if (n <= 1) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0) return false;

        // Write n - 1 as d * 2^r
        long d = n - 1;
        int r = 0;
        while ((d & 1L) == 0) {
            d >>= 1;
            r++;
        }

        BigInteger N = BigInteger.valueOf(n);
        for (long a : BASES) {
            if (a >= n) continue;
            BigInteger x = BigInteger.valueOf(a).modPow(BigInteger.valueOf(d), N);
            if (x.equals(BigInteger.ONE) || x.equals(N.subtract(BigInteger.ONE))) continue;
            boolean ok = false;
            for (int j = 0; j < r - 1; j++) {
                x = x.multiply(x).mod(N);
                if (x.equals(N.subtract(BigInteger.ONE))) { ok = true; break; }
            }
            if (!ok) return false;
        }
        return true;
    }

    public static boolean smokeTest() {
        MillerRabin mr = new MillerRabin();
        if (mr.isPrime(1)) return false;
        if (!mr.isPrime(2)) return false;
        if (!mr.isPrime(7919)) return false;
        if (!mr.isPrime(104729)) return false;
        if (!mr.isPrime(99991)) return false;
        if (mr.isPrime(1001L)) return false; // 7*11*13
        if (mr.isPrime(1000000L)) return false;
        if (!mr.isPrime(3)) return false;
        return true;
    }
}
