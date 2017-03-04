package chapter.nine;

import static chapter.Utils.WAR_AND_PEACE;
import static chapter.Utils.getPathToBook;

import chapter.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

class Chapter9 {

    Chapter9() {
        Utils.printChapter(Chapter9.class.getSimpleName());
    }

    void ex1() throws IOException {
        Utils.printExercise(1);

        Scanner scanner = null;
        PrintWriter writer = null;
        try {
            scanner = new Scanner(getPathToBook(WAR_AND_PEACE));
            try {
                writer = new PrintWriter("/tmp/book");
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
    }

    void ex2() throws Exception {
        Utils.printExercise(2);

        Scanner scanner = null;
        PrintWriter writer = null;
        Exception exception = null;
        try {
            scanner = new Scanner(getPathToBook(WAR_AND_PEACE));
            try {
                writer = new PrintWriter("/tmp/book");
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
            e.printStackTrace();
            throw e;
        }
    }

    void ex4() {
        Utils.printExercise(4);

        // catch block with some exception superclass help to group exception processing logic
        // in one place
    }

    public static void main(String[] args) {
        Chapter9 ch = new Chapter9();
        try {
            ch.ex1();
            ch.ex2();
            ch.ex3();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ch.ex4();
    }

}
