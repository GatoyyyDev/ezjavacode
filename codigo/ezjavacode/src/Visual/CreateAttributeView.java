package Visual;

import Funcional.EnumAtributo;
import Funcional.GeneradorDeClases;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableRow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Vista para la creación y gestión de atributos para una clase
 */
public class CreateAttributeView {
    
    private BorderPane mainLayout;
    private EZJavaCodeApp application;
    private String className;
    
    // Tabla para mostrar los atributos
    private TableView<AttributeModel> attributeTable;
    
    // Campos para el formulario de nuevo atributo
    private ComboBox<String> typeComboBox;
    private TextField nameField;
    private TextField valueField;
    private ComboBox<String> booleanComboBox; // Añadido para boolean
    private CheckBox privateCheckBox;
    private CheckBox staticCheckBox;
    private CheckBox finalCheckBox;
    
    // Lista observable para los atributos
    private ObservableList<AttributeModel> attributes = FXCollections.observableArrayList();
    
    public CreateAttributeView(EZJavaCodeApp application, String className) {
        this.application = application;
        this.className = className;
        createView();
        precargarAtributosDesdeGenerador();
    }
    
    /**
     * Crea la vista para gestionar atributos
     */
    private void createView() {
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("attribute-background");
        mainLayout.setPadding(new Insets(20));
        mainLayout.setMinHeight(700); // Altura mínima para que los botones de abajo siempre sean visibles
        
        // Título de la ventana con el nombre de la clase
        Label titleLabel = new Label("Crear Atributos de " + className);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.getStyleClass().add("view-title");
        
        // Crear la tabla de atributos
        createAttributeTable();
        
        // Crear el formulario para agregar atributos
        VBox formBox = createAttributeForm();
        
        // Botones de navegación
        HBox navigationButtons = createNavigationButtons();
        
        // Estructurar el layout
        VBox centerBox = new VBox(20);
        centerBox.getChildren().addAll(attributeTable, formBox);
        
        mainLayout.setTop(titleLabel);
        mainLayout.setCenter(centerBox);
        mainLayout.setBottom(navigationButtons);
        
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 20, 0));
        BorderPane.setMargin(navigationButtons, new Insets(20, 0, 0, 0));
    }
    
    /**
     * Crea la tabla para visualizar los atributos
     */
    private void createAttributeTable() {
        attributeTable = new TableView<>();
        Label placeholderLabel = new Label("No hay atributos definidos");
        placeholderLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1170d6;");
        attributeTable.setPlaceholder(placeholderLabel);

        // Columna de tipo
        TableColumn<AttributeModel, String> typeColumn = new TableColumn<>("Tipo");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setPrefWidth(120);

        // Columna de nombre
        TableColumn<AttributeModel, String> nameColumn = new TableColumn<>("Nombre");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(160);

        // Columna de valor
        TableColumn<AttributeModel, String> valueColumn = new TableColumn<>("Valor Inicial");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setPrefWidth(160);

        // Columna de visibilidad
        TableColumn<AttributeModel, String> visibilityColumn = new TableColumn<>("Visibilidad");
        visibilityColumn.setCellValueFactory(new PropertyValueFactory<>("visibility"));
        visibilityColumn.setPrefWidth(120);

        // Configurar la tabla
        attributeTable.getColumns().addAll(typeColumn, nameColumn, valueColumn, visibilityColumn);
        attributeTable.setItems(attributes);
        // Mostrar siempre al menos 4 filas
        attributeTable.setFixedCellSize(42);
        attributeTable.setPrefHeight(42 * 4 + 40); // 4 filas + header
        attributeTable.setMinHeight(42 * 4 + 40);
        attributeTable.setMaxHeight(42 * 8 + 40); // máximo 8 filas visibles
        attributeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // --- ESTILOS ---
        // Color de header #3db5ee
        String botonColor = "#3db5ee";
        attributeTable.setStyle("-fx-font-size: 18px; -fx-background-radius: 10px; -fx-background-color: white; -fx-border-color: #b3e0ff; -fx-border-width: 2px; -fx-table-cell-border-color: #b3e0ff; -fx-table-header-border-color: " + botonColor + ";");

        // Header y celdas con CellFactory
        String headerStyle = "-fx-alignment: CENTER; -fx-font-weight: bold; -fx-background-color: " + botonColor + "; -fx-text-fill: white;";
        typeColumn.setStyle(headerStyle);
        nameColumn.setStyle(headerStyle);
        valueColumn.setStyle(headerStyle);
        visibilityColumn.setStyle(headerStyle);

        // Centrar los valores de las celdas
        TableCellFactoryRedCenter(typeColumn);
        TableCellFactoryRedCenter(nameColumn);
        TableCellFactoryRedCenter(valueColumn);
        TableCellFactoryRedCenter(visibilityColumn);

        // Manejar doble clic para editar
        attributeTable.setRowFactory(tv -> {
            TableRow<AttributeModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    AttributeModel rowData = row.getItem();
                    populateForm(rowData);
                }
            });
            return row;
        });
    }
    
    /**
     * Crea el formulario para agregar o editar atributos
     */
    private VBox createAttributeForm() {
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(10));
        formBox.getStyleClass().add("form-panel");
        formBox.setStyle("-fx-background-color: #f7fbff; -fx-background-radius: 12px; -fx-padding: 24 32 24 32; -fx-effect: dropshadow(three-pass-box, #b3e0ff, 8, 0.1, 0, 2);");
        
        // Título del formulario
        Label formTitle = new Label("Agregar/Editar Atributo");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #005b99; -fx-padding: 0 0 8 0;");
        
        // Crear el grid para los campos
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        grid.setPadding(new Insets(10, 0, 10, 0));
        grid.setStyle("-fx-font-size: 16px;");
        
        // Campo tipo
        Label typeLabel = new Label("Tipo:");
        typeLabel.setStyle("-fx-font-weight: bold;");
        typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("String", "int", "double", "boolean");
        typeComboBox.setValue("String");
        typeComboBox.setPrefWidth(150);
        typeComboBox.setStyle("-fx-font-size: 15px; -fx-background-radius: 8px; -fx-padding: 2 8 2 8;");
        
        // Campo nombre
        Label nameLabel = new Label("Nombre:");
        nameLabel.setStyle("-fx-font-weight: bold;");
        nameField = new TextField();
        nameField.setPromptText("nombreAtributo");
        nameField.setPrefWidth(260);
        nameField.setStyle("-fx-font-size: 18px; -fx-background-radius: 8px; -fx-padding: 8 12 8 12;");
        
        // Campo valor inicial
        Label valueLabel = new Label("Valor Inicial:");
        valueLabel.setStyle("-fx-font-weight: bold;");
        valueField = new TextField();
        valueField.setPromptText("(opcional)");
        valueField.setPrefWidth(260);
        valueField.setStyle("-fx-font-size: 18px; -fx-background-radius: 8px; -fx-padding: 8 12 8 12;");
        booleanComboBox = new ComboBox<>();
        booleanComboBox.getItems().addAll("true", "false");
        booleanComboBox.setValue("true");
        booleanComboBox.setVisible(false);
        booleanComboBox.setStyle("-fx-font-size: 15px; -fx-background-radius: 8px; -fx-padding: 2 8 2 8;");
        
        // Campo visibilidad
        Label visibilityLabel = new Label("Visibilidad:");
        visibilityLabel.setStyle("-fx-font-weight: bold;");
        privateCheckBox = new CheckBox("Private");
        privateCheckBox.setSelected(true);
        privateCheckBox.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 0 12 0 0;");
        staticCheckBox = new CheckBox("Static");
        staticCheckBox.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 0 12 0 0;");
        finalCheckBox = new CheckBox("Final");
        finalCheckBox.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 0 12 0 0;");
        
        // Agrupar los checkbox en una fila
        HBox checkBoxRow = new HBox(24, staticCheckBox, finalCheckBox, privateCheckBox);
        checkBoxRow.setAlignment(Pos.CENTER_LEFT);
        
        // Agregar campos al grid
        grid.add(typeLabel, 0, 0);
        grid.add(typeComboBox, 1, 0);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(valueLabel, 0, 2);
        grid.add(valueField, 1, 2);
        grid.add(booleanComboBox, 1, 2);
        grid.add(checkBoxRow, 0, 3, 2, 1);
        
        // Botones de acción
        Button addButton = new Button("Agregar");
        addButton.getStyleClass().add("menu-button");
        addButton.setOnAction(e -> handleAddAttribute());
        
        Button deleteButton = new Button("Eliminar");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(e -> handleDeleteAttribute());
        
        Button clearButton = new Button("Limpiar");
        clearButton.getStyleClass().add("clear-button");
        clearButton.setOnAction(e -> clearForm());
        
        // Contenedor para los botones
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().setAll(deleteButton, clearButton, addButton);
        
        // Agregar todo al formulario
        formBox.getChildren().addAll(formTitle, grid, buttonBox);
        
        // Manejar el cambio de tipo
        typeComboBox.setOnAction(e -> {
            String selectedType = typeComboBox.getValue();
            if (selectedType.equalsIgnoreCase("boolean")) {
                valueField.setVisible(false);
                booleanComboBox.setVisible(true);
            } else {
                valueField.setVisible(true);
                booleanComboBox.setVisible(false);
            }
        });
        // Inicializa la visibilidad correcta
        if (typeComboBox.getValue().equalsIgnoreCase("boolean")) {
            valueField.setVisible(false);
            booleanComboBox.setVisible(true);
        } else {
            valueField.setVisible(true);
            booleanComboBox.setVisible(false);
        }
        
        return formBox;
    }
    
    /**
     * Crea los botones de navegación
     */
    private HBox createNavigationButtons() {
        Button backButton = new Button("← Volver");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> application.showCreateClassView());
        
        Button nextButton = new Button("Siguiente →");
        nextButton.getStyleClass().add("next-button");
        nextButton.setOnAction(e -> handleNextStep());
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton, nextButton);
        
        return buttonBox;
    }
    
    /**
     * Llena el formulario con los datos de un atributo para edición
     */
    private void populateForm(AttributeModel attribute) {
        typeComboBox.setValue(attribute.getType());
        nameField.setText(attribute.getName());
        if (attribute.getType().equalsIgnoreCase("boolean")) {
            booleanComboBox.setValue(attribute.getValue());
        } else {
            valueField.setText(attribute.getValue());
        }
        privateCheckBox.setSelected(attribute.getVisibility().equals("private"));
    }
    
    /**
     * Limpia el formulario
     */
    private void clearForm() {
        typeComboBox.setValue("String");
        nameField.clear();
        valueField.clear();
        booleanComboBox.setValue("true");
        privateCheckBox.setSelected(true);
    }
    
    /**
     * Maneja la acción de agregar un atributo
     */
    private void handleAddAttribute() {
        String name = nameField.getText().trim();
        // Validar si ya existe un atributo con ese nombre (ignorando mayúsculas/minúsculas)
        boolean existe = attributes.stream().anyMatch(attr -> attr.getName().equalsIgnoreCase(name));
        if (existe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ya existe un atributo con el nombre '" + name + "'.");
            alert.showAndWait();
            return;
        }
        String type = typeComboBox.getValue();
        String enumType = type;
        if (type.equalsIgnoreCase("int")) enumType = "INTEGER";
        if (type.equalsIgnoreCase("String")) enumType = "STRING";
        if (type.equalsIgnoreCase("double")) enumType = "DOUBLE";
        if (type.equalsIgnoreCase("boolean")) enumType = "BOOLEAN";
        String value;
        if (type.equalsIgnoreCase("boolean")) {
            value = booleanComboBox.getValue();
        } else {
            value = valueField.getText().trim();
        }
        String visibility = "";
        if (privateCheckBox.isSelected()) visibility += "private ";
        if (staticCheckBox.isSelected()) visibility += "static ";
        if (finalCheckBox.isSelected()) visibility += "final ";
        if (visibility.isEmpty()) visibility = "public";
        if (name.isEmpty()) {
            showAlert("Error", "El nombre del atributo no puede estar vacío");
            return;
        }
        // Validación para int
        if (type.equalsIgnoreCase("int") && !value.isEmpty()) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                showAlert("Error", "El valor debe ser un número entero válido");
                return;
            }
        }
        // Validación para double
        if (type.equalsIgnoreCase("double") && !value.isEmpty()) {
            try {
                Double.parseDouble(value);
                // Solo acepta formato con punto decimal
                if (!value.matches("^-?\\d+\\.\\d+$")) {
                    showAlert("Error", "El valor debe tener formato decimal, por ejemplo: 3.0");
                    return;
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "El valor debe ser un número decimal válido, por ejemplo: 3.0");
                return;
            }
        }
        Optional<AttributeModel> existingAttr = attributes.stream()
                .filter(attr -> attr.getName().equals(name))
                .findFirst();
        
        if (existingAttr.isPresent()) {
            AttributeModel attr = existingAttr.get();
            attr.setType(type);
            attr.setValue(value);
            attr.setVisibility(visibility);
            attributeTable.refresh();
        } else {
            AttributeModel newAttr = new AttributeModel(type, name, value, visibility);
            attributes.add(newAttr);
            GeneradorDeClases generador = application.getGenerador();
            if (generador != null) {
                EnumAtributo enumTipo = EnumAtributo.valueOf(enumType);
                boolean esPrivado = visibility.contains("private");
                boolean esStatic = visibility.contains("static");
                boolean esFinal = visibility.contains("final");
                generador.agregarAtributo(name, enumTipo, esPrivado, esStatic, esFinal, value);
            }
        }
        clearForm();
    }
    
    /**
     * Maneja la acción de eliminar un atributo
     */
    private void handleDeleteAttribute() {
        AttributeModel selected = attributeTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            attributes.remove(selected);
            // Eliminar también del modelo/clase real
            GeneradorDeClases generador = application.getGenerador();
            if (generador != null && generador.obtenerClase() != null) {
                generador.obtenerClase().getAtributos().removeIf(attr -> attr.getNombre().equals(selected.getName()));
            }
            clearForm();
        } else {
            showAlert("Aviso", "Selecciona un atributo para eliminar");
        }
    }
    
    /**
     * Maneja la acción de pasar al siguiente paso
     */
    private void handleNextStep() {
        if (attributes.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("No has definido ningún atributo. ¿Estás seguro de que quieres continuar?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                application.showCreateMethodView(className);
            }
            return;
        }
        application.showCreateMethodView(className);
    }
    
    /**
     * Muestra un cuadro de diálogo de alerta
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Diálogo personalizado para avisos
     */
    private void showCustomInfoDialog(String mensaje) {
        Stage dialog = new Stage();
        dialog.initOwner(mainLayout.getScene().getWindow());
        dialog.setTitle("Aviso");
        VBox box = new VBox(18);
        box.setPadding(new Insets(28, 36, 24, 36));
        box.setStyle("-fx-background-color: #f7fbff; -fx-background-radius: 14px; -fx-effect: dropshadow(three-pass-box, #b3e0ff, 8, 0.1, 0, 2);");
        HBox msgBox = new HBox(12);
        msgBox.setAlignment(Pos.CENTER_LEFT);
        // Icono info
        Label icon = new Label("\u2139");
        icon.setStyle("-fx-font-size: 32px; -fx-text-fill: #005b99; -fx-font-weight: bold;");
        // Texto
        Label msg = new Label(mensaje);
        msg.setStyle("-fx-font-size: 16px; -fx-text-fill: #005b99; -fx-font-weight: bold;");
        msgBox.getChildren().addAll(icon, msg);
        // Botón aceptar
        Button okBtn = new Button("Aceptar");
        okBtn.setStyle("-fx-background-color: linear-gradient(#3db5ee, #1170d6); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-font-size: 16px; -fx-padding: 8 28 8 28;");
        okBtn.setDefaultButton(true);
        okBtn.setOnAction(e -> dialog.close());
        HBox btnBox = new HBox(okBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        btnBox.setPadding(new Insets(18, 0, 0, 0));
        box.getChildren().addAll(msgBox, btnBox);
        Scene scene = new Scene(box);
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.showAndWait();
    }
    
    /**
     * Retorna la vista completa
     */
    public BorderPane getView() {
        return mainLayout;
    }
    
    /**
     * Clase modelo para representar un atributo en la tabla
     */
    public static class AttributeModel {
        private String type;
        private String name;
        private String value;
        private String visibility;
        
        public AttributeModel(String type, String name, String value, String visibility) {
            this.type = type;
            this.name = name;
            this.value = value;
            this.visibility = visibility;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getValue() {
            return value;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
        
        public String getVisibility() {
            return visibility;
        }
        
        public void setVisibility(String visibility) {
            this.visibility = visibility;
        }
    }
    
    // Precarga los atributos del generador si existen (para edición)
    private void precargarAtributosDesdeGenerador() {
        GeneradorDeClases generador = application.getGenerador();
        if (generador != null && generador.obtenerClase() != null) {
            attributes.clear();
            for (Funcional.Atributo attr : generador.obtenerClase().getAtributos()) {
                String tipo = attr.getTipo().name().equals("INTEGER") ? "int" :
                              attr.getTipo().name().equals("DOUBLE") ? "double" :
                              attr.getTipo().name().equals("BOOLEAN") ? "boolean" : "String";
                String valor = attr.getValorInicial() != null ? attr.getValorInicial() : "";
                // LIMPIAR COMILLAS DOBLES EXTERNAS si es string
                if (attr.getTipo() == Funcional.EnumAtributo.STRING) {
                    valor = valor.trim();
                    while (valor.startsWith("\"") && valor.endsWith("\"")) {
                        valor = valor.substring(1, valor.length() - 1).trim();
                    }
                }
                String vis = attr.isEsPrivado() ? "private" : "public";
                attributes.add(new AttributeModel(tipo, attr.getNombre(), valor, vis));
            }
        }
    }
    
    /**
     * Aplica un cellFactory a la columna para mostrar el texto en rojo y centrado al seleccionar
     */
    private void TableCellFactoryRedCenter(TableColumn<AttributeModel, String> col) {
        col.setCellFactory(column -> new TableCell<AttributeModel, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-alignment: CENTER;");
                    if (getTableRow() != null && getTableRow().isSelected()) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-alignment: CENTER;");
                    }
                }
            }
        });
    }
}
