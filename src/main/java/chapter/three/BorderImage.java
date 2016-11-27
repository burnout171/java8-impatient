package chapter.three;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BorderImage extends Application {
    private final Image image;
    private final int size;

    public BorderImage() {
        image = new Image("queen-mary.png");
        size = 10;
    }

    public BorderImage(final Image image, final int size) {
        this.image = image;
        this.size = size;
    }

    @Override
    public void start(final Stage stage) throws Exception {
        ColorTransformer border = prepareTransformer();
        Image transformed = transform(image, border);
        show(stage, transformed);
    }

    private void show(final Stage stage, final Image transformed) {
        stage.setScene(new Scene(new HBox(new ImageView(image), new ImageView(transformed))));
        stage.show();
    }

    private ColorTransformer prepareTransformer() {
        return (x, y, color) -> {
            if (x < size || x > image.getWidth() - size ||
                    y < size || y > image.getHeight() - size) {
                return Color.BLACK;
            }
            return color;
        };
    }

    private Image transform(final Image in, final ColorTransformer transformer) {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                out.getPixelWriter().setColor(x, y, transformer.apply(x, y, in.getPixelReader().getColor(x, y)));
            }
        }
        return out;
    }
}
