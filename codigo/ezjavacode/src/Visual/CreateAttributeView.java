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
    }
    
    /**
     * Crea la vista para gestionar atributos
     */
    private void createView() {
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("attribute-background");
        mainLayout.setPadding(new Insets(20));
        
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
        attributeTable.setPlaceholder(new Label("No hay atributos definidos"));
        
        // Columna de tipo
        TableColumn<AttributeModel, String> typeColumn = new TableColumn<>("Tipo");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setPrefWidth(100);
        
        // Columna de nombre
        TableColumn<AttributeModel, String> nameColumn = new TableColumn<>("Nombre");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(150);
        
        // Columna de valor
        TableColumn<AttributeModel, String> valueColumn = new TableColumn<>("Valor Inicial");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setPrefWidth(150);
        
        // Columna de visibilidad
        TableColumn<AttributeModel, String> visibilityColumn = new TableColumn<>("Visibilidad");
        visibilityColumn.setCellValueFactory(new PropertyValueFactory<>("visibility"));
        visibilityColumn.setPrefWidth(100);
        
        // Configurar la tabla
        attributeTable.getColumns().addAll(typeColumn, nameColumn, valueColumn, visibilityColumn);
        attributeTable.setItems(attributes);
        attributeTable.setPrefHeight(200);
        
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
        
        // Título del formulario
        Label formTitle = new Label("Agregar/Editar Atributo");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // Crear el grid para los campos
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 0, 10, 0));
        
        // Campo tipo
        Label typeLabel = new Label("Tipo:");
        typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("String", "int", "double", "boolean");
        typeComboBox.setValue("String");
        typeComboBox.setPrefWidth(150);
        
        // Campo nombre
        Label nameLabel = new Label("Nombre:");
        nameField = new TextField();
        nameField.setPromptText("nombreAtributo");
        
        // Campo valor inicial
        Label valueLabel = new Label("Valor Inicial:");
        valueField = new TextField();
        valueField.setPromptText("(opcional)");
        booleanComboBox = new ComboBox<>();
        booleanComboBox.getItems().addAll("true", "false");
        booleanComboBox.setValue("true");
        booleanComboBox.setVisible(false);
        
        // Campo visibilidad
        Label visibilityLabel = new Label("Visibilidad:");
        privateCheckBox = new CheckBox("Private");
        privateCheckBox.setSelected(true);
        staticCheckBox = new CheckBox("Static");
        finalCheckBox = new CheckBox("Final");
        
        // Agregar campos al grid
        grid.add(typeLabel, 0, 0);
        grid.add(typeComboBox, 1, 0);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(valueLabel, 0, 2);
        grid.add(valueField, 1, 2);
        grid.add(booleanComboBox, 1, 2);
        grid.add(staticCheckBox, 0, 3);
        grid.add(finalCheckBox, 1, 3);
        grid.add(privateCheckBox, 0, 4);
        
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
        buttonBox.getChildren().addAll(addButton, deleteButton, clearButton);
        
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
            showAlert("Aviso", "No has definido ningún atributo. ¿Estás seguro de que quieres continuar?");
        }
        GeneradorDeClases generador = application.getGenerador();
        if (generador != null) {
            application.showCreateMethodView(application.getCurrentClassName());
        } else {
            application.showMainMenu();
        }
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
}
