package Funcional.Funciones;

public class BloqueIf implements BloqueCodigo {
    private String condicion;
    private String cuerpo;

    public BloqueIf(String condicion, String cuerpo) {
        this.condicion = condicion;
        this.cuerpo = cuerpo;
    }

    @Override
    public String generarCodigo() {
        return "if (" + condicion + ") {\n    " + cuerpo + "\n}";
    }
}
