package chapter.three;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.BiFunction;

public class LightenImage extends Application {

    private final Image image;
    private final double coefficient;

    public LightenImage() {
        image = new Image("queen-mary.png");
        coefficient = 2.0;
    }

    public LightenImage(final Image image, double coefficient) {
        this.image = image;
        this.coefficient = coefficient;
    }

    @Override
    public void start(final Stage stage) throws Exception {
        Image transformed = transform(image, (c, factor) ->
                c.deriveColor(0, 1, factor, coefficient), coefficient);
        show(stage, transformed);
    }

    private void show(final Stage stage, final Image transformed) {
        stage.setScene(new Scene(new HBox(new ImageView(image), new ImageView(transformed))));
        stage.show();
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
