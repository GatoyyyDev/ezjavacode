public class Persona {

    private String nombre = "Ruben";
    public int edad = 23;
    public static final boolean ezjavacode = true;
    private static final double altura = 1.85;

    public int calcularNacimiento(int fechaActual) {
        int nacimiento = fechaActual - this.edad;
        return nacimiento;
    }
}
