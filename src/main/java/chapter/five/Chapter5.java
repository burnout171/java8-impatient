package chapter.five;

import chapter.Utils;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

    void ex7() {
        Utils.printExercise(7);

        TimeInterval first = new TimeInterval(LocalTime.of(10, 00), LocalTime.of(11, 00));
        TimeInterval second = new TimeInterval(LocalTime.of(9, 00), LocalTime.of(12, 00));
        TimeInterval third = new TimeInterval(LocalTime.of(9, 00), LocalTime.of(10, 30));
        TimeInterval fourth = new TimeInterval(LocalTime.of(10, 30), LocalTime.of(11, 30));
        TimeInterval fifth = new TimeInterval(LocalTime.of(12, 00), LocalTime.of(13, 00));


        System.out.println(first.intersect(second));
        System.out.println(first.intersect(third));
        System.out.println(first.intersect(fourth));
        System.out.println(first.intersect(fifth));
    }

    void ex8() {
        Utils.printExercise(8);

        getZoneOffsets()
                .distinct()
                .sorted()
                .forEach(System.out::println);
    }

    void ex9() {
        Utils.printExercise(9);

        getZoneOffsets()
                .filter(offset -> offset.getOffset().getTotalSeconds() % 3600 != 0)
                .distinct()
                .sorted()
                .forEach(System.out::println);
    }

    void ex10() {
        Utils.printExercise(10);

        ZonedDateTime start =
                ZonedDateTime.of(LocalDate.now(), LocalTime.of(15, 5), ZoneId.of("America/Los_Angeles"));
        Duration duration = Duration.ofMinutes(10 * 60 + 50);
        ZonedDateTime finish = getDestinationTime(start, duration, ZoneId.of("Europe/Berlin"));
        System.out.println(finish);
    }

    void ex11() {
        Utils.printExercise(11);

        ZonedDateTime start =
                ZonedDateTime.of(LocalDate.now(), LocalTime.of(14, 5), ZoneId.of("Europe/Berlin"));
        Duration duration = getDuration(start, LocalTime.of(16, 40), ZoneId.of("America/Los_Angeles"));

        System.out.println(duration.toHours());
    }

    void ex12() {
        Utils.printExercise(12);

        ZonedDateTime start = ZonedDateTime.of(LocalDate.now(), LocalTime.of(15, 30), ZoneId.of("Europe/Berlin"));
        LocalTime time = LocalTime.now();
        ZoneId zone = ZoneId.of("Europe/Moscow");

        boolean meeting = meetingInHour(start, time, zone);
        System.out.println(meeting);

    }

    private boolean meetingInHour(final ZonedDateTime start,
                                      final LocalTime time,
                                      final ZoneId zone) {
        Duration duration = getDuration(start, time, zone);

        long minutes = Math.abs(duration.toMinutes());
        return 0 < minutes && minutes < 60;
    }

    private ZonedDateTime getDestinationTime(final ZonedDateTime start,
                                             final Duration duration,
                                             final ZoneId arrivalZone) {
        return start.plus(duration).withZoneSameInstant(arrivalZone);
    }

    private Duration getDuration(final ZonedDateTime start,
                                 final LocalTime time,
                                 final ZoneId zone) {
        LocalDate date = LocalDate.now();
        ZonedDateTime finish = ZonedDateTime.of(date, time, zone);
        return Duration.between(start, finish);
    }

    private Stream<TimeZoneOffset> getZoneOffsets() {
        Instant now = Instant.now();
        return ZoneId.getAvailableZoneIds().stream()
                .map(zone -> {
                    ZoneOffset offset = now.atZone(ZoneId.of(zone)).getOffset();
                    return new TimeZoneOffset(offset, zone);
                });
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
        ch.ex1();
        ch.ex2();
        ch.ex3();
        ch.ex4();
        ch.ex5();
        ch.ex6();
        ch.ex7();
        ch.ex8();
        ch.ex9();
        ch.ex10();
        ch.ex11();
        ch.ex12();
    }

}
