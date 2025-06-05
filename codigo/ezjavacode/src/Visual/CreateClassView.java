package Visual;

import Funcional.GeneradorDeClases;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Vista para la creación de una nueva clase
 */
public class CreateClassView {
    
    private LogoBackgroundPane mainLayout;
    private EZJavaCodeApp application;
    private TextField classNameField;
    
    public CreateClassView(EZJavaCodeApp application) {
        this.application = application;
        createView();
    }
    
    /**
     * Crea la vista para crear una clase
     */
    private void createView() {
        mainLayout = new LogoBackgroundPane();
        BorderPane contentPane = new BorderPane();
        contentPane.getStyleClass().add("create-class-background");
        contentPane.setPadding(new Insets(20));
        
        // Título de la ventana
        Label titleLabel = new Label("Crear Clase");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.getStyleClass().add("view-title");
        
        // Panel central con el formulario
        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(50, 20, 50, 20));
        formBox.getStyleClass().add("form-panel");
        
        // Etiqueta para el campo de nombre de clase
        Label promptLabel = new Label("Introduce el nombre de la clase:");
        promptLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        
        // Campo para ingresar el nombre de la clase
        classNameField = new TextField();
        classNameField.setPromptText("NombreDeLaClase");
        classNameField.setPrefWidth(300);
        classNameField.setPrefHeight(40);
        classNameField.setFont(Font.font("Arial", 16));
        classNameField.getStyleClass().add("class-name-field");
        
        // Botones de acción
        Button createButton = new Button("★  Crear  ★");
        createButton.setPrefSize(140, 40);
        createButton.getStyleClass().add("create-button");
        createButton.getStyleClass().add("menu-button");
        createButton.setOnAction(e -> handleCreateClass());

        Button backButton = new Button("\uD83E\uDC80 Volver al Menú");
        backButton.setPrefWidth(200);
        backButton.setMinWidth(200);
        backButton.setPrefHeight(40);
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> application.showMainMenu());

        // Contenedor para los botones
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(createButton);

        // Añadir elementos al formulario
        formBox.getChildren().addAll(promptLabel, classNameField, buttonBox, backButton);

        // Añadir título y formulario al BorderPane
        contentPane.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        contentPane.setCenter(formBox);

        // Añadir el contentPane encima del fondo
        mainLayout.setContent(contentPane);
    }
    
    /**
     * Maneja la acción de crear una nueva clase
     */
    private void handleCreateClass() {
        String className = classNameField.getText().trim();
        
        if (className.isEmpty()) {
            // Mostrar mensaje de error (en una implementación real usaríamos un dialog)
            System.err.println("El nombre de la clase no puede estar vacío");
            return;
        }
        
        // Crear instancia funcional y guardarla en la app visual
        GeneradorDeClases generador = new GeneradorDeClases();
        generador.iniciarNuevaClase(className);
        application.setGenerador(generador);
        application.setCurrentClassName(className);
        
        // Ir a la vista de crear atributos
        application.showCreateAttributeView(className);
    }
    
    /**
     * Retorna la vista completa
     */
    public StackPane getView() {
        return mainLayout;
    }
}
