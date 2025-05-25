package Visual;

import Funcional.GeneradorDeClases;
import Funcional.EnumAtributo;
import Funcional.Funciones.Funcion;

import Visual.MainMenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Aplicación principal de EZJavaCode
 * Implementa la interfaz gráfica para la generación de código Java
 */
public class EZJavaCodeApp extends Application {

    private Stage primaryStage;
    // Instancia funcional compartida
    private GeneradorDeClases generador;
    private String currentClassName;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("EZJavaCode - Generador de Clases Java");
        
        // Mostrar el menú principal al iniciar
        showMainMenu();
        
        primaryStage.show();
    }
    
    /**
     * Muestra el menú principal de la aplicación
     */
    public void showMainMenu() {
        MainMenuView mainMenuView = new MainMenuView(this);
        Scene scene = new Scene(mainMenuView.getView(), 800, 600);
        // Cargamos el CSS directamente desde la carpeta de recursos
        String css = getClass().getResource("/css/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("EZJavaCode - Menú Principal");
    }
    
    /**
     * Muestra la ventana de creación de clases
     */
    public void showCreateClassView() {
        Visual.CreateClassView createClassView = new CreateClassView(this);
        Scene scene = new Scene(createClassView.getView(), 600, 400);
        // Cargamos el CSS directamente desde la carpeta de recursos
        String css = getClass().getResource("/css/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("EZJavaCode - Crear Clase");
    }
    
    /**
     * Muestra la ventana de creación de atributos para una clase
     * 
     * @param className Nombre de la clase a la que se añadirán atributos
     */
    public void showCreateAttributeView(String className) {
        setCurrentClassName(className);
        Visual.CreateAttributeView createAttributeView = new CreateAttributeView(this, className);
        Scene scene = new Scene(createAttributeView.getView(), 900, 750); // Aumenta altura y ancho por defecto
        // Cargamos el CSS directamente desde la carpeta de recursos
        String css = getClass().getResource("/css/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("EZJavaCode - Atributos de " + className);
    }
    
    /**
     * Muestra la ventana con el código generado
     * 
     * @param code Código generado
     */
    public void showGeneratedCode(String code) {
        // Crear layout principal
        javafx.scene.layout.BorderPane layout = new javafx.scene.layout.BorderPane();
        javafx.scene.control.TextArea area = new javafx.scene.control.TextArea(code);
        area.setEditable(false);
        area.getStyleClass().add("code-preview-area");
        layout.setCenter(area);
        
        // Botón para volver al menú principal
        javafx.scene.control.Button backButton = new javafx.scene.control.Button("Volver al menú principal");
        backButton.getStyleClass().add("menu-button");
        backButton.setOnAction(e -> showMainMenu());
        layout.setBottom(backButton);
        javafx.scene.layout.BorderPane.setMargin(backButton, new javafx.geometry.Insets(10));
        javafx.scene.layout.BorderPane.setAlignment(backButton, javafx.geometry.Pos.CENTER);
        
        javafx.scene.Scene scene = new javafx.scene.Scene(layout, 800, 600);
        // Añadir CSS para que el botón se vea igual que en el resto de la app
        String css = getClass().getResource("/css/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Código generado");
    }
    
    /**
     * Muestra la ventana de creación de métodos para una clase
     * 
     * @param className Nombre de la clase a la que se añadirán métodos
     */
    public void showCreateMethodView(String className) {
        Visual.CreateMethodView createMethodView = new CreateMethodView(this, className);
        Scene scene = new Scene(createMethodView.getView()); 
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false); 
    }
    
    /**
     * Muestra la ventana de listado de clases generadas
     */
    public void showMyClassesView() {
        MyClassesView view = new MyClassesView(this);
        Scene scene = new Scene(view.getView(), 800, 600);
        String css = getClass().getResource("/css/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mis clases");
    }
    
    /**
     * Muestra la ventana de visualización de la clase
     * 
     * @param className Nombre de la clase a visualizar
     */
    public void showViewClassView(String className) {
        ViewClassView view = new ViewClassView(this, className);
        Scene scene = new Scene(view.getView(), 800, 600);
        String css = getClass().getResource("/css/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Clase " + className);
    }
    
    /**
     * Método principal para ejecutar la aplicación
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public GeneradorDeClases getGenerador() {
        return generador;
    }
    public String getCurrentClassName() {
        return currentClassName;
    }
    public void setCurrentClassName(String name) {
        this.currentClassName = name;
    }
    public void setGenerador(GeneradorDeClases g) {
        this.generador = g;
    }
}
