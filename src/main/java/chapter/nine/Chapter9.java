package chapter.nine;

import static chapter.Utils.WAR_AND_PEACE;
import static chapter.Utils.getPathToBook;

import chapter.Utils;
import chapter.six.WebPageReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class Chapter9 {

    private static final String PATH = "/tmp/%s";

    Chapter9() {
        Utils.printChapter(Chapter9.class.getSimpleName());
    }

    void ex1() throws IOException {
        Utils.printExercise(1);

        Path path = Paths.get(String.format(PATH, "book"));
        Scanner scanner = null;
        PrintWriter writer = null;

        try {
            scanner = new Scanner(getPathToBook(WAR_AND_PEACE));
            try {
                writer = new PrintWriter(path.toFile());
                while (scanner.hasNext()) writer.println(scanner.next());
            } finally {
                if (Objects.nonNull(writer)) {
                    writer.close();
                }
            }
        } finally {
            if (Objects.nonNull(scanner)) {
                scanner.close();
            }
        }

        Files.delete(path);
    }

    void ex2() throws Exception {
        Utils.printExercise(2);

        Path path = Paths.get(String.format(PATH, "book"));
        Scanner scanner = null;
        PrintWriter writer = null;
        Exception exception = null;

        try {
            scanner = new Scanner(getPathToBook(WAR_AND_PEACE));
            try {
                writer = new PrintWriter(path.toFile());
                while (scanner.hasNext()) writer.println(scanner.next());
            } catch (FileNotFoundException ex) {
                exception = ex;
            } finally {
                try {
                    if (Objects.nonNull(writer)) {
                        writer.close();
                    }
                } catch (Exception ex) {
                    if (Objects.nonNull(exception)) {
                        exception.addSuppressed(ex);
                        throw exception;
                    }
                    throw ex;
                }
            }
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                if (Objects.nonNull(scanner)) {
                    scanner.close();
                }
            } catch (Exception ex) {
                if (Objects.nonNull(exception)) {
                    exception.addSuppressed(ex);
                    throw exception;
                }
                throw ex;
            }
        }

        Files.delete(path);
    }

    void ex3() throws FileNotFoundException, UnknownHostException {
        Utils.printExercise(3);

        Random random = new Random();
        try {
            if (random.nextInt() % 2 == 0) {
                throw new FileNotFoundException();
            } else {
                throw new UnknownHostException();
            }
        } catch (FileNotFoundException | UnknownHostException e) {
            //According to exercise.
            throw e;
        }
    }

    void ex4() {
        Utils.printExercise(4);

        // catch block with some exception superclass help to group exception processing logic
        // in one place
    }

    void ex5() throws Exception {
        Utils.printExercise(5);

        Path from = getPathToBook(WAR_AND_PEACE);
        Path to = Paths.get(String.format(PATH, "inverseBook"));

        try (FileInverter inverter = new FileInverter(from, to)) {
            inverter.symbols();
        }
    }

    void ex6() throws Exception {
        Utils.printExercise(6);

        Path from = getPathToBook(WAR_AND_PEACE);
        Path to = Paths.get(String.format(PATH, "inverseBook"));

        try (FileInverter inverter = new FileInverter(from, to)) {
            inverter.lines();
        }
    }

    void ex7() throws IOException {
        Utils.printExercise(7);

        Path to = Paths.get(String.format(PATH, "storedPage"));
        String url = "https://yandex.ru/";

        String page = WebPageReader.readPage(url, null);
        Files.write(to, page.getBytes());

        Files.delete(to);
    }

    void ex8() {
        Utils.printExercise(8);

        Point point = new Point(1, 2);
        Point first = new Point(3, 4);
        Point second = new Point(0, 0);

        System.out.printf("%s and %s are equal = %s\n", point, first, point.compareTo(first) == 0);
        System.out.printf("%s and %s are equal = %s\n", point, second, point.compareTo(second) == 0);
        System.out.printf("%s and %s are equal = %s\n", point, point, point.compareTo(point) == 0);
        System.out.printf("%s and %s are equal = %s\n", point, null, point.compareTo(null) == 0);
    }

    void ex9() {
        Utils.printExercise(9);

        LabeledPoint point = new LabeledPoint("test", new Point(1, 2));
        LabeledPoint another = new LabeledPoint("another", new Point(3, 4));

        System.out.printf("%s and %s are equal = %s\n", point, another, point.equals(another));
        System.out.printf("%s and %s are equal = %s\n", point, point, point.equals(new LabeledPoint(point)));
    }

    void ex10() {
        Utils.printExercise(10);

        LabeledPoint point = new LabeledPoint("test", new Point(1, 2));
        LabeledPoint another = new LabeledPoint("another", new Point(3, 4));

        System.out.printf("%s and %s are equal = %s\n", point, another, point.compareTo(another) == 0);
        System.out.printf("%s and %s are equal = %s\n", point, point, point.compareTo(point) == 0);
        System.out.printf("%s and %s are equal = %s\n", point, null, point.compareTo(null) == 0);
    }

    void ex11() throws IOException, InterruptedException {
        Utils.printExercise(11);

        Path to = Paths.get(String.format(PATH, "cards"));
        String reg = "(\\d{4}[ -]){4}\\d{4}";

        System.out.println(System.getProperty("user.home"));

        ProcessBuilder processBuilder =
                new ProcessBuilder("grep",
                        "-ohr",
                        "-E",
                        reg,
                        System.getProperty("user.home") + "/test")
                        .redirectOutput(to.toFile());
        processBuilder.start().waitFor(5, TimeUnit.MINUTES);

        Files.delete(to);
    }

    public static void main(String[] args) {
        Chapter9 ch = new Chapter9();
        try {
            ch.ex1();
            ch.ex2();
            ch.ex3();
            ch.ex5();
            ch.ex6();
            ch.ex7();
            ch.ex11();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ch.ex4();
        ch.ex8();
        ch.ex9();
        ch.ex10();
    }

}
