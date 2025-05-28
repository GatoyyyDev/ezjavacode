package Visual;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;

public class MyClassesView {
    private BorderPane mainLayout;
    private EZJavaCodeApp application;
    private GridPane classesGrid;

    public MyClassesView(EZJavaCodeApp application) {
        this.application = application;
        createView();
    }

    private void createView() {
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("main-background");
        mainLayout.setPadding(new Insets(0, 0, 0, 0));

        Label titleLabel = new Label("Mis clases");
        titleLabel.getStyleClass().add("view-title");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 64)); 
        titleLabel.setPadding(new Insets(16, 0, 16, 0)); 
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        mainLayout.setTop(titleLabel);

        classesGrid = new GridPane();
        classesGrid.setHgap(30);
        classesGrid.setVgap(22);
        classesGrid.setAlignment(Pos.CENTER);
        classesGrid.setStyle("-fx-background-color: transparent;");
        loadClasses();

        // Scroll para máximo 6 filas
        ScrollPane scrollPane = new ScrollPane(classesGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(6 * 48); 
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // --- RECUADRO BLANCO DETRÁS DEL GRID ---
        VBox whiteBox = new VBox(scrollPane);
        whiteBox.setAlignment(Pos.CENTER);
        whiteBox.setPadding(new Insets(36, 48, 36, 48));
        whiteBox.setStyle("-fx-background-color: white; -fx-background-radius: 18px; -fx-effect: dropshadow(three-pass-box, #b3e0ff, 14, 0.12, 0, 2);");
        VBox centerBox = new VBox(whiteBox);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(0, 24, 0, 24));
        mainLayout.setCenter(centerBox);

        // Botón para volver al menú principal
        Button backButton = new Button("← Volver al Menú");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> application.showMainMenu());
        BorderPane.setMargin(backButton, new Insets(24, 0, 24, 24));
        mainLayout.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.CENTER_LEFT);
    }

    private void loadClasses() {
        File dir = new File(Funcional.ExportadorDeClases.leerRutaExportacion());
        File[] files = dir.listFiles((d, name) -> name.endsWith(".java"));
        if (files == null) return;
        for (int i = 0; i < files.length; i++) {
            final String classNameFinal = files[i].getName().replace(".java", "");
            final File classFileFinal = files[i];
            Label nameLabel = new Label(classNameFinal);
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 22));
            nameLabel.setStyle("-fx-text-fill: #005b99;");
            classesGrid.add(nameLabel, 0, i);

            Button editBtn = new Button("Editar");
            editBtn.getStyleClass().add("menu-button");
            editBtn.setPrefSize(110, 36);
            Button viewBtn = new Button("Visualizar");
            viewBtn.getStyleClass().add("menu-button");
            viewBtn.setPrefSize(130, 36); 
            Button delBtn = new Button("Eliminar");
            delBtn.getStyleClass().add("menu-button");
            delBtn.setPrefSize(110, 36);
            delBtn.setOnAction(e -> showDeleteDialog(classNameFinal, classFileFinal));

            viewBtn.setOnAction(e -> application.showViewClassView(classNameFinal));

            editBtn.setOnAction(e -> {
                // Parsear la clase y cargarla en el generador
                File file = new File(Funcional.ExportadorDeClases.leerRutaExportacion() + "/" + classNameFinal + ".java");
                Funcional.Clase clase = Funcional.ClaseParser.parse(file);
                if (clase != null) {
                    Funcional.GeneradorDeClases generador = new Funcional.GeneradorDeClases();
                    generador.setClase(clase);
                    application.setGenerador(generador);
                    application.setCurrentClassName(classNameFinal);
                    application.showCreateAttributeView(classNameFinal);
                }
            });

            HBox btnBox = new HBox(18, editBtn, viewBtn, delBtn);
            btnBox.setAlignment(Pos.CENTER);
            classesGrid.add(btnBox, 1, i);
        }
    }

    private void showDeleteDialog(String className, File classFile) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mainLayout.getScene().getWindow());
        dialog.setResizable(false);
        dialog.setTitle("Eliminar " + className);

        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(0, 0, 18, 0));
        box.setStyle("-fx-background-color: white; -fx-border-color: #ff0000; -fx-border-width: 12px; -fx-background-radius: 4px;");

        Label title = new Label("Eliminar " + className);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-background-color: #1170d6; -fx-text-fill: white; -fx-padding: 16 0 16 0; -fx-background-radius: 2px;");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setMinWidth(340);
        title.setAlignment(Pos.CENTER);

        VBox msgBox = new VBox(6);
        msgBox.setAlignment(Pos.CENTER);
        Label msg1 = new Label("¿Estas seguro");
        msg1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label msg2 = new Label("de eliminar la clase?");
        msg2.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label classLabel = new Label(className);
        classLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        classLabel.setStyle("-fx-text-fill: #ff0000; -fx-padding: 8 0 0 0;");
        msgBox.getChildren().addAll(msg1, msg2, classLabel);

        HBox btnBox = new HBox(32);
        btnBox.setAlignment(Pos.CENTER);
        Button cancelBtn = new Button("Cancelar");
        cancelBtn.setStyle("-fx-background-color: linear-gradient(#e57373, #b71c1c); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-effect: dropshadow(three-pass-box, #888, 2, 0, 1, 1);");
        cancelBtn.setPrefWidth(110);
        cancelBtn.setPrefHeight(36);
        Button acceptBtn = new Button("Aceptar");
        acceptBtn.setStyle("-fx-background-color: linear-gradient(#81c784, #388e3c); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-effect: dropshadow(three-pass-box, #888, 2, 0, 1, 1);");
        acceptBtn.setPrefWidth(110);
        acceptBtn.setPrefHeight(36);
        btnBox.getChildren().addAll(cancelBtn, acceptBtn);

        cancelBtn.setOnAction(e -> dialog.close());
        acceptBtn.setOnAction(e -> {
            classFile.delete();
            dialog.close();
            refreshClassList();
        });

        box.getChildren().addAll(title, msgBox, btnBox);
        Scene scene = new Scene(box);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void refreshClassList() {
        classesGrid.getChildren().clear();
        loadClasses();
    }

    public BorderPane getView() {
        return mainLayout;
    }
}
