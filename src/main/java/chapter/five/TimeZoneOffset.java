package chapter.five;

import java.time.ZoneOffset;
import java.util.Objects;

class TimeZoneOffset implements Comparable {

    private final ZoneOffset offset;
    private final String name;

    public TimeZoneOffset(final ZoneOffset offset, final String name) {
        this.offset = offset;
        this.name = name;
    }

    public ZoneOffset getOffset() {
        return offset;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeZoneOffset that = (TimeZoneOffset) o;
        return Objects.equals(getOffset(), that.getOffset());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOffset());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TimeZoneOffset{");
        sb.append("offset=").append(offset);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(final Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Can`t compare with null!");
        }
        TimeZoneOffset that = (TimeZoneOffset) o;
        return this.getOffset().compareTo(that.getOffset());
    }
}
