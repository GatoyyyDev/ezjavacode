package Funcional.Funciones;

public class BloqueImprimir implements BloqueCodigo {
    private String mensaje;

    public BloqueImprimir(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String generarCodigo() {
        return "System.out.println(\"" + mensaje + "\");";
    }
}
