import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Chapter2 {

    private static final int SYMBOLS_FOR_WORD = 12;
    private static final String ALICE = "alice.txt";
    private static final String WAR_AND_PEACE = "war_and_peace.txt";

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
        long seed = System.currentTimeMillis();
        long a = 25214903917L;
        long c = 11L;
        long m = (long) Math.pow(2, 48);

        generator(a, c, m, seed)
                .limit(20)
                .forEach(System.out::println);
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
        ch.ex5();
    }
}
