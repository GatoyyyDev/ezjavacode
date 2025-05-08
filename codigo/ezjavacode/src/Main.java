import Funciones.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Crear el generador de clases
        GeneradorDeClases generador = new GeneradorDeClases();

        // Iniciar la nueva clase Persona
        generador.iniciarNuevaClase("Persona");

        // Agregar el atributo 'nombre' de tipo String
        generador.agregarAtributo("nombre", EnumAtributo.STRING, true, "Juana");  // Asignamos un valor por defecto

        // Agregar una función que imprima el nombre
        // Primero, crear la función imprimirNombre
        Funcion imprimirNombre = new Funcion("imprimirNombre", "void");

        String nombre = "nombre";
        // Crear el bloque de imprimir correctamente refiriéndose al atributo 'nombre'
        imprimirNombre.agregarBloque(new BloqueImprimir(nombre));



        // Agregar la función a la clase
        generador.obtenerClase().agregarFuncion(imprimirNombre);


        // Obtener la clase generada
        Clase claseGenerada = generador.obtenerClase();

        // Usar ExportadorDeClases para guardar la clase en un archivo
        String rutaCarpeta = "clases_generadas";  // Puedes modificar la ruta si lo deseas
        ExportadorDeClases.guardarClaseComoArchivo(claseGenerada, rutaCarpeta);
    }
}


