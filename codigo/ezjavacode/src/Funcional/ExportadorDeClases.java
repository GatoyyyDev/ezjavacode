package Funcional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportadorDeClases {

    public static void guardarClaseComoArchivo(Clase clase, String rutaCarpeta) {
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
}
