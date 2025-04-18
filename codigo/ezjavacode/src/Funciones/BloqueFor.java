package Funciones;

public class BloqueFor implements BloqueCodigo {
    private String inicializacion;
    private String condicion;
    private String incremento;
    private String cuerpo;

    public BloqueFor(String inicializacion, String condicion, String incremento, String cuerpo) {
        this.inicializacion = inicializacion;
        this.condicion = condicion;
        this.incremento = incremento;
        this.cuerpo = cuerpo;
    }

    @Override
    public String generarCodigo() {
        return "for (" + inicializacion + "; " + condicion + "; " + incremento + ") {\n    " + cuerpo + "\n}";
    }
}
