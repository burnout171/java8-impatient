package chapter.nine;

import java.util.Objects;

class LabeledPoint implements Comparable<LabeledPoint> {

    private final String label;
    private final Point point;

    LabeledPoint(final LabeledPoint labeledPoint) {
        this.label = labeledPoint.getLabel();
        this.point = labeledPoint.getPoint();
    }

    LabeledPoint(final String label, final Point point) {
        this.label = label;
        this.point = point;
    }

    public String getLabel() {
        return label;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabeledPoint that = (LabeledPoint) o;
        return Objects.equals(getLabel(), that.getLabel()) &&
                Objects.equals(getPoint(), that.getPoint());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLabel(), getPoint());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LabeledPoint{");
        sb.append("label='").append(label).append('\'');
        sb.append(", point=").append(point);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(final LabeledPoint o) {
        if (Objects.isNull(o)) {
            return 1;
        }
        int result = label.compareTo(o.getLabel());
        return result == 0 ? point.compareTo(o.getPoint()) : result;
    }
}
