package chapter.three;

import javafx.scene.paint.Color;

@FunctionalInterface
public interface ColorTransformer {
    Color apply(final int x, final int y, final Color color);
}
