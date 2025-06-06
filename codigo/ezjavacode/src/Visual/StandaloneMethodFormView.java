package Visual;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import java.io.File;

public class StandaloneMethodFormView {
    public static void show(String functionName, Runnable onSave) {
        MethodFormView form = new MethodFormView(functionName == null ? "" : functionName);
        // Si existe la función, cargarla desde funciones_generadas
        if (functionName != null && !functionName.isEmpty()) {
            java.io.File file = new java.io.File(Visual.PathUtils.getJarDir() + "/funciones_generadas/" + functionName + ".java");
            if (file.exists()) {
                try {
                    String code = new String(java.nio.file.Files.readAllBytes(file.toPath()));
                    // Parsear la firma: visibilidad [static] tipo nombre(param1 tipo1, ...)
                    String[] lines = code.split("\\r?\\n");
                    if (lines.length > 0) {
                        String header = lines[0].trim();
                        int parenOpen = header.indexOf('(');
                        int parenClose = header.indexOf(')');
                        if (parenOpen > 0 && parenClose > parenOpen) {
                            // Extraer nombre
                            String beforeParen = header.substring(0, parenOpen).trim();
                            String[] parts = beforeParen.split(" ");
                            // visibilidad [static] tipo nombre
                            String methodName = parts[parts.length - 1];
                            form.getNameField().setText(methodName);
                            // tipo
                            if (parts.length >= 3) form.getReturnTypeCombo().setValue(parts[parts.length - 2]);
                            // visibilidad
                            if (parts.length >= 2) form.getVisibilidadCombo().setValue(parts[0]);
                            // static
                            form.getEstatico().setSelected(header.contains("static"));
                            // parámetros
                            String paramsStr = header.substring(parenOpen + 1, parenClose).trim();
                            form.getParamsTable().getItems().clear();
                            if (!paramsStr.isEmpty()) {
                                String[] paramsArr = paramsStr.split(",");
                                for (String param : paramsArr) {
                                    String[] p = param.trim().split(" ");
                                    if (p.length == 2) {
                                        form.getParamsTable().getItems().add(new ParameterModel(p[0], p[1]));
                                    }
                                }
                            }
                        }
                    }
                    // --- Cargar el cuerpo (resto del código) eliminando el prefijo común solo si TODAS las líneas lo tienen ---
                    java.util.List<String> codeLines = new java.util.ArrayList<>();
                    for (int i = 1; i < lines.length - 1; i++) {
                        String line = lines[i];
                        if (!line.trim().startsWith("return ")) codeLines.add(line);
                    }
                    // Detectar el prefijo común exacto
                    String commonPrefix = null;
                    boolean allHavePrefix = true;
                    for (String l : codeLines) {
                        if (l.trim().isEmpty()) continue;
                        String prefix = l.replaceAll("[^ \t].*", "");
                        if (commonPrefix == null) {
                            commonPrefix = prefix;
                        } else if (!prefix.startsWith(commonPrefix)) {
                            allHavePrefix = false;
                            break;
                        }
                    }
                    StringBuilder body = new StringBuilder();
                    for (String l : codeLines) {
                        if (allHavePrefix && commonPrefix != null && l.startsWith(commonPrefix))
                            body.append(l.substring(commonPrefix.length()));
                        else
                            body.append(l);
                        body.append("\n");
                    }
                    form.getCodeArea().setText(body.toString().trim());
                    // Intentar cargar return
                    for (int i = 1; i < lines.length - 1; i++) {
                        String line = lines[i].trim();
                        if (line.startsWith("return ")) {
                            String ret = line.substring(7);
                            if (ret.endsWith(";")) ret = ret.substring(0, ret.length() - 1);
                            form.getReturnField().setText(ret.trim());
                        }
                    }
                } catch (Exception ignored) {}
            }
        }
        // --- CONTENEDOR BLANCO CON SOMBRA Y BORDE AZUL ---
        VBox whiteBox = new VBox(24, form); // 24px separación vertical
        whiteBox.setAlignment(Pos.CENTER);
        whiteBox.setPadding(new Insets(32, 38, 32, 38));
        whiteBox.setStyle("-fx-background-color: white; -fx-background-radius: 18px; -fx-effect: dropshadow(three-pass-box, #b3e0ff, 8, 0.1, 0, 2);");

        // --- ESCENA Y STAGE ---
        Scene scene = new Scene(whiteBox);
        // Añadir el CSS global para que la cabecera de la tabla sea azul SIEMPRE
        String css = StandaloneMethodFormView.class.getResource("/css/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(functionName == null ? "Nueva función" : "Editar función: " + functionName);
        dialog.setScene(scene);

        // --- HANDLER DEL BOTÓN INCLUIR MÉTODO ---
        form.getIncluirButton().setOnAction(e -> {
            var method = form.getMethodModel();
            if (method == null) return;
            // --- Generar el código de la función con visibilidad correcta ---
            StringBuilder sb = new StringBuilder();
            String vis = method.getVisibilidad();
            sb.append(vis).append(" ");
            if (method.isStatic()) sb.append("static ");
            sb.append(method.getReturnType()).append(" ");
            sb.append(method.getName()).append("(");
            sb.append(method.getParameters()).append(") {\n");
            if (!method.getCode().isBlank()) {
                String[] codeLines = method.getCode().split("\\r?\\n");
                for (String line : codeLines) {
                    sb.append(line).append("\n");
                }
            }
            if (!method.getReturnValue().isBlank()) {
                sb.append("    return ").append(method.getReturnValue()).append(";\n");
            }
            sb.append("}\n");
            String functionCode = sb.toString();
            java.io.File file = new java.io.File(Visual.PathUtils.getJarDir() + "/funciones_generadas/" + method.getName() + ".java");
            try (java.io.FileWriter fw = new java.io.FileWriter(file)) {
                fw.write(functionCode);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            dialog.close();
            if (onSave != null) onSave.run();
        });

        dialog.showAndWait();
        return;
    }
}
