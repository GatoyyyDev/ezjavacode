package Funcional;

import java.util.ArrayList;
import java.util.List;

public class GeneradorDeClases {
    private Clase claseActual;

    public void iniciarNuevaClase(String nombreClase) {
        claseActual = new Clase(nombreClase);
    }

    public void agregarAtributo(String nombre, EnumAtributo tipo, boolean esPrivado, boolean esStatic, boolean esFinal, String valorInicial) {
        if (claseActual == null) {
            throw new IllegalStateException("No se ha iniciado ninguna clase.");
        }
        Atributo atributo = new Atributo(nombre, tipo, esPrivado, esStatic, esFinal, valorInicial);
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

    // Permite cargar una clase ya existente en el generador
    public void setClase(Clase clase) {
        this.claseActual = clase;
    }
}
