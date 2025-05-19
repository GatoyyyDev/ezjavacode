package Funcional;

public class Atributo {
    private String nombre;
    private EnumAtributo tipo;
    private boolean esPrivado;
    private boolean esStatic;
    private boolean esFinal;
    private String valorInicial; // Nuevo campo

    public Atributo(String nombre, EnumAtributo tipo, boolean esPrivado, boolean esStatic, boolean esFinal, String valorInicial) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.esPrivado = esPrivado;
        this.esStatic = esStatic;
        this.esFinal = esFinal;
        this.valorInicial = valorInicial;
    }

    public String getNombre() {
        return nombre;
    }

    public EnumAtributo getTipo() {
        return tipo;
    }

    public boolean isEsPrivado() {
        return esPrivado;
    }

    public boolean isEsStatic() {
        return esStatic;
    }

    public boolean isEsFinal() {
        return esFinal;
    }

    public String getValorInicial() {
        return valorInicial;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(EnumAtributo tipo) {
        this.tipo = tipo;
    }

    public void setEsPrivado(boolean esPrivado) {
        this.esPrivado = esPrivado;
    }

    public void setEsStatic(boolean esStatic) {
        this.esStatic = esStatic;
    }

    public void setEsFinal(boolean esFinal) {
        this.esFinal = esFinal;
    }

    public void setValorInicial(String valorInicial) {
        this.valorInicial = valorInicial;
    }

    // Generar código Java del atributo
    public String generarCodigo() {
        StringBuilder vis = new StringBuilder();
        vis.append(esPrivado ? "private" : "public");
        if (esStatic) vis.append(" static");
        if (esFinal) vis.append(" final");
        String tipoJava = tipo.toJavaType();

        if (valorInicial != null && !valorInicial.isEmpty()) {
            return vis + " " + tipoJava + " " + nombre + " = " + formatValorInicial() + ";";
        } else {
            return vis + " " + tipoJava + " " + nombre + ";";
        }
    }

    // Para formatear bien los valores según el tipo
    private String formatValorInicial() {
        switch (tipo) {
            case STRING:
                return "\"" + valorInicial + "\"";
            case BOOLEAN:
                return valorInicial.toLowerCase();
            case INTEGER:
            case DOUBLE:
            default:
                return valorInicial;
        }
    }
}
