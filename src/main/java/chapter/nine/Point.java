package chapter.nine;

import java.util.Objects;

class Point implements Comparable<Point> {

    private final int x;
    private final int y;

    Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int compareTo(final Point o) {
        if (Objects.isNull(o)) {
            return 1;
        }

        int result = compare(x, o.getX());
        return result == 0 ? compare(y, o.getY()) : result;
    }

    private int compare(final int value, final int that) {
        return (value < that) ? -1 : value == that ? 0 : 1;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Point{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }
}
