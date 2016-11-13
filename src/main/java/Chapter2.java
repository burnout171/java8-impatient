import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Chapter2 {

    private static final int SYMBOLS_FOR_WORD = 12;
    private static final String ALICE = "alice.txt";
    private static final String WAR_AND_PEACE = "war_and_peace.txt";

    private class DoubleAverager {
        private final double total;
        private final int count;

        DoubleAverager() {
            this.total = 0;
            this.count = 0;
        }

        DoubleAverager(double total, int count) {
            this.total = total;
            this.count = count;
        }

        double average() {
            return total > 0 ? total / count : 0;
        }

        DoubleAverager accept(final double num) {
            return new DoubleAverager(total + num, count + 1);
        }

        DoubleAverager combine(final DoubleAverager averager) {
            return new DoubleAverager(total + averager.total, count + averager.count);
        }
    }

    void ex1() throws IOException {
        List<String> words = getWordsFromFile(ALICE);
        long counter = parallelCounter(words);
        System.out.println(counter);
    }

    void ex2() throws IOException {
        List<String> words = getWordsFromFile(ALICE);
        words.parallelStream()
                .filter(w -> {
                    if (w.length() > SYMBOLS_FOR_WORD) {
                        System.out.println("Filter call with: " + w);
                        return true;
                    }
                    return false;
                })
                .limit(5).forEach(System.out::println);
    }

    void ex3() throws IOException {
        List<String> words = getWordsFromFile(WAR_AND_PEACE);

        long before = System.currentTimeMillis();
        parallelCounter(words);
        long after = System.currentTimeMillis();
        long diff = after - before;
        System.out.printf("Time for parallel processing: %d ms %n", diff);

        before = System.currentTimeMillis();
        sequentialCounter(words);
        after = System.currentTimeMillis();
        diff = after - before;
        System.out.printf("Time for sequential processing: %d ms %n", diff);
    }

    void ex4() {
        int[] values = {1, 4, 9, 16};
        Stream stream = Stream.of(values);
        stream.forEach(System.out::println);

        IntStream intStream = IntStream.of(values);
        intStream.forEach(System.out::println);
    }

    void ex5() {
        initGenerator()
                .limit(20)
                .forEach(System.out::println);
    }

    void ex6() {
        characterStream(WAR_AND_PEACE).forEach(System.out::println);
    }

    void ex7() {
        System.out.println(isFiniteSimple(characterStream(WAR_AND_PEACE)));
        System.out.println(isFiniteStable(initGenerator()));
    }

    void ex8() {
        Stream<Long> first = Stream.iterate(1L, v -> v + 1).limit(10);
        Stream<Long> second = Stream.iterate(1L, v -> v + 1).limit(11);
        zip(first, second).forEach(System.out::println);
    }

    void ex9() {
        List<Integer> array = Arrays.asList(1, 2, 3, 4, 5);
        BinaryOperator<List<Integer>> accumulator = (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };

        List<Integer> first = Stream.of(array).reduce(new ArrayList<>(), accumulator);
        collectionPrinter("First reduce: ", first);

        List<Integer> second = Stream.of(array).reduce((list1, list2) -> {
            list1.addAll(list2);
            return list1;
        }).orElse(new ArrayList<>());
        collectionPrinter("Second reduce: ", second);

        List<Integer> third = Stream.of(array).reduce(new ArrayList<>(),
                accumulator,
                accumulator);
        collectionPrinter("Third reduce: ", third);
    }

    private void ex10() {
        Stream<Double> stream = Stream.iterate(1.0, v -> v + 1.0).limit(4);
        System.out.println(stream.reduce(new DoubleAverager(),
                DoubleAverager::accept,
                DoubleAverager::combine).average());
    }

    private void collectionPrinter(final String message, final Collection<Integer> collection) {
        System.out.print(message);
        collection.forEach(v -> System.out.printf("%d ", v));
    }

    private Stream<Long> initGenerator() {
        long seed = System.currentTimeMillis();
        long a = 25214903917L;
        long c = 11L;
        long m = (long) Math.pow(2, 48);
        return generator(a, c, m, seed);
    }

    /**
     * Linear congruential generator.
     * x2 = (a * x1 + c) % m
     * @return Stream of Long values.
     */
    private Stream<Long> generator(long a, long c, long m, long seed) {
        return Stream.iterate(seed, v -> (a * v + c) % m);
    }

    private long parallelCounter(final List<String> words) {
        return words.parallelStream()
                .filter(w -> w.length() > SYMBOLS_FOR_WORD)
                .count();
    }

    private long sequentialCounter(final List<String> words) {
        return words.stream()
                .filter(w -> w.length() > SYMBOLS_FOR_WORD)
                .count();
    }

    private List<String> getWordsFromFile(final String name) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Path file = Paths.get(classLoader.getResource(name).getFile());
        String contents = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
        return Arrays.asList(contents.split("[\\P{L}]+"));
    }

    private Stream<Character> characterStream(final String s) {
        return s.chars().mapToObj(c -> (char) c);
    }

    /**
     * The easiest implementation of ifFinite method. Will return true if Stream is finite.
     * Otherwise loop is never be finished.
     * @param stream Incoming stream.
     * @param <T> Type under the Stream.
     * @return True if stream is finite.
     */
    public static <T> boolean isFiniteSimple(final Stream<T> stream) {
        Iterator<T> it = stream.iterator();
        while (it.hasNext()) {
            it.next();
        }
        return true;
    }

    /**
     * Will return true if Stream is finite and false otherwise.
     * @param stream Incoming stream.
     * @param <T> Type under the Stream.
     * @return True if stream is finite, false otherwise.
     */
    public static <T> boolean isFiniteStable(final Stream<T> stream) {
        return stream.spliterator().getExactSizeIfKnown() >= 0;
    }

    public static <T> Stream<T> zip(final Stream<T> first, final Stream<T> second) {
        Iterator<T> firstIt = first.iterator();
        Iterator<T> secondIt = second.iterator();
        List<T> result = new ArrayList<>();
        while (firstIt.hasNext() && secondIt.hasNext()) {
            result.add(secondIt.next());
            result.add(firstIt.next());
        }
        return result.stream();
    }

    public static void main(String[] args) {
        Chapter2 ch = new Chapter2();
//        try {
//            ch.ex1();
//            ch.ex2();
//            ch.ex3();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ch.ex4();
//        ch.ex5();
//        ch.ex6();
//        ch.ex7();
//        ch.ex8();
//        ch.ex9();
        ch.ex10();
    }
}
