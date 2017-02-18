package chapter.eight;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

class CustomSeedRandom {

    private static final long M = 25214903917L;
    private static final long V = 246154705703781L;
    private static final long A = 11;
    private static final long N = 281474976710656L;

    Random get() {
        long seed = Stream.iterate(prev(0), this::prev)
                .limit(1_000_000)
                .min(Comparator.comparingLong(x -> x ^ M))
                .orElse(0L);
        return new Random(seed ^ M);
    }

    private long prev(long s) {
        return (BigInteger.valueOf(s).subtract(BigInteger.valueOf(A)))
                .multiply(BigInteger.valueOf(V))
                .mod(BigInteger.valueOf(N)).longValue();
    }

}
