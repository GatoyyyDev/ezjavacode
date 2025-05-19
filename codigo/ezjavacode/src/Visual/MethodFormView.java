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
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setMaxWidth(500);
        this.setMinWidth(420);
        this.setStyle("-fx-background-color: #f7fbff; -fx-border-color: #90caf9; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12;");
        this.getStyleClass().add("form-panel");

        // Título
        Label title = new Label("Crear Metodos/Funciones de {" + className + "}");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.getStyleClass().add("view-title");

        // Visibilidad
        Label visLabel = new Label("Visibilidad:");
        visibilidadCombo = new ComboBox<>();
        visibilidadCombo.getItems().addAll("public", "private", "protected");
        visibilidadCombo.setValue("public");
        HBox visBox = new HBox(10, visLabel, visibilidadCombo);
        visBox.setAlignment(Pos.CENTER_LEFT);

        // Nombre de la función
        nameField = new TextField();
        HBox nameBox = new HBox(10, new Label("Nombre de la función"), nameField);
        nameBox.setAlignment(Pos.CENTER_LEFT);

        // Tipo de retorno
        returnTypeCombo = new ComboBox<>();
        returnTypeCombo.getItems().addAll("void", "int", "double", "String", "boolean");
        returnTypeCombo.setValue("void");
        HBox typeBox = new HBox(10, new Label("Elegir tipo"), returnTypeCombo);
        typeBox.setAlignment(Pos.CENTER_LEFT);

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
        TableColumn<ParameterModel, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nombreCol.setMinWidth(100);
        nombreCol.setMaxWidth(200);
        TableColumn<ParameterModel, Void> borrarCol = new TableColumn<>("Eliminar");
        borrarCol.setMinWidth(60);
        borrarCol.setMaxWidth(80);
        borrarCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("X");
            {
                btn.setOnAction(e -> {
                    int idx = getIndex();
                    parametros.remove(idx);
                });
                btn.setStyle("-fx-background-color: #e57373; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 2 8 2 8; -fx-background-radius: 4;");
                btn.setPrefWidth(32);
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
        tipoParam.setPromptText("Tipo");
        tipoParam.setPrefWidth(100);
        TextField nombreParam = new TextField();
        nombreParam.setPromptText("Nombre");
        nombreParam.setPrefWidth(100);
        Button addParamBtn = new Button("Añadir parámetro");
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
        paramInputBox.setAlignment(Pos.CENTER_LEFT);
        VBox paramsBox = new VBox(7, new Label("Parámetros de entrada"), paramsTable, paramInputBox);
        paramsBox.setAlignment(Pos.CENTER);
        paramsBox.setPadding(new Insets(0,0,10,0));

        // Privado y estático
        privado = new CheckBox("¿Privado?");
        estatico = new CheckBox("¿Estático?");
        HBox optionsBox = new HBox(20, privado, estatico);
        optionsBox.setAlignment(Pos.CENTER_LEFT);

        // Código
        Label codeLabel = new Label("Código");
        codeArea = new TextArea();
        codeArea.setPromptText("Introduce aquí el código del método...");
        codeArea.setPrefRowCount(5);

        // Return
        Label returnLabel = new Label("Return");
        returnField = new TextField();
        returnField.setPromptText("dejar en blanco para no retornar");

        // Botón incluir método
        addMethodBtn = new Button("Incluir Método");
        addMethodBtn.getStyleClass().add("menu-button");

        // Layout principal
        this.getChildren().addAll(
            title,
            visBox,
            nameBox,
            typeBox,
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
