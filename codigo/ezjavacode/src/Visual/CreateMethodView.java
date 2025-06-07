package Visual;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;
import java.nio.file.Files;

public class CreateMethodView {
    private LogoBackgroundPane mainLayout;
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
        mainLayout = new LogoBackgroundPane();
        BorderPane contentPane = new BorderPane();
        contentPane.setStyle("-fx-background-color: linear-gradient(to bottom, #eaf6fb 0%, #b3e0ff 100%) !important; background: linear-gradient(to bottom, #eaf6fb 0%, #b3e0ff 100%) !important;");
        contentPane.setPadding(new Insets(20));
        // Elimina la línea que fuerza transparencia
        // contentPane.setStyle("-fx-background-color: transparent;");

        // Asegura que la hoja de estilos se aplique SOLO si no está ya aplicada
        // (Elimina la declaración duplicada de 'css')
        // Si ya existe, no la vuelve a añadir
        String css = getClass().getResource("/css/style.css").toExternalForm();
        if (!contentPane.getStylesheets().contains(css)) {
            contentPane.getStylesheets().add(css);
        }

        // Título
        Label titleLabel = new Label("Métodos de " + className);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.getStyleClass().add("view-title");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 20, 0));

        // Separa más el botón de añadir de librería del título
        Region spacer = new Region();
        spacer.setMinHeight(32);
        spacer.setPrefHeight(32);
        spacer.setMaxHeight(32);
        // Asegura que el spacer no expanda y no colapse el layout
        VBox.setVgrow(spacer, Priority.NEVER);

        // Botón para añadir método de la librería
        Button addFromLibraryBtn = new Button("➕ Añadir método de la librería ➕");
        addFromLibraryBtn.getStyleClass().add("menu-button");
        addFromLibraryBtn.setPrefWidth(320);
        addFromLibraryBtn.setMinHeight(44);
        addFromLibraryBtn.setMaxHeight(48);
        addFromLibraryBtn.setFocusTraversable(false);
        addFromLibraryBtn.setPickOnBounds(true);
        addFromLibraryBtn.setOnAction(e -> {
            // Mostrar ventana con lista de funciones generadas
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Añadir método de la librería");
            dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            dialog.getDialogPane().getStyleClass().add("form-panel");
            // Label fuera del recuadro blanco
            Label label = new Label("Selecciona un método para añadirlo a tu clase");
            label.getStyleClass().add("subtitle-label");
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #1565c0; -fx-padding: 0 0 18 0;");
            VBox dialogVBox = new VBox(0);
            dialogVBox.setAlignment(Pos.TOP_CENTER);
            dialogVBox.setStyle("");
            dialogVBox.getChildren().add(label);
            // Contenedor blanco solo para la lista de métodos
            VBox contentWrapper = new VBox();
            contentWrapper.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 14px; -fx-padding: 18 36 18 36; -fx-effect: dropshadow(three-pass-box, #e3e3e3, 8, 0.08, 0, 2);");
            VBox content = new VBox(18);
            content.setAlignment(Pos.CENTER_LEFT);
            // Elimina padding del content, solo en el wrapper
            File dir = new File("funciones_generadas");
            File[] archivos = dir.listFiles((d, name) -> name.endsWith(".java"));

            if (archivos.length == 0) {
                Label noFiles = new Label("No hay funciones disponibles en la biblioteca.");
                noFiles.setStyle("-fx-text-fill: #b71c1c; -fx-font-size: 16px; -fx-font-weight: bold;");
                content.getChildren().add(noFiles);
            } else {
                for (File archivo : archivos) {
                    HBox fila = new HBox();
                    fila.setAlignment(Pos.CENTER_LEFT);
                    fila.setSpacing(0);
                    Region filaSpacer = new Region();
                    HBox.setHgrow(filaSpacer, Priority.ALWAYS);
                    Label nombre = new Label(archivo.getName());
                    nombre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1565c0; -fx-cursor: hand;");
                    Button agregar = new Button("Agregar");
                    agregar.getStyleClass().add("menu-button");
                    agregar.setStyle("-fx-font-size: 14px; -fx-min-width: 88px; -fx-padding: 6 18 6 18;");
                    agregar.setOnAction(ev -> {
                        // Leer el archivo seleccionado
                        try {
                            List<String> lines = java.nio.file.Files.readAllLines(archivo.toPath());
                            StringBuilder codeBuilder = new StringBuilder();
                            String methodName = archivo.getName().replace(".java", "");
                            String returnType = "void";
                            String params = "";
                            String visibilidad = "public";
                            boolean isPrivate = false;
                            boolean isStatic = false;
                            String returnValue = "";

                            // Buscar firma y cuerpo (muy simple)
                            for (String line : lines) {
                                String l = line.trim();
                                if (l.startsWith("public ") || l.startsWith("private ") || l.startsWith("protected ") || l.startsWith("static ") || l.startsWith("void ") || l.contains("(") && l.contains(")")) {
                                    int paren1 = l.indexOf('(');
                                    int paren2 = l.indexOf(')');
                                    if (paren1 > 0 && paren2 > paren1) {
                                        String beforeParen = l.substring(0, paren1).trim();
                                        String[] parts = beforeParen.split(" ");
                                        if (parts.length >= 2) {
                                            returnType = parts[parts.length-2];
                                            methodName = parts[parts.length-1];
                                        }
                                        params = l.substring(paren1+1, paren2).trim();
                                    }
                                    if (l.startsWith("private")) { visibilidad = "private"; isPrivate = true; }
                                    if (l.contains("static")) isStatic = true;
                                } else if (!l.isEmpty()) {
                                    codeBuilder.append(l).append("\n");
                                    if (l.startsWith("return ")) {
                                        returnValue = l.substring(7, l.length()-1).trim();
                                    }
                                }
                            }
                            final String paramsFinal = params;
                            final String methodNameFinal = methodName;
                            // --- Comprobar si ya existe un método con el mismo nombre y parámetros ---
                            boolean exists = methods.stream().anyMatch(m -> m.getName().equals(methodNameFinal) && m.getParameters().equals(paramsFinal));
                            if (exists) {
                                new Alert(Alert.AlertType.WARNING, "Ya existe un método con ese nombre y parámetros.").showAndWait();
                                return;
                            }
                            MethodModel nuevo = new MethodModel(methodNameFinal, returnType, paramsFinal, visibilidad, isPrivate, isStatic, codeBuilder.toString(), returnValue);
                            methods.add(nuevo);
                            methodTable.refresh();

                            // --- Añadir a la clase real para exportación ---
                            var clase = application.getGenerador().obtenerClase();
                            boolean existsInClass = clase.getFunciones().stream().anyMatch(f -> f.getNombre().equals(methodNameFinal) && String.join(", ", f.getParametros()).equals(paramsFinal));
                            if (!existsInClass) {
                                Funcional.Funciones.Funcion funcion = new Funcional.Funciones.Funcion(methodNameFinal, returnType, visibilidad);
                                // Añadir parámetros
                                if (!paramsFinal.isEmpty()) {
                                    for (String param : paramsFinal.split(",")) {
                                        String[] p = param.trim().split(" ");
                                        if (p.length == 2) funcion.agregarParametro(p[0], p[1]);
                                    }
                                }
                                // Añadir cuerpo como bloque
                                String bloqueStr = codeBuilder.toString();
                                Funcional.Funciones.BloqueCodigo bloqueCodigo = new Funcional.Funciones.BloqueCodigo() {
                                    @Override
                                    public String generarCodigo() {
                                        return bloqueStr;
                                    }
                                };
                                funcion.agregarBloque(bloqueCodigo);
                                clase.agregarFuncion(funcion);
                            }
                        } catch (Exception ex) {
                            new Alert(Alert.AlertType.ERROR, "No se pudo importar el método: " + ex.getMessage()).showAndWait();
                        }
                        dialog.close();
                    });
                    fila.getChildren().addAll(nombre, filaSpacer, agregar);
                    content.getChildren().add(fila);
                }
            }
            contentWrapper.getChildren().add(content);
            dialogVBox.getChildren().add(contentWrapper);
            ButtonType cerrarBtn = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().add(cerrarBtn);
            // Aplica el estilo al botón de cerrar después de crearlo
            Button cerrarButton = (Button) dialog.getDialogPane().lookupButton(cerrarBtn);
            if (cerrarButton != null) {
                cerrarButton.getStyleClass().add("menu-button");
                cerrarButton.setStyle("-fx-font-size: 15px; -fx-min-width: 92px; -fx-padding: 8 24 8 24;");
            }
            dialog.getDialogPane().setContent(dialogVBox);
            dialog.showAndWait();
        });
        // Espacio entre el botón de librería y la cabecera
        VBox topBox = new VBox(0, titleLabel, spacer, addFromLibraryBtn);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(0, 0, -20, 0)); // Solo espacio inferior
        VBox.setMargin(addFromLibraryBtn, new Insets(0, 0, 0, 0)); // Sin margen extra sobre el botón
        contentPane.setTop(topBox);

        // Tabla de métodos
        methodTable = new TableView<>();
        // Ajusta la tabla de métodos a un tamaño intermedio
        methodTable.setPrefHeight(260);
        methodTable.setMinHeight(170);
        methodTable.setMaxHeight(320);
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

        // Añadir logo semitransparente entre el fondo y la tabla, SIN modificar la tabla
        ImageView bgImage = new ImageView(new Image(getClass().getResourceAsStream("/Visual/logo.png")));
        bgImage.setPreserveRatio(true);
        bgImage.setOpacity(0.13);
        bgImage.setFitWidth(600);
        bgImage.setFitHeight(600);
        bgImage.setSmooth(true);
        bgImage.setCache(true);
        StackPane stack = new StackPane();
        stack.getChildren().addAll(bgImage, methodTable); // logo detrás, tabla delante

        // Menos espacio debajo de la tabla
        VBox centerBox = new VBox(stack);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setPadding(new Insets(-24, 0, -24, 0)); // Padding negativo fuerte
        contentPane.setCenter(centerBox);

        // --- Botones alineados en una sola línea ---
        Button backBtn = new Button("\uD83E\uDC80 Volver");
        backBtn.getStyleClass().add("back-button");
        backBtn.setPrefWidth(140);
        backBtn.setOnAction(e -> application.showCreateAttributeView(className));

        Button addMethodBtn = new Button("➕ Añadir Método nuevo ➕");
        addMethodBtn.getStyleClass().add("menu-button");
        addMethodBtn.setPrefWidth(280);
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
            String cssDialog = getClass().getResource("/css/style.css").toExternalForm();
            if (!dialogScene.getStylesheets().contains(cssDialog)) {
                dialogScene.getStylesheets().add(cssDialog);
            }
            dialog.setScene(dialogScene);
            dialog.setResizable(false);
            dialog.centerOnScreen();
            // Al guardar el método
            form.getIncluirButton().setOnAction(ev -> {
                MethodModel m = form.getMethodModel();
                if (m != null) {
                    // --- Comprobar si ya existe un método con el mismo nombre y parámetros ---
                    boolean exists = methods.stream().anyMatch(m2 -> m2.getName().equals(m.getName()) && m2.getParameters().equals(m.getParameters()));
                    if (exists) {
                        new Alert(Alert.AlertType.WARNING, "Ya existe un método con ese nombre y parámetros.").showAndWait();
                        return;
                    }
                    methods.add(m);
                    var generador = application.getGenerador();
                    if (generador != null) {
                        var clase = generador.obtenerClase();
                        boolean existsInClass = clase.getFunciones().stream().anyMatch(f -> f.getNombre().equals(m.getName()) && String.join(", ", f.getParametros()).equals(m.getParameters()));
                        if (!existsInClass) {
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
                    }
                    dialog.close();
                }
            });
            dialog.showAndWait();
        });

        Button exportBtn = new Button("✓ Exportar " + className +" ✓");
        exportBtn.getStyleClass().add("menu-button");
        exportBtn.setPrefWidth(280);
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

        // Añadir contenido al layout principal
        contentPane.setBottom(bottomBox);
        mainLayout.setContent(contentPane);
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
        // form.getPrivado().setSelected(m.isPrivate()); // Elimina esta línea, ya no hay checkbox de privado
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
        String cssDialog2 = getClass().getResource("/css/style.css").toExternalForm();
        if (!dialogScene.getStylesheets().contains(cssDialog2)) {
            dialogScene.getStylesheets().add(cssDialog2);
        }
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

    public StackPane getView() {
        return mainLayout;
    }
}
