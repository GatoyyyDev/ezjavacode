public class Estudiante {

    private String nombre = "Ana";
    public int edad = 20;
    public static final boolean matriculado = true;
    private static final double promedio = 8.7;

    public int calcularIngreso(int anioActual) {
        int ingreso = anioActual - this.edad + 6;
        return ingreso;
    }
}
