public class Main {
    public static void main(String[] args) {
        GeneradorDeClases generador = new GeneradorDeClases();

        generador.iniciarNuevaClase("Persona");
        generador.agregarAtributo("nombre", EnumAtributo.STRING, false, "Juan");
        generador.agregarAtributo("edad", EnumAtributo.INTEGER, true, "25");
        generador.agregarAtributo("esEstudiante", EnumAtributo.BOOLEAN, false, "true");

        Clase clase = generador.obtenerClase();

        // Ruta relativa o absoluta a la carpeta
        String rutaCarpeta = "clases generadas";
        ExportadorDeClases.guardarClaseComoArchivo(clase, rutaCarpeta);
    }
}
