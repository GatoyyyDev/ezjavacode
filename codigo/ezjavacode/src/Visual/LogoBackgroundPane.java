package Visual;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class LogoBackgroundPane extends StackPane {
    public LogoBackgroundPane() {
        super();
        this.getStyleClass().add("main-background"); // Fondo azul
        Image logoImg = null;
        try {
            logoImg = new Image(getClass().getResourceAsStream("/Visual/logo.png"));
        } catch (Exception e) {
            System.err.println("Error cargando logo.png: " + e.getMessage());
        }
        ImageView bgImage = new ImageView(logoImg);
        bgImage.setPreserveRatio(true);
        bgImage.setOpacity(0.13);
        bgImage.setFitWidth(600);
        bgImage.setFitHeight(600);
        bgImage.setSmooth(true);
        bgImage.setCache(true);
        this.getChildren().add(bgImage);
    }
    // Para añadir contenido encima del fondo
    public void setContent(javafx.scene.Node node) {
        if (this.getChildren().size() == 1) {
            this.getChildren().add(node);
        } else if (this.getChildren().size() > 1) {
            this.getChildren().set(1, node);
        }
    }
}
