package chapter.five;

import java.time.LocalTime;
import java.util.Objects;

class TimeInterval {
    private final LocalTime start;
    private final LocalTime end;

    public TimeInterval(final LocalTime start, final LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public boolean intersect(final TimeInterval that) {
        return this.start.isAfter(that.start) && this.start.isBefore(that.end)
                || that.start.isAfter(this.start) && that.start.isBefore(this.end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeInterval that = (TimeInterval) o;
        return Objects.equals(start, that.start) &&
                Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
