import java.util.ArrayList;
import java.util.List;

public class GeneradorDeClases {
    private Clase claseActual;

    public void iniciarNuevaClase(String nombreClase) {
        claseActual = new Clase(nombreClase);
    }

    public void agregarAtributo(String nombre, EnumAtributo tipo, boolean esPrivado, String valorInicial) {
        if (claseActual == null) {
            throw new IllegalStateException("No se ha iniciado ninguna clase.");
        }
        Atributo atributo = new Atributo(nombre, tipo, esPrivado, valorInicial);
        claseActual.agregarAtributo(atributo);
    }


    public Clase obtenerClase() {
        return claseActual;
    }

    public String generarCodigoClase() {
        if (claseActual == null) {
            throw new IllegalStateException("No se ha creado ninguna clase.");
        }
        return claseActual.generarCodigo();
    }

    public void reiniciar() {
        claseActual = null;
    }
}

