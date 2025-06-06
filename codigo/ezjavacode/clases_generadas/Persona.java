public class Persona {

    private final int edad = 23;
    private final double altura = 1.85;
    private static final String genero = "masculino";
    public boolean ezjavacode = true;

    public int calcularEdad(LocalDate fechaNacimiento) {
         LocalDate hoy = LocalDate.now();
        return Period.between(fechaNacimiento, hoy).getYears();;
    }

}
