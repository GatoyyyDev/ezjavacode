package Funciones;

import java.util.ArrayList;
import java.util.List;
import Funciones.BloqueCodigo;

public class Funcion {
    private String nombre;
    private String tipoRetorno;
    private List<String> parametros; // Lista de parámetros de la función
    private List<BloqueCodigo> bloques; // Lista de bloques de código (modulares)

    public Funcion(String nombre, String tipoRetorno) {
        this.nombre = nombre;
        this.tipoRetorno = tipoRetorno;
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
        sb.append("    public ").append(tipoRetorno).append(" ").append(nombre).append("(");

        // Agregar parámetros
        for (int i = 0; i < parametros.size(); i++) {
            sb.append(parametros.get(i));
            if (i < parametros.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(") {\n");

        // Agregar bloques de código
        for (BloqueCodigo bloque : bloques) {
            sb.append("        ").append(bloque.generarCodigo()).append("\n");
        }

        sb.append("    }\n");
        return sb.toString();
    }
}
