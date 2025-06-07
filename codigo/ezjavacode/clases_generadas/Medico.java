public class Medico {

    private String nombre = "Sofia";
    public int edad = 38;
    public static final boolean enGuardia = true;
    private static final double especialidad = 2.0;

    public int calcularAniosExperiencia(int anioActual) {
        int experiencia = anioActual - (this.edad - 24);
        return experiencia;
    }
}
