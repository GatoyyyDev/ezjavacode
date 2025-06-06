package Visual;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Funcional.Funciones.Funcion;
import java.io.File;
import java.util.List;

public class MyFunctionsView {
    private LogoBackgroundPane mainLayout;
    private EZJavaCodeApp application;
    private GridPane functionsGrid;

    public MyFunctionsView(EZJavaCodeApp application) {
        this.application = application;
        createView();
    }

    private void createView() {
        mainLayout = new LogoBackgroundPane();
        BorderPane contentPane = new BorderPane();
        contentPane.getStyleClass().add("main-background");
        contentPane.setPadding(new Insets(0, 0, 0, 0));
        contentPane.setStyle("-fx-background-color: transparent;");

        // Cambia el título superior
        Label titleLabel = new Label("Mi libreria de Funciones");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #1565c0; -fx-padding: 10 0 20 0;");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        titleLabel.setPadding(new Insets(16, 0, 16, 0));
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        functionsGrid = new GridPane();
        functionsGrid.setHgap(30);
        functionsGrid.setVgap(30);
        functionsGrid.setAlignment(Pos.CENTER);
        refreshFunctionList();

        VBox whiteBox = new VBox(functionsGrid);
        whiteBox.setStyle("-fx-background-color: white; -fx-background-radius: 18px; -fx-padding: 32 38 32 38; -fx-effect: dropshadow(three-pass-box, #b3e0ff, 8, 0.1, 0, 2);");
        VBox centerBox = new VBox(whiteBox);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(0, 24, 0, 24));

        // REVERTIR: Elimina el StackPane y el logo, vuelve a dejar la caja blanca directamente como centro
        contentPane.setCenter(centerBox);

        // Botón de nueva función con estilo igual al de volver, pero con + en vez de flecha
        Button addBtn = new Button("+ Nueva función");
        addBtn.getStyleClass().add("back-button");
        addBtn.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 7; -fx-border-radius: 7; -fx-padding: 7 22;");
        addBtn.setOnAction(e -> showCreateFunctionDialog());
        VBox topBox = new VBox(titleLabel, addBtn);
        topBox.setAlignment(Pos.CENTER);

        Button backButton = new Button("\uD83E\uDC80 Volver al Menú");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> application.showMainMenu());
        BorderPane.setMargin(backButton, new Insets(24, 0, 24, 24));
        BorderPane.setAlignment(backButton, Pos.CENTER_LEFT);

        contentPane.setTop(topBox);
        contentPane.setBottom(backButton);
        mainLayout.setContent(contentPane);
    }

    private void refreshFunctionList() {
        functionsGrid.getChildren().clear();
        File dir = new File("funciones_generadas");
        File[] files = dir.listFiles((d, name) -> {
            if (!name.endsWith(".java")) return false;
            // Ocultar archivos internos
            return !(name.equals("Funcion.java") || name.equals("BloqueCodigo.java") || name.equals("BloqueIf.java") || name.equals("BloqueFor.java") || name.equals("BloqueImprimir.java"));
        });
        if (files == null) return;
        for (int i = 0; i < files.length; i++) {
            final String funcName = files[i].getName().replace(".java", "");
            final File funcFile = files[i];
            Label nameLabel = new Label(funcName);
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
            nameLabel.setStyle("-fx-text-fill: #005b99;");
            Button editBtn = new Button("Editar");
            Button delBtn = new Button("Eliminar");
            editBtn.getStyleClass().add("edit-btn");
            delBtn.getStyleClass().add("delete-btn");
            editBtn.setStyle("-fx-font-size: 15px;");
            delBtn.setStyle("-fx-font-size: 15px;");
            editBtn.setOnAction(e -> showEditFunctionDialog(funcName));
            delBtn.setOnAction(e -> showDeleteDialog(funcName, funcFile));
            HBox btnBox = new HBox(18, editBtn, delBtn);
            btnBox.setAlignment(Pos.CENTER);
            functionsGrid.add(nameLabel, 0, i);
            functionsGrid.add(btnBox, 1, i);
        }
    }

    private void showCreateFunctionDialog() {
        StandaloneMethodFormView.show(null, this::refreshFunctionList);
    }

    private void showEditFunctionDialog(String funcName) {
        StandaloneMethodFormView.show(funcName, this::refreshFunctionList);
    }

    private void showDeleteDialog(String funcName, File funcFile) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setTitle("Eliminar " + funcName);

        StackPane bg = new StackPane();
        bg.getStyleClass().add("blue-gradient-bg");
        bg.setStyle("-fx-border-color: #ff0000; -fx-border-width: 12px; -fx-background-radius: 4px;");

        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(0, 0, 18, 0));
        box.setMaxWidth(420);
        box.setMinWidth(340);
        box.setStyle("");

        Label title = new Label("Eliminar " + funcName);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #d32f2f;");

        Label msg = new Label("¿Seguro que quieres eliminar la función '" + funcName + "'?");
        msg.setFont(Font.font("Arial", 17));
        msg.setStyle("-fx-text-fill: #222; -fx-padding: 12 0 0 0;");
        msg.setWrapText(true);
        msg.setAlignment(Pos.CENTER);

        HBox btnBox = new HBox(24);
        btnBox.setAlignment(Pos.CENTER);
        Button cancelBtn = new Button("Cancelar");
        Button acceptBtn = new Button("Eliminar");
        cancelBtn.getStyleClass().add("green-btn");
        acceptBtn.getStyleClass().add("delete-btn");
        cancelBtn.setPrefWidth(110);
        acceptBtn.setPrefWidth(110);
        cancelBtn.setPrefHeight(36);
        acceptBtn.setPrefHeight(36);
        cancelBtn.setOnAction(e -> dialog.close());
        acceptBtn.setOnAction(e -> {
            funcFile.delete();
            dialog.close();
            refreshFunctionList();
        });
        btnBox.getChildren().addAll(cancelBtn, acceptBtn);
        box.getChildren().addAll(title, msg, btnBox);
        bg.getChildren().add(box);
        Scene scene = new Scene(bg);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public StackPane getView() {
        return mainLayout;
    }
}
