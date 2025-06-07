public class Empleado {

    private String nombre = "Carlos";
    public int edad = 30;
    public static final boolean activo = false;
    private static final double salario = 2500.0;

    public int calcularAntiguedad(int anioActual) {
        int antiguedad = anioActual - this.edad + 22;
        return antiguedad;
    }
}
