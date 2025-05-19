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
        Scene scene = new Scene(createAttributeView.getView(), 700, 600);
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
        // Ventana simple para mostrar el código generado
        javafx.scene.control.TextArea area = new javafx.scene.control.TextArea(code);
        area.setEditable(false);
        Scene scene = new Scene(area, 800, 600);
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
        Scene scene = new Scene(createMethodView.getView(), 900, 700); // Ventana más grande
        String css = getClass().getResource("/css/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EZJavaCode - Métodos de " + className);
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
