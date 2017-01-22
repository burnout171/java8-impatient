package chapter.four;

import chapter.Utils;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Predicate;

class Chapter4 {

    Chapter4() {
        Utils.printChapter(Chapter4.class.getSimpleName());
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
        System.out.println(date);
    }

    public static void main(String[] args) {
        Chapter4 ch = new Chapter4();
//        ch.ex1();
//        ch.ex2();
//        ch.ex3();
        ch.ex4();
    }

}
