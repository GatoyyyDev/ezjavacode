public class Atributo {
    private String nombre;
    private EnumAtributo tipo;
    private boolean esPrivado;
    private String valorInicial; // Nuevo campo

    public Atributo(String nombre, EnumAtributo tipo, boolean esPrivado, String valorInicial) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.esPrivado = esPrivado;
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

    public void setValorInicial(String valorInicial) {
        this.valorInicial = valorInicial;
    }

    // Generar código Java del atributo
    public String generarCodigo() {
        String visibilidad = esPrivado ? "private" : "public";
        String tipoJava = tipo.toJavaType();

        if (valorInicial != null && !valorInicial.isEmpty()) {
            return visibilidad + " " + tipoJava + " " + nombre + " = " + formatValorInicial() + ";";
        } else {
            return visibilidad + " " + tipoJava + " " + nombre + ";";
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
