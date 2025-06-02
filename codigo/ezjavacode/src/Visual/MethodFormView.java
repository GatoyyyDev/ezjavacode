package Visual;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
    private CheckBox estatico;
    private ComboBox<String> visibilidadCombo;
    private TextArea codeArea;
    private TextField returnField;
    private Button addMethodBtn;

    public MethodFormView(String className) {
        this.setSpacing(15);
        this.setPadding(new Insets(40, 48, 40, 48));
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(650);
        this.setMaxWidth(650);
        this.setMinWidth(420);
        this.setStyle("-fx-background-color: #f7fbff; -fx-border-color: #90caf9; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12;");
        this.getStyleClass().add("form-panel");

        // Título
        Label title = new Label("Crear Metodos/Funciones de " + className);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #1565c0; -fx-padding: 10 0 20 0;");
        title.setAlignment(Pos.CENTER);
        title.getStyleClass().add("view-title");

        // Fila 1: Visibilidad y tipo
        HBox fila1 = new HBox(18);
        fila1.setAlignment(Pos.CENTER);
        Label visLabel = new Label("Visibilidad:");
        visLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        visibilidadCombo = new ComboBox<>();
        visibilidadCombo.getItems().addAll("public", "private", "protected");
        visibilidadCombo.setValue("public");
        visibilidadCombo.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #b3e0ff; -fx-border-width: 1; -fx-padding: 0 10; -fx-font-size: 15px;");
        Label tipoLabel = new Label("Elegir tipo");
        tipoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        returnTypeCombo = new ComboBox<>();
        returnTypeCombo.getItems().addAll("void", "int", "double", "String", "boolean");
        returnTypeCombo.setValue("void");
        returnTypeCombo.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #b3e0ff; -fx-border-width: 1; -fx-padding: 0 10; -fx-font-size: 15px;");
        fila1.getChildren().addAll(visLabel, visibilidadCombo, tipoLabel, returnTypeCombo);

        // Fila 2: Nombre de la función
        HBox fila2 = new HBox(12);
        fila2.setAlignment(Pos.CENTER);
        Label nombreLabel = new Label("Nombre de la función");
        nombreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        nameField = new TextField();
        nameField.setPrefWidth(240);
        nameField.setPrefHeight(32);
        nameField.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #b3e0ff; -fx-border-width: 1; -fx-padding: 0 10; -fx-font-size: 15px;");
        fila2.getChildren().addAll(nombreLabel, nameField);

        this.getChildren().addAll(title, fila1, fila2);

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
                btn.setOnAction(e -> {
                    ParameterModel param = getTableView().getItems().get(getIndex());
                    parametros.remove(param);
                });
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
        tipoParam.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #b3e0ff; -fx-border-width: 1; -fx-font-size: 15px;");
        TextField nombreParam = new TextField();
        nombreParam.setPrefWidth(100);
        nombreParam.setPrefHeight(32);
        nombreParam.setPromptText("Nombre");
        nombreParam.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #b3e0ff; -fx-border-width: 1; -fx-font-size: 15px;");
        Button addParamBtn = new Button("Añadir parámetro");
        addParamBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
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
        addParamBtn.getStyleClass().clear();
        addParamBtn.getStyleClass().add("menu-button");
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

        // Estático
        estatico = new CheckBox("¿Estático?");
        estatico.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        estatico.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 0 10 0 0;");
        HBox optionsBox = new HBox(20, estatico);
        optionsBox.setAlignment(Pos.CENTER);

        // Código
        Label codeLabel = new Label("Código");
        codeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        codeArea = new TextArea();
        codeArea.setPromptText("Introduce aquí el código del método...");
        codeArea.setPrefRowCount(5);
        codeArea.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #b3e0ff; -fx-border-width: 1; -fx-font-size: 15px;");

        // Return
        Label returnLabel = new Label("Return");
        returnLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        returnField = new TextField();
        returnField.setPrefWidth(240);
        returnField.setPrefHeight(32);
        returnField.setPromptText("dejar en blanco para no retornar");
        returnField.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #b3e0ff; -fx-border-width: 1; -fx-font-size: 15px;");

        // Layout principal
        this.getChildren().addAll(
            paramsBox,
            optionsBox,
            codeLabel, codeArea,
            returnLabel, returnField
        );

        // --- BOTÓN INCLUIR MÉTODO ---
        addMethodBtn = new Button("Incluir Método");
        addMethodBtn.getStyleClass().clear();
        addMethodBtn.getStyleClass().add("menu-button");
        addMethodBtn.setPrefHeight(38);
        addMethodBtn.setPrefWidth(200);
        // Añade margen superior
        VBox.setMargin(addMethodBtn, new Insets(18, 0, 0, 0));
        // Asegúrate de añadir el botón al layout principal al final
        this.getChildren().add(addMethodBtn);

        // --- CABECERA AZUL EN LA TABLA DE PARÁMETROS (USANDO CSS DIRECTO SOBRE EL HEADER ROW) ---
        Runnable forceBlueHeader = () -> {
            paramsTable.lookupAll(".table-view .column-header-background, .table-view .column-header, .table-view .column-header .label, .column-header-background, .column-header, .column-header .label").forEach(n -> {
                String style = n.getStyle();
                // Aplica azul solo si es fondo de cabecera
                if (n.getStyleClass().contains("column-header-background") || n.getStyleClass().contains("column-header")) {
                    n.setStyle("-fx-background-color: #3db5ee !important; -fx-border-radius: 6 6 0 0; -fx-background-radius: 6 6 0 0;");
                }
                // Aplica blanco y negrita a las etiquetas
                if (n.getStyleClass().contains("label")) {
                    n.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
                }
            });
        };
        paramsTable.skinProperty().addListener((obs, oldSkin, newSkin) -> forceBlueHeader.run());
        paramsTable.widthProperty().addListener((obs, oldVal, newVal) -> forceBlueHeader.run());
        paramsTable.heightProperty().addListener((obs, oldVal, newVal) -> forceBlueHeader.run());
        // Llama también tras inicializar
        forceBlueHeader.run();
    }

    public Button getIncluirButton() {
        return addMethodBtn;
    }

    public MethodModel getMethodModel() {
        String name = nameField.getText().trim();
        String returnType = returnTypeCombo.getValue();
        String visibilidad = visibilidadCombo.getValue();
        boolean isPriv = "private".equals(visibilidad); // Solo si el combo es private
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
        estatico.setSelected(false);
        codeArea.clear();
        returnField.clear();
    }

    // --- Getters para edición desde fuera ---
    public TextField getNameField() { return nameField; }
    public ComboBox<String> getReturnTypeCombo() { return returnTypeCombo; }
    public TableView<ParameterModel> getParamsTable() { return paramsTable; }
    public CheckBox getEstatico() { return estatico; }
    public ComboBox<String> getVisibilidadCombo() { return visibilidadCombo; }
    public TextArea getCodeArea() { return codeArea; }
    public TextField getReturnField() { return returnField; }
}
