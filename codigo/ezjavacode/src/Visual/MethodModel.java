package Visual;

public class MethodModel {
    private String name;
    private String returnType;
    private String parameters;
    private String visibilidad;
    private boolean isPrivate;
    private boolean isStatic;
    private String code;
    private String returnValue;

    public MethodModel(String name, String returnType, String parameters, String visibilidad, boolean isPrivate, boolean isStatic, String code, String returnValue) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.visibilidad = visibilidad;
        this.isPrivate = isPrivate;
        this.isStatic = isStatic;
        this.code = code;
        this.returnValue = returnValue;
    }

    public String getName() { return name; }
    public String getReturnType() { return returnType; }
    public String getParameters() { return parameters; }
    public String getVisibilidad() { return visibilidad; }
    public boolean isPrivate() { return isPrivate; }
    public boolean isStatic() { return isStatic; }
    public String getCode() { return code; }
    public String getReturnValue() { return returnValue; }

    public void setName(String name) { this.name = name; }
    public void setReturnType(String returnType) { this.returnType = returnType; }
    public void setParameters(String parameters) { this.parameters = parameters; }
    public void setVisibilidad(String visibilidad) { this.visibilidad = visibilidad; }
    public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
    public void setStatic(boolean isStatic) { this.isStatic = isStatic; }
    public void setCode(String code) { this.code = code; }
    public void setReturnValue(String returnValue) { this.returnValue = returnValue; }
}
