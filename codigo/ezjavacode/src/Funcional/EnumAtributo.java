package Funcional;

public enum EnumAtributo {
    STRING("String"),
    INTEGER("int"),
    DOUBLE("double"),
    BOOLEAN("boolean");

    private final String javaType;

    EnumAtributo(String javaType) {
        this.javaType = javaType;
    }

    public String toJavaType() {
        return javaType;
    }
}
