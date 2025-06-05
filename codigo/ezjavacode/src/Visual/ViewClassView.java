package Visual;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewClassView {
    private LogoBackgroundPane mainLayout;
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
        mainLayout = new LogoBackgroundPane();
        BorderPane contentPane = new BorderPane();
        contentPane.getStyleClass().add("main-background");
        contentPane.setPadding(new Insets(20));
        contentPane.setStyle("-fx-background-color: transparent;");

        Label titleLabel = new Label("Código de la clase " + className);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.getStyleClass().add("view-title");
        titleLabel.setTextFill(Color.web("#1170d6"));
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 18, 0));

        TextArea codeArea = new TextArea(code);
        codeArea.setFont(Font.font("Consolas", 17));
        codeArea.setEditable(false);
        codeArea.setWrapText(false);
        codeArea.setStyle("-fx-background-color: #f7fbff; -fx-border-color: #1170d6; -fx-border-width: 2px; -fx-background-radius: 7px;");
        codeArea.setPrefRowCount(20);
        codeArea.setPrefColumnCount(60);
        codeArea.setFocusTraversable(false);

        ScrollPane codeScroll = new ScrollPane(codeArea);
        codeScroll.setFitToWidth(true);
        codeScroll.setFitToHeight(true);
        codeScroll.setStyle("-fx-background-color:transparent;");
        BorderPane.setMargin(codeScroll, new Insets(18, 0, 18, 0));

        Button closeButton = new Button("Cerrar");
        closeButton.setStyle("-fx-background-color: #1170d6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7px; -fx-font-size: 15px; -fx-padding: 7 28;");
        closeButton.setOnAction(e -> {
            if (application != null) {
                application.showMyClassesView();
            }
        });
        BorderPane.setAlignment(closeButton, Pos.CENTER);
        BorderPane.setMargin(closeButton, new Insets(16, 0, 0, 0));

        contentPane.setTop(titleLabel);
        contentPane.setCenter(codeScroll);
        contentPane.setBottom(closeButton);
        mainLayout.setContent(contentPane);
    }

    private String loadCode(String className) {
        java.io.File file = new java.io.File(Funcional.ExportadorDeClases.leerRutaExportacion() + "/" + className + ".java");
        if (!file.exists()) return "No se encontró el archivo de la clase.";
        try {
            return new String(java.nio.file.Files.readAllBytes(file.toPath()));
        } catch (java.io.IOException e) {
            return "Error al leer el código de la clase.";
        }
    }

    public StackPane getView() {
        return mainLayout;
    }
}
