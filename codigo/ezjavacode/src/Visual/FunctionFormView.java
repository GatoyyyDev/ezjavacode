package Visual;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import Funcional.Funciones.Funcion;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FunctionFormView extends VBox {
    private TextField nameField;
    private ComboBox<String> returnTypeCombo;
    private TextArea codeArea;
    private Button saveBtn;
    private String functionName;

    public FunctionFormView(String functionName) {
        this.functionName = functionName;
        this.setSpacing(15);
        this.setPadding(new Insets(40, 48, 40, 48));
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(650);
        this.setMaxWidth(650);
        this.setMinWidth(420);
        this.setStyle("-fx-background-color: #f7fbff; -fx-border-color: #90caf9; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12;");
        this.getStyleClass().add("form-panel");
        Label title = new Label(functionName == null ? "Nueva función" : "Editar función: " + functionName);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.getStyleClass().add("view-title");
        HBox fila1 = new HBox(18);
        fila1.setAlignment(Pos.CENTER);
        Label tipoLabel = new Label("Tipo de retorno:");
        tipoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        returnTypeCombo = new ComboBox<>();
        returnTypeCombo.getItems().addAll("void", "int", "double", "String", "boolean");
        returnTypeCombo.setValue("void");
        fila1.getChildren().addAll(tipoLabel, returnTypeCombo);
        HBox fila2 = new HBox(12);
        fila2.setAlignment(Pos.CENTER);
        Label nombreLabel = new Label("Nombre de la función");
        nombreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        nameField = new TextField();
        nameField.setPrefWidth(240);
        nameField.setPrefHeight(32);
        fila2.getChildren().addAll(nombreLabel, nameField);
        Label codeLabel = new Label("Código de la función:");
        codeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        codeArea = new TextArea();
        codeArea.setPrefWidth(480);
        codeArea.setPrefHeight(180);
        saveBtn = new Button("Guardar función");
        saveBtn.setStyle("-fx-background-color: #3db5ee; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7; -fx-font-size: 18px; -fx-padding: 8 32;");
        saveBtn.setOnAction(e -> saveFunction());
        this.getChildren().addAll(title, fila1, fila2, codeLabel, codeArea, saveBtn);
        if (functionName != null) {
            loadFunction(functionName);
        }
    }

    private void loadFunction(String functionName) {
        File file = new File("funciones_generadas/" + functionName + ".java");
        if (!file.exists()) return;
        try {
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            // Aquí puedes parsear el contenido si quieres mostrar tipo y nombre
            codeArea.setText(content);
            nameField.setText(functionName);
        } catch (IOException e) {
            codeArea.setText("Error al cargar la función");
        }
    }

    private void saveFunction() {
        String name = nameField.getText().trim();
        String type = returnTypeCombo.getValue();
        String code = codeArea.getText();
        if (name.isEmpty() || code.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("El nombre y el código no pueden estar vacíos.");
            alert.showAndWait();
            return;
        }
        File file = new File("funciones_generadas/" + name + ".java");
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(code);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al guardar");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo guardar la función.");
            alert.showAndWait();
        }
        Stage stage = (Stage) this.getScene().getWindow();
        stage.close();
    }
}
