package chapter.eight;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

import java.util.Comparator;

class GeometryComparators {

    private final Comparator<Point2D> point2DComparator =
            Comparator.comparingDouble(Point2D::getX)
                    .thenComparingDouble(Point2D::getY);
    private final Comparator<Rectangle2D> rectangle2DComparator =
            Comparator.comparingDouble(Rectangle2D::getMinX)
                    .thenComparingDouble(Rectangle2D::getMinY)
                    .thenComparingDouble(Rectangle2D::getWidth)
                    .thenComparingDouble(Rectangle2D::getHeight);

    boolean compare(Point2D first, Point2D second) {
        int result = point2DComparator.compare(first, second);
        boolean equals = result == 0;
        System.out.printf("Result of comparing '%s' and '%s' is %b\n", first, second, equals);

        return equals;
    }

    boolean compare(Rectangle2D first, Rectangle2D second) {
        int result = rectangle2DComparator.compare(first, second);
        boolean equals = result == 0;
        System.out.printf("Result of comparing '%s' and '%s' is %b\n", first, second, equals);

        return equals;
    }
}
