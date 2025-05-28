package Funcional.Funciones;

import java.util.ArrayList;
import java.util.List;
import Funcional.Funciones.BloqueCodigo;

public class Funcion {
    private String nombre;
    private String tipoRetorno;
    private String visibilidad = "public";
    private List<String> parametros; // Lista de parámetros de la función
    private List<BloqueCodigo> bloques; // Lista de bloques de código (modulares)

    public Funcion(String nombre, String tipoRetorno, String visibilidad) {
        this.nombre = nombre;
        this.tipoRetorno = tipoRetorno;
        this.visibilidad = visibilidad;
        this.parametros = new ArrayList<>();
        this.bloques = new ArrayList<>();
    }

    // Agregar parámetro a la función
    public void agregarParametro(String tipo, String nombre) {
        parametros.add(tipo + " " + nombre);
    }

    // Agregar un bloque de código
    public void agregarBloque(BloqueCodigo bloque) {
        bloques.add(bloque);
    }

    // Método para generar el código de la función
    public String generarCodigo() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(visibilidad).append(" ").append(tipoRetorno).append(" ").append(nombre).append("(");

        // Agregar parámetros
        for (int i = 0; i < parametros.size(); i++) {
            sb.append(parametros.get(i));
            if (i < parametros.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(") {\n");

        // Agregar bloques de código con indentación por línea
        for (BloqueCodigo bloque : bloques) {
            String bloqueCodigo = bloque.generarCodigo();
            String[] lineas = bloqueCodigo.split("\\r?\\n");
            for (String linea : lineas) {
                sb.append("        ").append(linea).append("\n");
            }
        }

        sb.append("    }\n");
        return sb.toString();
    }

    public String getNombre() {
        return nombre;
    }

    public void setVisibilidad(String visibilidad) {
        this.visibilidad = visibilidad;
    }

    public String getVisibilidad() {
        return visibilidad;
    }

    // Devuelve la lista de parámetros
    public List<String> getParametros() {
        return parametros;
    }

    // Devuelve el tipo de retorno de la función
    public String getTipoRetorno() {
        return tipoRetorno;
    }

    // Devuelve la lista de bloques de código
    public List<BloqueCodigo> getBloques() {
        return bloques;
    }
}
