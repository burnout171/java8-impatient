package chapter.six;

import static chapter.Utils.ALICE;
import static chapter.Utils.WAR_AND_PEACE;
import static chapter.Utils.getWordsFromFile;

import chapter.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

class Chapter6 {

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

        System.out.printf("Word \"%s\" with length %d\n", longest.get(), longest.get().length());
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

    void ex4() throws IOException {
        Utils.printExercise(4);

        LongAccumulator max = new LongAccumulator(Math::max, 0);
        LongAccumulator min = new LongAccumulator(Math::min, Long.MAX_VALUE);
        getWordsFromFile(WAR_AND_PEACE).parallelStream()
                .forEach(word -> {
                    int size = word.length();
                    max.accumulate(size);
                    min.accumulate(size);
                });

        System.out.printf("Max accumulator value %d\n", max.longValue());
        System.out.printf("Min accumulator value %d\n", min.longValue());

    }

    void ex5() {
        Utils.printExercise(5);

        ConcurrentMap<String, Set<File>> wordsToFiles = new ConcurrentHashMap<>();
        BiFunction<Set<File>, Set<File>, Set<File>> merge = (oldValue, newValue) -> {
            if (oldValue == null) {
                return newValue;
            }
            else {
                oldValue.addAll(newValue);
                return oldValue;
            }
        };

        wordsInFiles(wordsToFiles,
                (key, value) -> wordsToFiles.merge(key, new HashSet<>(Collections.singletonList(value)), merge));
    }

    void ex6() {
        Utils.printExercise(6);

        ConcurrentMap<String, Set<File>> wordsToFiles = new ConcurrentHashMap<>();
        wordsInFiles(wordsToFiles,
                (key, value) -> wordsToFiles.computeIfAbsent(key, k -> new HashSet<>()).add(value));
    }

    private void wordsInFiles(final ConcurrentMap<String, Set<File>> map,
                              final BiConsumer<String, File> consumer) {
        List<String> books = Arrays.asList(ALICE, WAR_AND_PEACE);
        books.parallelStream().forEach(book -> {
            File file = new File(this.getClass().getClassLoader().getResource(book).toString());
            try {
                getWordsFromFile(book).forEach(word -> {
                    consumer.accept(word, file);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        System.out.printf("Number of words = %d in files %s\n",
                map.keySet().size(),
                map.values().stream()
                        .filter(s -> s.size() == books.size())
                        .findFirst());
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
                ex.printStackTrace();
                return;
            }
            long stop = System.currentTimeMillis();
            System.out.printf("Counter value %,d, execution time %d\n", counter.longValue(), stop - start);
        });
        executor.shutdown();
    }

    public static void main(String[] args) {
        Chapter6 ch = new Chapter6();
        try {
            ch.ex1();
            ch.ex4();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ch.ex3();
        ch.ex5();
        ch.ex6();
    }

}
