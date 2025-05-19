package Visual;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateMethodView {
    private BorderPane mainLayout;
    private Visual.EZJavaCodeApp application;
    private String className;
    private ObservableList<MethodModel> methods = FXCollections.observableArrayList();
    private TableView<MethodModel> methodTable;

    public CreateMethodView(Visual.EZJavaCodeApp application, String className) {
        this.application = application;
        this.className = className;
        createView();
    }

    private void createView() {
        mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.getStyleClass().add("attribute-background");

        // Título
        Label titleLabel = new Label("Metodos de {" + className + "}");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.getStyleClass().add("view-title");
        mainLayout.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 20, 0));

        // Tabla de métodos
        methodTable = new TableView<>();
        TableColumn<MethodModel, String> nameCol = new TableColumn<>("Nombre");
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        nameCol.setPrefWidth(180);
        TableColumn<MethodModel, String> typeCol = new TableColumn<>("Tipo");
        typeCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getReturnType()));
        typeCol.setPrefWidth(100);
        TableColumn<MethodModel, String> paramsCol = new TableColumn<>("Parámetros");
        paramsCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getParameters()));
        paramsCol.setPrefWidth(180);
        TableColumn<MethodModel, Void> actionsCol = new TableColumn<>("Acciones");
        actionsCol.setPrefWidth(140);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Editar");
            private final Button delBtn = new Button("Eliminar");
            {
                editBtn.setOnAction(e -> {
                    MethodModel m = getTableView().getItems().get(getIndex());
                    showEditDialog(m);
                });
                delBtn.setOnAction(e -> {
                    int idx = getIndex();
                    MethodModel m = getTableView().getItems().get(idx);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmar eliminación");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Seguro que quieres eliminar el método '" + m.getName() + "'?");
                    alert.initOwner(mainLayout.getScene().getWindow());
                    alert.showAndWait().ifPresent(result -> {
                        if (result == ButtonType.OK) {
                            methods.remove(idx);
                            // Eliminar de la clase real
                            var generador = application.getGenerador();
                            if (generador != null) {
                                var clase = generador.obtenerClase();
                                clase.getFunciones().removeIf(f -> f.getNombre().equals(m.getName()));
                            }
                        }
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, editBtn, delBtn);
                    setGraphic(box);
                }
            }
        });
        methodTable.getColumns().setAll(nameCol, typeCol, paramsCol, actionsCol);
        methodTable.setItems(methods);
        methodTable.setPrefHeight(150);
        methodTable.setPlaceholder(new Label("No hay métodos definidos"));

        // Botón para añadir método nuevo
        Button addMethodBtn = new Button("Añadir Método nuevo");
        addMethodBtn.getStyleClass().add("menu-button");
        addMethodBtn.setOnAction(e -> {
            MethodFormView form = new MethodFormView(className);
            Stage dialog = new Stage();
            dialog.setTitle("Nuevo Método");
            dialog.initOwner(mainLayout.getScene().getWindow());
            dialog.initModality(Modality.WINDOW_MODAL);
            VBox dialogVBox = new VBox(form);
            dialogVBox.setPadding(new Insets(20));
            Scene dialogScene = new Scene(dialogVBox, 700, 700);
            dialog.setScene(dialogScene);
            dialog.setResizable(false);
            dialog.centerOnScreen();
            String css = getClass().getResource("/css/style.css").toExternalForm();
            dialogScene.getStylesheets().add(css);
            // Al guardar el método
            form.getIncluirButton().setOnAction(ev -> {
                MethodModel m = form.getMethodModel();
                if (m != null) {
                    methods.add(m);
                    // --- Integración lógica ---
                    var generador = application.getGenerador();
                    if (generador != null) {
                        var clase = generador.obtenerClase();
                        Funcional.Funciones.Funcion funcion = new Funcional.Funciones.Funcion(m.getName(), m.getReturnType(), m.getVisibilidad());
                        if (!m.getParameters().isEmpty()) {
                            String[] params = m.getParameters().split(",");
                            for (String param : params) {
                                String[] parts = param.trim().split(" ");
                                if (parts.length == 2) {
                                    funcion.agregarParametro(parts[0], parts[1]);
                                }
                            }
                        }
                        StringBuilder bloque = new StringBuilder();
                        if (!m.getCode().isEmpty()) bloque.append(m.getCode()).append("\n");
                        if (!m.getReturnValue().isEmpty()) bloque.append("return ").append(m.getReturnValue()).append(";");
                        Funcional.Funciones.BloqueCodigo bloqueCodigo = new Funcional.Funciones.BloqueCodigo() {
                            @Override
                            public String generarCodigo() {
                                return bloque.toString();
                            }
                        };
                        funcion.agregarBloque(bloqueCodigo);
                        clase.agregarFuncion(funcion);
                    }
                    dialog.close();
                }
            });
            dialog.showAndWait();
        });
        VBox methodsBox = new VBox(10, methodTable, addMethodBtn);
        methodsBox.setAlignment(Pos.CENTER_LEFT);
        methodsBox.setPadding(new Insets(10, 0, 10, 0));

        VBox formPanel = new VBox(20, methodsBox);
        formPanel.setAlignment(Pos.CENTER);

        // Botón exportar
        Button exportBtn = new Button("Exportar {" + className + "}");
        exportBtn.getStyleClass().add("menu-button");
        exportBtn.setPrefWidth(300);
        exportBtn.setOnAction(e -> {
            var generador = application.getGenerador();
            if (generador != null) {
                var clase = generador.obtenerClase();
                String rutaExport = "clases_generadas"; // Carpeta correcta
                Funcional.ExportadorDeClases.guardarClaseComoArchivo(clase, rutaExport);
                mostrarAlertaExportacionExitosa(clase.getNombre(), rutaExport);
                application.showGeneratedCode(clase.generarCodigo());
            }
        });
        VBox exportBox = new VBox(exportBtn);
        exportBox.setAlignment(Pos.CENTER);
        exportBox.setPadding(new Insets(30, 0, 0, 0));

        // Panel central
        VBox centerBox = new VBox(30, formPanel, exportBox);
        centerBox.setAlignment(Pos.CENTER);
        mainLayout.setCenter(centerBox);
    }

    // --- Diálogo de edición ---
    private void showEditDialog(MethodModel m) {
        MethodFormView form = new MethodFormView(className);
        // Rellenar los campos
        form.getNameField().setText(m.getName());
        form.getReturnTypeCombo().setValue(m.getReturnType());
        // Cargar parámetros en la tabla
        form.getParamsTable().getItems().clear();
        String paramsStr = m.getParameters();
        if (paramsStr != null && !paramsStr.isEmpty()) {
            String[] paramsArr = paramsStr.split(",");
            for (String param : paramsArr) {
                String[] parts = param.trim().split(" ");
                if (parts.length == 2) {
                    form.getParamsTable().getItems().add(new ParameterModel(parts[0], parts[1]));
                }
            }
        }
        form.getPrivado().setSelected(m.isPrivate());
        form.getEstatico().setSelected(m.isStatic());
        form.getCodeArea().setText(m.getCode());
        form.getReturnField().setText(m.getReturnValue());

        Stage dialog = new Stage();
        dialog.setTitle("Editar Método");
        dialog.initOwner(mainLayout.getScene().getWindow());
        dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
        VBox dialogVBox = new VBox(form);
        dialogVBox.setPadding(new Insets(20));
        Scene dialogScene = new Scene(dialogVBox, 700, 700);
        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.centerOnScreen();
        String css = getClass().getResource("/css/style.css").toExternalForm();
        dialogScene.getStylesheets().add(css);
        form.getIncluirButton().setText("Guardar Cambios");
        form.getIncluirButton().setOnAction(ev -> {
            MethodModel nuevo = form.getMethodModel();
            if (nuevo != null) {
                // Actualizar en la lista visual
                int idx = methods.indexOf(m);
                if (idx >= 0) methods.set(idx, nuevo);
                // Actualizar en la clase real
                var generador = application.getGenerador();
                if (generador != null) {
                    var clase = generador.obtenerClase();
                    clase.getFunciones().removeIf(f -> f.getNombre().equals(m.getName()));
                    Funcional.Funciones.Funcion funcion = new Funcional.Funciones.Funcion(nuevo.getName(), nuevo.getReturnType(), nuevo.getVisibilidad());
                    if (!nuevo.getParameters().isEmpty()) {
                        String[] params = nuevo.getParameters().split(",");
                        for (String param : params) {
                            String[] parts = param.trim().split(" ");
                            if (parts.length == 2) {
                                funcion.agregarParametro(parts[0], parts[1]);
                            }
                        }
                    }
                    StringBuilder bloque = new StringBuilder();
                    if (!nuevo.getCode().isEmpty()) bloque.append(nuevo.getCode()).append("\n");
                    if (!nuevo.getReturnValue().isEmpty()) bloque.append("return ").append(nuevo.getReturnValue()).append(";");
                    Funcional.Funciones.BloqueCodigo bloqueCodigo = new Funcional.Funciones.BloqueCodigo() {
                        @Override
                        public String generarCodigo() {
                            return bloque.toString();
                        }
                    };
                    funcion.agregarBloque(bloqueCodigo);
                    clase.agregarFuncion(funcion);
                }
                dialog.close();
            }
        });
        dialog.showAndWait();
    }

    private void mostrarAlertaExportacionExitosa(String nombreClase, String ruta) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportación exitosa");
        alert.setHeaderText(null);
        alert.setContentText("La clase '" + nombreClase + ".java' se ha exportado correctamente a la carpeta: " + ruta);
        alert.showAndWait();
    }

    public BorderPane getView() {
        return mainLayout;
    }
}
