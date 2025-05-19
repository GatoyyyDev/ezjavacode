package Visual;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vista del menú principal de la aplicación
 */
public class MainMenuView {
    
    private BorderPane mainLayout;
    private Visual.EZJavaCodeApp application;
    
    public MainMenuView(Visual.EZJavaCodeApp application) {
        this.application = application;
        createView();
    }
    
    /**
     * Crea la vista del menú principal
     */
    private void createView() {
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("main-background");
        
        // Título de la aplicación
        Label titleLabel = new Label("EZJavaCode");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.getStyleClass().add("title-label");
        
        // Subtítulo
        Label subtitleLabel = new Label("Generador de Código Java");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        subtitleLabel.getStyleClass().add("subtitle-label");
        
        // Contenedor para el título y subtítulo
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 0, 40, 0));
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Botones del menú principal (en un grid de 2x2)
        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(20);
        menuGrid.setVgap(20);
        menuGrid.setAlignment(Pos.CENTER);
        
        // Botón para crear clase
        Button createClassBtn = createMenuButton("Crear Clase", null);
        createClassBtn.setOnAction(e -> application.showCreateClassView());
        
        // Botón para librerías
        Button librariesBtn = createMenuButton("Librerías", null);
        
        // Botón para mis clases
        Button myClassesBtn = createMenuButton("Mis clases", null);
        
        // Botón para ajustes
        Button settingsBtn = createMenuButton("Ajustes", null);
        
        // Agregar botones al grid
        menuGrid.add(createClassBtn, 0, 0);
        menuGrid.add(librariesBtn, 1, 0);
        menuGrid.add(myClassesBtn, 0, 1);
        menuGrid.add(settingsBtn, 1, 1);
        
        // Poner todo junto en el layout principal
        mainLayout.setTop(headerBox);
        mainLayout.setCenter(menuGrid);
        BorderPane.setMargin(menuGrid, new Insets(0, 0, 50, 0));
    }
    
    /**
     * Crea un botón estilizado para el menú principal
     */
    private Button createMenuButton(String text, String iconName) {
        Button button = new Button(text);
        button.setPrefSize(200, 150);
        button.getStyleClass().add("menu-button");
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // Si tenemos íconos, los agregaríamos aquí
        // try {
        //     ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/ui/images/" + iconName)));
        //     imageView.setFitHeight(64);
        //     imageView.setFitWidth(64);
        //     button.setGraphic(imageView);
        //     button.setContentDisplay(ContentDisplay.TOP);
        // } catch (Exception e) {
        //     System.err.println("No se pudo cargar el ícono: " + iconName);
        // }
        
        return button;
    }
    
    /**
     * Retorna la vista completa
     */
    public BorderPane getView() {
        return mainLayout;
    }
}
