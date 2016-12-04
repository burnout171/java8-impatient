package chapter.three.image;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Map;

public abstract class AbstractImageWithEffect extends Application {

    private static final String DEFAULT_IMAGE_URL = "queen-mary.png";
    protected Image image = new Image(DEFAULT_IMAGE_URL);

    protected AbstractImageWithEffect() {
    }

    protected AbstractImageWithEffect(final Image image) {
        this.image = image;
    }

    protected void extractParameters() {
        Map<String, String> parameters = getNamedParameters();
        if (!parameters.isEmpty()) {
            image = new Image(parameters.get("image"));
        }
    }

    protected Map<String, String> getNamedParameters() {
        Parameters parameters = getParameters();
        return parameters.getNamed();
    }

    protected void show(final Stage stage, final Image transformed) {
        stage.setScene(new Scene(new HBox(new ImageView(image), new ImageView(transformed))));
        stage.show();
    }
}
