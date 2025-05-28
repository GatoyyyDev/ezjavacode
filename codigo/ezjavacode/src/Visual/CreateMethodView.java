package Visual;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
        this.methods = javafx.collections.FXCollections.observableArrayList();
        // --- Asegurar que el generador tiene la clase cargada ---
        Funcional.GeneradorDeClases generador = application.getGenerador();
        if (generador == null || generador.obtenerClase() == null || !className.equals(generador.obtenerClase().getNombre())) {
            java.io.File file = new java.io.File("clases_generadas/" + className + ".java");
            Funcional.Clase clase = Funcional.ClaseParser.parse(file);
            if (generador == null) {
                generador = new Funcional.GeneradorDeClases();
                application.setGenerador(generador);
            }
            generador.setClase(clase);
        }
        createView();
        precargarMetodosDesdeGenerador();
    }

    private void createView() {
        mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.getStyleClass().add("attribute-background");

        // Título
        Label titleLabel = new Label("Métodos de " + className);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.getStyleClass().add("view-title");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 20, 0));

        // Tabla de métodos
        methodTable = new TableView<>();
        methodTable.setPrefHeight(300); // Reduce la altura preferida de la tabla para que la pantalla sea más compacta
        methodTable.setStyle("-fx-background-color: white; -fx-control-inner-background: white; -fx-table-cell-border-color: #b3e0ff; -fx-font-size: 17px;");
        methodTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Editar");
            private final Button delBtn = new Button("Eliminar");
            {
                // Aplica el color #3db5ee a los botones
                String blueStyle = "-fx-background-color: #3db5ee; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7; -fx-padding: 5 16; -fx-font-size: 15px; -fx-cursor: hand;";
                editBtn.setStyle(blueStyle);
                delBtn.setStyle(blueStyle);
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
                    HBox box = new HBox(8, editBtn, delBtn);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
        actionsCol.setPrefWidth(160);
        methodTable.getColumns().setAll(nameCol, typeCol, paramsCol, actionsCol);
        methodTable.setItems(methods);
        methodTable.setPlaceholder(new Label("No hay métodos definidos"));

        // Centrar valores en las columnas
        nameCol.setCellFactory(column -> {
            return new TableCell<MethodModel, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                    setAlignment(Pos.CENTER);
                }
            };
        });
        typeCol.setCellFactory(column -> {
            return new TableCell<MethodModel, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                    setAlignment(Pos.CENTER);
                }
            };
        });
        paramsCol.setCellFactory(column -> {
            return new TableCell<MethodModel, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                    setAlignment(Pos.CENTER);
                }
            };
        });

        // Forzar color azul en la cabecera desde Java
        methodTable.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            Node header = methodTable.lookup(".column-header-background");
            if (header != null) {
                header.setStyle("-fx-background-color: #3db5ee;");
            }
        });

        // --- Botones alineados en una sola línea ---
        Button backBtn = new Button("← Volver");
        backBtn.getStyleClass().add("back-button");
        backBtn.setPrefWidth(140);
        backBtn.setOnAction(e -> application.showCreateAttributeView(className));

        Button addMethodBtn = new Button("Añadir Método nuevo");
        addMethodBtn.getStyleClass().add("menu-button");
        addMethodBtn.setPrefWidth(220);
        addMethodBtn.setOnAction(e -> {
            MethodFormView form = new MethodFormView(className);
            Stage dialog = new Stage();
            dialog.setTitle("Nuevo Método");
            dialog.initOwner(mainLayout.getScene().getWindow());
            dialog.initModality(Modality.WINDOW_MODAL);
            StackPane root = new StackPane(form);
            root.setStyle("-fx-background-color: #eaf6fb;");
            root.setPadding(new Insets(32, 0, 32, 0)); // Espacio arriba y abajo
            StackPane.setAlignment(form, Pos.CENTER);
            Scene dialogScene = new Scene(root, 700, 700);
            String css = getClass().getResource("/css/style.css").toExternalForm();
            dialogScene.getStylesheets().add(css);
            dialog.setScene(dialogScene);
            dialog.setResizable(false);
            dialog.centerOnScreen();
            // Al guardar el método
            form.getIncluirButton().setOnAction(ev -> {
                MethodModel m = form.getMethodModel();
                if (m != null) {
                    methods.add(m);
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

        Button exportBtn = new Button("Exportar " + className);
        exportBtn.getStyleClass().add("menu-button");
        exportBtn.setPrefWidth(220);
        exportBtn.setOnAction(e -> {
            var generador = application.getGenerador();
            if (generador != null) {
                var clase = generador.obtenerClase();
                Funcional.ExportadorDeClases.guardarClaseComoArchivo(clase, null);
                mostrarAlertaExportacionExitosa(clase.getNombre(), Funcional.ExportadorDeClases.leerRutaExportacion());
                application.showGeneratedCode(clase.generarCodigo());
            }
        });

        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox bottomBox = new HBox(20, backBtn, spacer1, addMethodBtn, spacer2, exportBtn);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(0, 32, 0, 32)); // Sin margen inferior

        // Agrupa título, tabla y botones en un solo VBox
        VBox contentBox = new VBox(16, titleLabel, methodTable, bottomBox); // Espacio agradable entre título, tabla y botones
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(0, 32, 0, 32));

        // Layout principal
        BorderPane layout = new BorderPane();
        layout.setTop(contentBox); // Todo el contenido va arriba
        layout.setCenter(null);
        layout.setBottom(null);
        layout.setStyle("-fx-background-color: #eaf6fb;");
        String css = getClass().getResource("/css/style.css").toExternalForm();
        layout.getStylesheets().add(css);
        mainLayout.setCenter(layout);
        mainLayout.setMinHeight(Region.USE_COMPUTED_SIZE);
        mainLayout.setPrefHeight(Region.USE_COMPUTED_SIZE);
        if (mainLayout.getScene() != null && mainLayout.getScene().getWindow() instanceof Stage) {
            Stage stage = (Stage) mainLayout.getScene().getWindow();
            stage.sizeToScene();
            stage.setResizable(false);
        }
    }

    // Precarga los métodos del generador si existen (para edición)
    private void precargarMetodosDesdeGenerador() {
        var generador = application.getGenerador();
        if (generador != null && generador.obtenerClase() != null) {
            methods.clear();
            for (var fun : generador.obtenerClase().getFunciones()) {
                String params = fun.getParametros() != null ? String.join(", ", fun.getParametros()) : "";
                // Obtener el cuerpo del método (primer bloque)
                String cuerpo = "";
                String returnValue = "";
                if (fun.getBloques() != null && !fun.getBloques().isEmpty()) {
                    cuerpo = fun.getBloques().get(0).generarCodigo();
                    // Buscar return en el cuerpo
                    java.util.regex.Matcher mRet = java.util.regex.Pattern.compile("return (.+);\\s*").matcher(cuerpo);
                    if (mRet.find()) {
                        returnValue = mRet.group(1).trim();
                    }
                }
                methods.add(new MethodModel(
                    fun.getNombre(),
                    fun.getTipoRetorno(),
                    params,
                    fun.getVisibilidad(),
                    "private".equalsIgnoreCase(fun.getVisibilidad()),
                    false,
                    cuerpo,
                    returnValue
                ));
            }
        }
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
        dialog.initModality(Modality.WINDOW_MODAL);
        StackPane root = new StackPane(form);
        root.setStyle("-fx-background-color: #eaf6fb;");
        root.setPadding(new Insets(32, 0, 32, 0)); // Espacio arriba y abajo
        StackPane.setAlignment(form, Pos.CENTER);
        Scene dialogScene = new Scene(root, 700, 700);
        String css = getClass().getResource("/css/style.css").toExternalForm();
        dialogScene.getStylesheets().add(css);
        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.centerOnScreen();
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
