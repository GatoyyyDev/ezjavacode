import java.util.ArrayList;
import java.util.List;

public class Clase {
    private String nombre;
    private List<Atributo> atributos;
    private List<Funcion> funciones;

    public Clase(String nombre) {
        this.nombre = nombre;
        this.atributos = new ArrayList<>();
        this.funciones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Atributo> getAtributos() {
        return atributos;
    }

    public void agregarAtributo(Atributo atributo) {
        atributos.add(atributo);
    }

    public List<Funcion> getFunciones() {
        return funciones;
    }

    public void agregarFuncion(Funcion funcion) {
        funciones.add(funcion);
    }

    // Metodo para generar el código fuente Java de la clase
    public String generarCodigo() {
        StringBuilder sb = new StringBuilder();
        sb.append("public class ").append(nombre).append(" {\n\n");

        // Atributos
        for (Atributo atributo : atributos) {
            sb.append("    ").append(atributo.generarCodigo()).append("\n");
        }

        sb.append("\n");

        // Métodos
        for (Funcion funcion : funciones) {
            sb.append(funcion.generarCodigo()).append("\n");
        }

        sb.append("}\n");
        return sb.toString();
    }
}
