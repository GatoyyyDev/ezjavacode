package Funcional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExportadorDeClases {

    public static void guardarClaseComoArchivo(Clase clase, String rutaCarpeta) {
        if (rutaCarpeta == null || rutaCarpeta.isEmpty()) {
            rutaCarpeta = leerRutaExportacion();
        }
        String codigo = clase.generarCodigo();
        String nombreArchivo = clase.getNombre() + ".java";

        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists()) {
            carpeta.mkdirs(); // Crea la carpeta si no existe
        }

        File archivo = new File(carpeta, nombreArchivo);

        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write(codigo);
            System.out.println("Clase guardada en: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    public static String leerRutaExportacion() {
        String defaultPath = "clases_generadas";
        try {
            String path = new String(Files.readAllBytes(Paths.get("export_path.txt"))).trim();
            if (!path.isEmpty()) {
                return path;
            }
        } catch (IOException e) {
            // Si hay error, usar por defecto
        }
        return defaultPath;
    }
}
