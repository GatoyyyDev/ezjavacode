Persona.javapublic class Persona {

    private static int edad = 23;
    private final String altura = "1.85";
    public final String genero = "masculino";
    private static final boolean ezjavacode = true;

    public void CalcularEdad(int edad) {
        int anioActual = Year.now().getValue();
                int anioNacimiento = anioActual - edad;
        
                System.out.println("Tienes " + edad + " años, por lo tanto naciste en el año " + anioNacimiento + ".");
    }

}
