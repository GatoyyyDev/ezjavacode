public class Profesor {

    private String nombre = "Laura";
    public int edad = 45;
    public static final boolean titular = true;
    private static final double experiencia = 15.5;

    public int calcularJubilacion(int anioActual) {
        int jubilacion = anioActual + (65 - this.edad);
        return jubilacion;
    }
}
