package chapter.eight;

import static chapter.Utils.ALICE;
import static chapter.Utils.WAR_AND_PEACE;

import chapter.Utils;
import chapter.six.WebPageReader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Chapter8 {

    Chapter8() {
        Utils.printChapter(Chapter8.class.getSimpleName());
    }

    void ex1() {
        Utils.printExercise(1);

        Integer first = Integer.MAX_VALUE;
        Integer second = Integer.MAX_VALUE;
        System.out.println(unsignedOperation(first, second, Integer::sum));
        System.out.println(unsignedOperation(first, 2, Integer::divideUnsigned));
    }

    void ex2() {
        Utils.printExercise(2);

        Integer value = Integer.MIN_VALUE;
        try {
            Math.negateExact(value);
        } catch (ArithmeticException ex) {
            System.out.printf("Exception '%s' was generated for the value %d\n", ex, value);
        }
    }

    void ex3() {
        Utils.printExercise(3);

        GcdFinder gcd = new GcdFinder();
        int a = 180;
        int b = -150;

        if (gcd.gcdMod(a, b) == gcd.gcdFloorMod(a, b) &&
                gcd.gcdFloorMod(a, b) == gcd.gcbCustomRem(a, b)) {
            System.out.printf("The great common divisor is equal for all functions = %d\n", gcd.gcdMod(a, b));
        } else {
            System.out.printf("Results are different %d, %d, %d",
                    gcd.gcdMod(a, b), gcd.gcdFloorMod(a, b), gcd.gcbCustomRem(a, b));
        }
    }

    void ex4() {
        Utils.printExercise(4);

        Random generator = new CustomSeedRandom().get();
        int index = 0;
        double value;
        do {
            index++;
            value = generator.nextDouble();
        } while (index != 376050 && value != 0);
        System.out.printf("Index %d, value %f\n", index, value);
    }

    void ex5() throws IOException {
        Utils.printExercise(5);

        List<String> words = Utils.getWordsFromFile(WAR_AND_PEACE);
        Set<String> longWords = new HashSet<>();
        words.forEach(w -> {
            if (w.length() > 12) {
                longWords.add(w);
            }
        });
        longWords.forEach(w -> System.out.printf("Word '%s', length = %d\n", w, w.length()));
    }

    void ex6() {
        Utils.printExercise(6);

        GeometryComparators comparator = new GeometryComparators();
        Point2D point = new Point2D(1.0, 2.0);
        comparator.compare(point, point);
        comparator.compare(point, new Point2D(2.0, 3.0));

        Rectangle2D rectangle = new Rectangle2D(1.0, 2.0, 3, 4);
        comparator.compare(rectangle, rectangle);
        comparator.compare(rectangle, new Rectangle2D(2.0, 3.0, 4, 5));
    }

    void ex7() {
        Utils.printExercise(7);

        String[] strings = {"b", "c", null, "a"};
        Arrays.sort(strings, Comparator.nullsLast(Comparator.reverseOrder()));
        Arrays.stream(strings).forEach(System.out::println);
    }

    void ex8() {
        Utils.printExercise(8);

        Queue queue = Collections.checkedQueue(new ArrayDeque<>(), Point2D.class);
        try {
            queue.add(Point2D.ZERO);
            queue.add("test");
        } catch (ClassCastException ex) {
            System.out.printf("Catch exception during put in the queue '%s'\n", ex);
        }
    }

    void ex9() throws IOException {
        Utils.printExercise(9);

        int limit = 10;
        ScannerStream streamGenerator = new ScannerStream(ALICE);

        streamGenerator.words().limit(limit).forEach(w -> System.out.printf("%s, ", w));
        System.out.println();

        streamGenerator.lines().limit(limit).forEach(l -> System.out.printf("%s, ", l));
        System.out.println();

        streamGenerator.integers().limit(limit).forEach(i -> System.out.printf("%d, ", i));
        System.out.println();

        streamGenerator.doubles().limit(limit).forEach(d -> System.out.printf("%f, ", d));
        System.out.println();
    }

    void ex10() {
        Utils.printExercise(10);

        String zipFile = System.getenv().get("JAVA_HOME") + "/src.zip";
        String outputDirectory = "/tmp/ex10";
        try (ZipExtractor extractor = new ZipExtractor(zipFile, outputDirectory)) {
            extractor.extract();
            List<Path> files = Files.walk(Paths.get(outputDirectory))
                    .filter(file -> {
                        if (Files.isDirectory(file)) {
                            return false;
                        }

                        try {
                            return Files.lines(file)
                                    .anyMatch(line -> line.contains("transient") || line.contains("volatile"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            System.out.printf("Found %d classes:\n", files.size());
            files.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void ex11() throws IOException {
        Utils.printExercise(11);

        String url = "http://192.168.1.1/";
        String authentication = "test:test";
        String page = WebPageReader.readPage(url, authentication);
        System.out.println(page);
    }

    void ex12() {
        Utils.printExercise(12);

        Method[] methods = Chapter8.class.getMethods();
        Arrays.stream(methods).forEach(method -> {
            TestCase testCase = method.getAnnotation(TestCase.class);
            if (testCase == null) {
                return;
            }
            try {
                Integer actual = (Integer) method.invoke(this, testCase.argument());
                int result = Integer.compare(testCase.expected(), actual);

                System.out.printf("'%s()' returns %d. Expected %d.\n", method.getName(), actual, testCase.expected());
                if (result != 0) {
                    throw new AssertionError();
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    @TestCase(argument = 2, expected = 4)
    public int square(final int argument) {
        return argument * argument;
    }

    private Long unsignedOperation(final Integer first,
                                   final Integer second,
                                   final BiFunction<Integer, Integer, Integer> operation) {
        Integer result = operation.apply(first, second);
        return Integer.toUnsignedLong(result);
    }

    public static void main(String[] args) {
        Chapter8 ch = new Chapter8();
//        ch.ex1();
//        ch.ex2();
//        ch.ex3();
//        ch.ex4();
//        try {
//            ch.ex5();
//            ch.ex9();
//            ch.ex11();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ch.ex6();
//        ch.ex7();
//        ch.ex8();
//        ch.ex10();
        ch.ex12();
    }
}
