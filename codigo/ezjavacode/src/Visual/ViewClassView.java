package Visual;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewClassView {
    private BorderPane mainLayout;
    private EZJavaCodeApp application;
    private String className;
    private String code;

    public ViewClassView(EZJavaCodeApp application, String className) {
        this.application = application;
        this.className = className;
        this.code = loadCode(className);
        createView();
    }

    private void createView() {
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #b3e0ff;");

        // Título azul
        Label titleLabel = new Label("Clase " + className);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 38));
        titleLabel.setStyle("-fx-background-color: #1170d6; -fx-text-fill: white; -fx-padding: 18 0 18 0; -fx-background-radius: 2px;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);
        mainLayout.setTop(titleLabel);

        VBox centerBox = new VBox(0);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setPadding(new Insets(32, 40, 32, 40));

        Label codeLabel = new Label("Codigo");
        codeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        codeLabel.setStyle("-fx-background-color: #dde6ee; -fx-padding: 2 0 2 0; -fx-border-color: #888; -fx-border-width: 0 0 1 0;");
        codeLabel.setMaxWidth(Double.MAX_VALUE);
        codeLabel.setAlignment(Pos.CENTER);

        TextArea codeArea = new TextArea(code);
        codeArea.setFont(Font.font("Consolas", 17));
        codeArea.setEditable(false);
        codeArea.setWrapText(false);
        codeArea.setStyle("-fx-background-color: white; -fx-border-color: #888; -fx-border-width: 2px;");
        codeArea.setPrefRowCount(12);
        codeArea.setPrefColumnCount(40);

        ScrollPane codeScroll = new ScrollPane(codeArea);
        codeScroll.setFitToWidth(true);
        codeScroll.setStyle("-fx-background-color:transparent;");

        centerBox.getChildren().addAll(codeLabel, codeScroll);
        mainLayout.setCenter(centerBox);

        Button backButton = new Button("← Volver");
        backButton.setOnAction(e -> application.showMyClassesView());
        backButton.setStyle("-fx-background-color: #1170d6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6px;");
        BorderPane.setMargin(backButton, new Insets(24, 0, 24, 24));
        mainLayout.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.CENTER_LEFT);
    }

    private String loadCode(String className) {
        File file = new File(Funcional.ExportadorDeClases.leerRutaExportacion() + "/" + className + ".java");
        if (!file.exists()) return "No se encontró el archivo de la clase.";
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            return "Error al leer el código de la clase.";
        }
    }

    public BorderPane getView() {
        return mainLayout;
    }
}
