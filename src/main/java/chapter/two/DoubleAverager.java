package chapter.two;

class DoubleAverager {
    private final double total;
    private final int count;

    DoubleAverager() {
        this.total = 0;
        this.count = 0;
    }

    DoubleAverager(double total, int count) {
        this.total = total;
        this.count = count;
    }

    double average() {
        return total > 0 ? total / count : 0;
    }

    DoubleAverager accept(final double num) {
        return new DoubleAverager(total + num, count + 1);
    }

    DoubleAverager combine(final DoubleAverager averager) {
        return new DoubleAverager(total + averager.total, count + averager.count);
    }
}
