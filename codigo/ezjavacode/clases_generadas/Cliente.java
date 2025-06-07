public class Cliente {

    private String nombre = "Miguel";
    public int edad = 50;
    public static final boolean frecuente = true;
    private static final double compras = 1500.5;

    public int calcularDescuento(int anioActual) {
        int descuento = (int)(this.compras / 100);
        return descuento;
    }
}
