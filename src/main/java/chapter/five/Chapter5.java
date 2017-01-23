package chapter.five;

import chapter.Utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Predicate;

class Chapter5 {

    Chapter5() {
        Utils.printChapter(Chapter5.class.getSimpleName());
    }

    void ex1() {
        Utils.printExercise(1);
        Period period = Period.ofDays(255);

        printDate(
                LocalDate.of(2017, Month.JANUARY, 1).plus(period)
        );
    }

    void ex2() {
        Utils.printExercise(2);
        LocalDate base = LocalDate.of(2000, Month.FEBRUARY, 29);

        printDate(base.plusYears(1));
        printDate(base.plusYears(4));
        printDate(base.plusYears(1).plusYears(1).plusYears(1).plusYears(1));
    }

    void ex3() {
        Utils.printExercise(3);
        LocalDate now = LocalDate.now();

        LocalDate date = now.with(
                next(w -> w.getDayOfWeek().getValue() < 6)
        );

        printDate(date);
    }

    void ex4() {
        Utils.printExercise(4);
        cal(2017, Month.JANUARY);
    }

    void ex5() {
        Utils.printExercise(5);
        LocalDate birthday = LocalDate.of(1989, Month.OCTOBER, 31);

        System.out.println(birthday.until(LocalDate.now(), ChronoUnit.DAYS));
    }

    void ex6() {
        Utils.printExercise(6);
        int year = 1900;
        LocalDate start = LocalDate.of(year, Month.JANUARY, 1)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        LocalDate stop = LocalDate.of(year + 100, Month.JANUARY, 1);

        while (start.isBefore(stop)) {
            if (start.getDayOfMonth() == 13) {
                printDate(start);
            }
            start = start.plusWeeks(1);
        }
    }

    private void cal(final int year, final Month month) {
        LocalDate date = LocalDate.of(year, month, 1);
        int counter = 0;

        int shift = date.getDayOfWeek().getValue();
        for (int i = 1; i < shift; i++) {
            counter++;
            System.out.print("   ");
        }

        for (int i = 0; i < date.getDayOfMonth(); i++) {
            if (counter % 7 == 0) {
                System.out.println();
            }

            int day = date.getDayOfMonth();
            if (day < 10) {
                System.out.print("  " + day);
            } else {
                System.out.print(" " + day);
            }

            counter++;
            date = date.plusDays(1);
        }
    }

    private TemporalAdjuster next(final Predicate<LocalDate> predicate) {
        return TemporalAdjusters.ofDateAdjuster(temp -> {
            LocalDate date = temp;
            do {
                date = date.plusDays(1);
            } while (!predicate.test(date));
            return date;
        });
    }

    private void printDate(final LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E dd MM yyyy");
        System.out.println(date.format(formatter));
    }

    public static void main(String[] args) {
        Chapter5 ch = new Chapter5();
//        ch.ex1();
//        ch.ex2();
//        ch.ex3();
//        ch.ex4();
//        ch.ex5();
        ch.ex6();
    }

}
