package chapter.six;

import static chapter.Utils.getWordsFromFile;

import chapter.Utils;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

class Chapter6 {

    private static final String WAR_AND_PEACE = "war_and_peace.txt";

    Chapter6() {
        Utils.printChapter(Chapter6.class.getSimpleName());
    }

    void ex1() throws IOException {
        Utils.printExercise(1);

        AtomicReference<String> longest = new AtomicReference<>();
        LongAccumulator accumulator = new LongAccumulator(Math::max, 0);
        getWordsFromFile(WAR_AND_PEACE).parallelStream()
                .forEach(word -> {
                    UnaryOperator<String> update = (stored) -> {
                        String updated  = word.length() > accumulator.intValue() ? word : stored;
                        accumulator.accumulate(updated.length());
                        return updated;
                    };
                    longest.updateAndGet(update);
                });

        System.out.printf("Word %s with length %d\n", longest.get(), longest.get().length());
    }

    void ex3() {
        Utils.printExercise(3);

        AtomicLong atomicLong = new AtomicLong(0);
        counterRunner(atomicLong, atomicLong::incrementAndGet);

        LongAdder longAdder = new LongAdder();
        counterRunner(longAdder, () -> {
            longAdder.increment();
            return null;
        });
    }

    private <T extends Number> void counterRunner(final T counter, final Supplier<?> supplier) {
        final int threads = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CompletableFuture[] features = new CompletableFuture[threads];
        long start = System.currentTimeMillis();

        IntStream.range(0, threads).forEach(index -> {
            Runnable routine = () -> {
                for (int i = 0; i < 100_000; i++) supplier.get();
            };
            features[index] = CompletableFuture.runAsync(routine, executor);
        });
        CompletableFuture.allOf(features).whenComplete((v, ex) -> {
            if (ex != null) {
                System.out.println(ex);
                return;
            }
            long stop = System.currentTimeMillis();
            System.out.printf("Counter value %,d, execution time %d\n", counter.longValue(), stop - start);
        });
        executor.shutdown();
    }

    public static void main(String[] args) {
        Chapter6 ch = new Chapter6();
//        try {
//            ch.ex1();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ch.ex3();
    }

}
