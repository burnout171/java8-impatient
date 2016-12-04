package chapter.three.image;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Map;
import java.util.function.BiFunction;

public class LightenImage extends AbstractImageWithEffect {
    private double coefficient = 2.0;

    public LightenImage() {
        super();
    }

    @Override
    public void start(final Stage stage) throws Exception {
        Image transformed = transform(image, (c, factor) ->
                c.deriveColor(0, 1, factor, coefficient), coefficient);
        show(stage, transformed);
    }

    @Override
    protected void extractParameters() {
        Map<String, String> parameters = getNamedParameters();
        if(!parameters.isEmpty()) {
            coefficient = Integer.valueOf(parameters.get("coefficient"));
        }
    }

    private <T> Image transform(final Image in, final BiFunction<Color, T, Color> f, T arg) {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                out.getPixelWriter().setColor(i, j,
                        f.apply(in.getPixelReader().getColor(i, j), arg));
            }
        }
        return out;
    }
}
