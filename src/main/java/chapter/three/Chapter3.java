package chapter.three;

import chapter.Utils;
import chapter.three.image.BorderImage;
import chapter.three.image.LightenImage;
import javafx.application.Application;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Chapter3 {

    Chapter3() {
        Utils.printChapter(Chapter3.class.getSimpleName());
    }

    void ex1() {
        Utils.printExercise(1);

        Logger.getGlobal().setLevel(Level.OFF);
        generateAndPrintInfoSequence();

        Logger.getGlobal().setLevel(Level.INFO);
        generateAndPrintInfoSequence();
    }

    void ex2() {
        Utils.printExercise(2);
        withLock(new ReentrantLock(), () -> System.out.println("Inside lock"));
    }

    void ex3() {
        Utils.printExercise(3);
        assertFunction(() -> true);
        try {
            assertFunction(() -> false);
        } catch (AssertionError error) {
            System.out.println(error);
        }
    }

    void ex5() {
        Utils.printExercise(5);
        Application.launch(BorderImage.class);
    }

    void ex6() {
        Utils.printExercise(6);
        Application.launch(LightenImage.class);
    }

    void ex7() {
        Utils.printExercise(7);
        List<String> strings = Arrays.asList(" First", "second", " third ", "FouRth");
        Comparator<String> comparator = StringComparator.get(StringComparator.Options.all());
        strings.sort(comparator);
        strings.forEach(s -> System.out.printf("%s ", s));

    }

    void ex8() {
        Utils.printExercise(8);
        Application.launch(BorderImage.class, "--size=10", "--`color=white");
    }

    private void assertFunction(final BooleanSupplier supplier) {
        if (!supplier.getAsBoolean()) {
            throw new AssertionError("Wrong condition: " + supplier.getAsBoolean());
        }
    }

    private void withLock(final Lock lock, final Runnable run) {
        lock.lock();
        try {
            new Thread(run).start();
        } finally {
            lock.unlock();
        }
    }

    private void generateAndPrintInfoSequence() {
        for (int i : IntStream.range(1, 11).toArray()) {
            logIf(Level.INFO, () -> i == 10, () -> "a[" + i + "] = " + i);
        }
    }

    public static void logIf(final Level level, final Supplier<Boolean> condition, final Supplier<String> message) {
        Logger logger = Logger.getGlobal();
        if (logger.isLoggable(level) && condition.get()) {
            logger.log(logger.getLevel(), message.get());
        }
    }

    public static void main(String[] args) {
        Chapter3 ch = new Chapter3();
//        ch.ex1();
//        ch.ex2();
//        ch.ex3();
//        ch.ex5();
//        ch.ex6();
//        ch.ex7();
        ch.ex8();
    }

}
