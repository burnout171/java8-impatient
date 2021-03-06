package chapter.one;

import chapter.Utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

class Chapter1 {

    private static final File FILE = new File(System.getProperty("user.home"));

    Chapter1() {
        Utils.printChapter(Chapter1.class.getSimpleName());
    }

    void ex1() {
        Utils.printExercise(1);
        String[] list = {"first", "second", "third"};
        Arrays.sort(list, (first, second) -> Integer.compare(first.length(), second.length()));
    }

    void ex2() {
        Utils.printExercise(2);
        FileFilter directoryFilter = new FileFilter() {
            @Override
            public boolean accept(final File pathname) {
                return Chapter1.accept(pathname);
            }
        };

        FILE.listFiles(directoryFilter);
        System.out.println();

        FILE.listFiles((file) -> accept(file));
        System.out.println();

        FILE.listFiles(directoryFilter::accept);
    }

    void ex3() {
        Utils.printExercise(3);
        FILE.list((file, name) -> {
            if (name.endsWith(".xml")) {
                System.out.println(file + File.separator + name);
                return true;
            }
            return false;
        });
    }

    void ex4() {
        Utils.printExercise(4);
        File[] files = FILE.listFiles();
        Arrays.sort(files, (first, second) -> {
            if (first.isDirectory() && second.isDirectory() || first.isFile() && second.isFile()) {
                return first.compareTo(second);
            } else if (first.isDirectory() && second.isFile()) {
                return -1;
            }
            return 1;
        });
        for (File file : files) {
            System.out.print(file + " ");
        }
        System.out.println();
    }

    void ex6() {
        Utils.printExercise(6);
        new Thread(RunnableEx.uncheck(() -> {
            System.out.println("sleep");
            TimeUnit.SECONDS.sleep(1);
        })).start();
    }

    void ex9() {
        Utils.printExercise(9);
        new Thread(andThen(() -> System.out.println("First thread"), () -> System.out.println("Second thread")))
                .start();
    }

    void ex10() {
        Utils.printExercise(10);
        String[] names = {"Peter", "Paul", "Mary"};
        List<Runnable> runners = new ArrayList<>();
        /* The following loop will not be compiled. i variable is not final.
           for(int i=0; i< names.length; i++) runners.add(() -> System.out.println(name[i]))
        */
        for (String name : names) {
            runners.add(() -> System.out.println(name));
        }

        for (Runnable runner : runners) {
            new Thread(runner).start();
        }
    }

    void ex14() {
        Utils.printExercise(14);
        /*
            interface J {
                void f();         // abstract. Inheritor class needs to implement it.
                default f() {...} // default. Inheritor will use this interface implementation.
                                  // Also it is able to override default method.
                static f() {...}  // static. Inheritor will not able to override it.
            }

            interface I {
                void f();
            }

            class Superclass {
                public void f() {...}
            }

            class Test extend Superclass implements I {} // f() method from Superclass will be used.
         */
    }

    private static boolean accept(final File pathname) {
        if (pathname.isDirectory()) {
            System.out.print(pathname + " ");
            return true;
        }
        return false;
    }

    private static Runnable andThen(final Runnable first, final Runnable second) {
        return () -> {
            new Thread(first).run();
            new Thread(second).run();
            System.out.println("Third thread");
        };
    }

    public static void main(String[] args) {
        Chapter1 ch1 = new Chapter1();
//        ch1.ex1();
        ch1.ex2();
        ch1.ex3();
        ch1.ex4();
        ch1.ex6();
        ch1.ex9();
        ch1.ex10();
//        ch1.ex14();
    }
}
