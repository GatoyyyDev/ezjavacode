public class Artista {

    private String nombre = "Valeria";
    public int edad = 33;
    public static final boolean activo = true;
    private static final double obras = 12;

    public int calcularObrasAnuales(int anioActual) {
        int obrasAnuales = (int)(this.obras / (this.edad - 18));
        return obrasAnuales;
    }

    public double Dividir(int divisor, int dividendo) {
        double resultado = divisor / dividendo
        return resultado;
        }
    }

}
