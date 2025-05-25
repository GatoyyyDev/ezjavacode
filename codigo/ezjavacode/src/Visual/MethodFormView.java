package Visual;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MethodFormView extends VBox {
    // --- Campos del formulario ---
    private TextField nameField;
    private ComboBox<String> returnTypeCombo;
    private ObservableList<ParameterModel> parametros = FXCollections.observableArrayList();
    private TableView<ParameterModel> paramsTable;
    private CheckBox privado;
    private CheckBox estatico;
    private ComboBox<String> visibilidadCombo;
    private TextArea codeArea;
    private TextField returnField;
    private Button addMethodBtn;

    public MethodFormView(String className) {
        this.setSpacing(15);
        this.setPadding(new Insets(40, 20, 40, 20));
        this.setAlignment(Pos.CENTER);
        this.setMaxWidth(500);
        this.setMinWidth(420);
        this.setStyle("-fx-background-color: #f7fbff; -fx-border-color: #90caf9; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12;");
        this.getStyleClass().add("form-panel");

        // Título
        Label title = new Label("Crear Metodos/Funciones de {" + className + "}");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.getStyleClass().add("view-title");

        // Visibilidad y tipo en la misma fila
        Label visLabel = new Label("Visibilidad:");
        visLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        visibilidadCombo = new ComboBox<>();
        visibilidadCombo.getItems().addAll("public", "private", "protected");
        visibilidadCombo.setValue("public");
        Label typeLabel = new Label("Elegir tipo");
        typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        returnTypeCombo = new ComboBox<>();
        returnTypeCombo.getItems().addAll("void", "int", "double", "String", "boolean");
        returnTypeCombo.setValue("void");
        HBox visTypeBox = new HBox(24, visLabel, visibilidadCombo, typeLabel, returnTypeCombo);
        visTypeBox.setAlignment(Pos.CENTER_LEFT);

        // Nombre de la función
        Label nameLabel = new Label("Nombre de la función");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        nameField = new TextField();
        nameField.setPrefWidth(240);
        nameField.setPrefHeight(32);
        HBox nameBox = new HBox(10, nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_LEFT);

        // Parámetros (tabla)
        paramsTable = new TableView<>();
        paramsTable.setItems(parametros);
        paramsTable.setMinWidth(380);
        paramsTable.setMaxWidth(420);
        paramsTable.setStyle("-fx-background-color: white; -fx-border-color: #bdbdbd; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6;");
        paramsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<ParameterModel, String> tipoCol = new TableColumn<>("Tipo");
        tipoCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        tipoCol.setMinWidth(100);
        tipoCol.setMaxWidth(200);
        tipoCol.setStyle("-fx-alignment: CENTER;");
        TableColumn<ParameterModel, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nombreCol.setMinWidth(100);
        nombreCol.setMaxWidth(200);
        nombreCol.setStyle("-fx-alignment: CENTER;");
        TableColumn<ParameterModel, Void> borrarCol = new TableColumn<>("Eliminar");
        borrarCol.setMinWidth(60);
        borrarCol.setMaxWidth(80);
        borrarCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("X");
            {
                btn.setStyle("-fx-background-color: #e57373; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7; -fx-padding: 2 10; -fx-cursor: hand;");
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
                setAlignment(Pos.CENTER);
            }
        });
        paramsTable.getColumns().setAll(tipoCol, nombreCol, borrarCol);

        // --- Altura dinámica de la tabla de parámetros ---
        parametros.addListener((ListChangeListener<ParameterModel>) c -> {
            int filas = Math.max(2, Math.min(8, parametros.size()));
            paramsTable.setPrefHeight(filas * 40 + 32); // 40px por fila aprox + cabecera
        });
        paramsTable.setPrefHeight(112); // Mínimo para 2 filas
        paramsTable.setMaxHeight(8 * 40 + 32); // Máximo para 8 filas

        // Input para añadir parámetro
        TextField tipoParam = new TextField();
        tipoParam.setPrefWidth(100);
        tipoParam.setPrefHeight(32);
        tipoParam.setPromptText("Tipo");
        TextField nombreParam = new TextField();
        nombreParam.setPrefWidth(100);
        nombreParam.setPrefHeight(32);
        nombreParam.setPromptText("Nombre");
        Button addParamBtn = new Button("Añadir parámetro");
        addParamBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        addParamBtn.setStyle("-fx-background-color: #3db5ee; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7; -fx-padding: 5 16; -fx-font-size: 15px; -fx-cursor: hand;");
        addParamBtn.setOnAction(e -> {
            String tipo = tipoParam.getText().trim();
            String nombre = nombreParam.getText().trim();
            if (!tipo.isEmpty() && !nombre.isEmpty() && nombre.matches("[a-zA-Z_$][a-zA-Z\\d_$]*")) {
                parametros.add(new ParameterModel(tipo, nombre));
                tipoParam.clear();
                nombreParam.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Introduce tipo y nombre válido para el parámetro (nombre Java válido).", ButtonType.OK);
                alert.showAndWait();
            }
        });
        HBox paramInputBox = new HBox(10, tipoParam, nombreParam, addParamBtn);
        paramInputBox.setMinWidth(380);
        paramInputBox.setMaxWidth(420);
        paramInputBox.setAlignment(Pos.CENTER);

        // Parámetros de entrada label
        Label paramsLabel = new Label("Parámetros de entrada");
        paramsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        // Placeholder de la tabla en negrita
        Label emptyLabel = new Label("Tabla sin contenido");
        emptyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        paramsTable.setPlaceholder(emptyLabel);

        VBox paramsBox = new VBox(7, paramsLabel, paramsTable, paramInputBox);
        paramsBox.setAlignment(Pos.CENTER);
        paramsBox.setPadding(new Insets(0,0,10,0));

        // Privado y estático
        privado = new CheckBox("¿Privado?");
        estatico = new CheckBox("¿Estático?");
        HBox optionsBox = new HBox(20, privado, estatico);
        optionsBox.setAlignment(Pos.CENTER);

        // Código
        Label codeLabel = new Label("Código");
        codeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        codeArea = new TextArea();
        codeArea.setPromptText("Introduce aquí el código del método...");
        codeArea.setPrefRowCount(5);

        // Return
        Label returnLabel = new Label("Return");
        returnLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        returnField = new TextField();
        returnField.setPrefWidth(240);
        returnField.setPrefHeight(32);
        returnField.setPromptText("dejar en blanco para no retornar");

        // Botón incluir método
        addMethodBtn = new Button("Incluir Método");
        addMethodBtn.getStyleClass().add("menu-button");

        // Layout principal
        this.getChildren().addAll(
            title,
            visTypeBox,
            nameBox,
            paramsBox,
            optionsBox,
            codeLabel, codeArea,
            returnLabel, returnField,
            addMethodBtn
        );
    }

    public Button getIncluirButton() {
        return addMethodBtn;
    }

    public MethodModel getMethodModel() {
        String name = nameField.getText().trim();
        String returnType = returnTypeCombo.getValue();
        String visibilidad = visibilidadCombo.getValue();
        boolean isPriv = privado.isSelected();
        boolean isStat = estatico.isSelected();
        String code = codeArea.getText();
        String ret = returnField.getText().trim();
        // Concatenar parámetros
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < parametros.size(); i++) {
            ParameterModel p = parametros.get(i);
            params.append(p.getType()).append(" ").append(p.getName());
            if (i < parametros.size() - 1) params.append(", ");
        }
        if (name.isEmpty()) return null;
        return new MethodModel(name, returnType, params.toString(), visibilidad, isPriv, isStat, code, ret);
    }

    public void clearForm() {
        nameField.clear();
        returnTypeCombo.setValue("void");
        parametros.clear();
        privado.setSelected(false);
        estatico.setSelected(false);
        codeArea.clear();
        returnField.clear();
    }

    // --- Getters para edición desde fuera ---
    public TextField getNameField() { return nameField; }
    public ComboBox<String> getReturnTypeCombo() { return returnTypeCombo; }
    public TableView<ParameterModel> getParamsTable() { return paramsTable; }
    public CheckBox getPrivado() { return privado; }
    public CheckBox getEstatico() { return estatico; }
    public ComboBox<String> getVisibilidadCombo() { return visibilidadCombo; }
    public TextArea getCodeArea() { return codeArea; }
    public TextField getReturnField() { return returnField; }
}
