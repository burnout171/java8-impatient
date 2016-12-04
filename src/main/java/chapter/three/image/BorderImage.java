package chapter.three.image;

import chapter.three.ColorTransformer;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Map;

public class BorderImage extends AbstractImageWithEffect {
    private int size = 10;
    private Color color = Color.BLACK;

    public BorderImage() {
        super();
    }

    @Override
    public void start(final Stage stage) throws Exception {
        extractParameters();
        ColorTransformer border = prepareTransformer();
        Image transformed = transform(image, border);
        show(stage, transformed);
    }

    @Override
    protected void extractParameters() {
        Map<String, String> parameters = getNamedParameters();
        if(!parameters.isEmpty()) {
            size = Integer.valueOf(parameters.get("size"));
            color = Color.valueOf(parameters.get("color"));
        }
    }

    private ColorTransformer prepareTransformer() {
        return (x, y, color) -> {
            if (x < size || x > image.getWidth() - size ||
                    y < size || y > image.getHeight() - size) {
                return this.color;
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

    public static void main(String[] args) {
        System.out.println(args);
    }
}
