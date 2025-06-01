package Visual;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsView {
    private BorderPane mainLayout;
    private Visual.EZJavaCodeApp application;
    private Label rutaLabel;

    public SettingsView(Visual.EZJavaCodeApp application) {
        this.application = application;
        createView();
    }

    private void createView() {
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("attribute-background");
        mainLayout.setPadding(new Insets(32));

        Label titleLabel = new Label("Ajustes");
        titleLabel.setStyle("-fx-font-size: 38px; -fx-font-weight: bold; -fx-text-fill: #005b99;");
        titleLabel.getStyleClass().add("view-title");

        // Caja visual para la ruta actual
        String rutaActual = Funcional.ExportadorDeClases.leerRutaExportacion();
        Label rutaTitulo = new Label("Ruta de exportación actual:");
        rutaTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1170d6;");
        rutaLabel = new Label(rutaActual);
        rutaLabel.setStyle("-fx-font-size: 16px; -fx-background-color: #f7fbff; -fx-padding: 10 20 10 20; -fx-border-color: #1170d6; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-text-fill: #005b99;");
        VBox rutaBox = new VBox(4, rutaTitulo, rutaLabel);
        rutaBox.setAlignment(Pos.CENTER);
        rutaBox.setPadding(new Insets(14, 0, 14, 0));

        Button elegirRutaBtn = new Button("Elegir carpeta de exportación");
        elegirRutaBtn.getStyleClass().add("next-button");
        elegirRutaBtn.setOnAction(e -> elegirNuevaRuta());

        Button defaultRutaBtn = new Button("Ruta por defecto");
        defaultRutaBtn.getStyleClass().add("clear-button");
        defaultRutaBtn.setOnAction(e -> setRutaPorDefecto());

        HBox botonesBox = new HBox(18, elegirRutaBtn, defaultRutaBtn);
        botonesBox.setAlignment(Pos.CENTER);
        botonesBox.setPadding(new Insets(10, 0, 10, 0));

        VBox box = new VBox(32, titleLabel, rutaBox, botonesBox);
        box.setAlignment(Pos.TOP_CENTER);
        mainLayout.setTop(box);

        Button backBtn = new Button("\uD83E\uDC80 Volver al Menú");
        backBtn.setPrefWidth(200);
        backBtn.setMinWidth(200);
        backBtn.setPrefHeight(40);
        backBtn.getStyleClass().add("back-button");
        backBtn.setOnAction(e -> application.showMainMenu());
        VBox bottomBox = new VBox(backBtn);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(32, 0, 0, 0));
        mainLayout.setBottom(bottomBox);
    }

    private void elegirNuevaRuta() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Selecciona la carpeta de exportación");
        File selectedDir = chooser.showDialog(null);
        if (selectedDir != null) {
            String nuevaRuta = selectedDir.getAbsolutePath();
            try (FileWriter writer = new FileWriter("export_path.txt", false)) {
                writer.write(nuevaRuta + System.lineSeparator());
            } catch (IOException ex) {
                rutaLabel.setText("Error al guardar la ruta: " + ex.getMessage());
                rutaLabel.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #d32f2f; -fx-border-color: #d32f2f; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 10 20 10 20;");
                return;
            }
            rutaLabel.setText(nuevaRuta);
            rutaLabel.setStyle("-fx-font-size: 16px; -fx-background-color: #f7fbff; -fx-padding: 10 20 10 20; -fx-border-color: #1170d6; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-text-fill: #005b99;");
        }
    }

    private void setRutaPorDefecto() {
        String defaultPath = "clases_generadas";
        try (FileWriter writer = new FileWriter("export_path.txt", false)) {
            writer.write(defaultPath + System.lineSeparator());
        } catch (IOException ex) {
            rutaLabel.setText("Error al guardar la ruta: " + ex.getMessage());
            rutaLabel.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #d32f2f; -fx-border-color: #d32f2f; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 10 20 10 20;");
            return;
        }
        rutaLabel.setText(defaultPath);
        rutaLabel.setStyle("-fx-font-size: 16px; -fx-background-color: #f7fbff; -fx-padding: 10 20 10 20; -fx-border-color: #1170d6; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-text-fill: #005b99;");
    }

    public BorderPane getView() {
        return mainLayout;
    }
}
