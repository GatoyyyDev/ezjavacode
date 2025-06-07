public class Ingeniero {

    private String nombre = "Luis";
    public int edad = 28;
    public static final boolean colegiado = false;
    private static final double proyectos = 5;

    public int calcularProyectos(int anioActual) {
        int proyectosRealizados = anioActual - (2020 - 2);
        return proyectosRealizados;
    }
}
