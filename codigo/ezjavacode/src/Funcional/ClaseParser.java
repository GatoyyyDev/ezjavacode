package Funcional;

import Funcional.Funciones.Funcion;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class ClaseParser {
    public static Clase parse(File archivoJava) {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoJava))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (lineas.isEmpty()) return null;

        // Buscar nombre de la clase (ignorando líneas vacías y comentarios)
        String nombreClase = null;
        for (String l : lineas) {
            String lTrim = l.trim();
            if (lTrim.isEmpty() || lTrim.startsWith("//") || lTrim.startsWith("/*") || lTrim.startsWith("*")) continue;
            if (lTrim.contains("public class ")) {
                // Busca el nombre después de 'public class'
                String[] partes = lTrim.split("public class ", 2);
                if (partes.length > 1) {
                    String resto = partes[1].trim();
                    String[] tokens = resto.split("[ {]");
                    if (tokens.length > 0) {
                        nombreClase = tokens[0];
                        break;
                    }
                }
            }
        }
        if (nombreClase == null) return null;
        Clase clase = new Clase(nombreClase);

        // Expresiones regulares para atributos y métodos
        Pattern patronAtributo = Pattern.compile("(private|public)( static)?( final)? ([^ ]+) ([^ ]+)( = (.*))?;");
        Pattern patronMetodo = Pattern.compile("(public|private) (static )?([^ ]+) ([^\\(]+)\\(([^\\)]*)\\) ?\\{");

        boolean enMetodo = false;
        StringBuilder metodoActual = new StringBuilder();
        String firmaMetodo = null;
        for (String l : lineas) {
            if (!enMetodo) {
                Matcher m = patronAtributo.matcher(l);
                if (m.matches()) {
                    // Extraer datos del atributo
                    boolean esPrivado = m.group(1).equals("private");
                    boolean esStatic = m.group(2) != null;
                    boolean esFinal = m.group(3) != null;
                    String tipo = m.group(4);
                    String nombre = m.group(5);
                    String valor = m.group(7) != null ? m.group(7) : "";
                    EnumAtributo enumTipo = tipo.equals("int") ? EnumAtributo.INTEGER :
                        tipo.equals("String") ? EnumAtributo.STRING :
                        tipo.equals("double") ? EnumAtributo.DOUBLE :
                        tipo.equals("boolean") ? EnumAtributo.BOOLEAN : EnumAtributo.STRING;
                    clase.agregarAtributo(new Atributo(nombre, enumTipo, esPrivado, esStatic, esFinal, valor));
                } else {
                    Matcher mm = patronMetodo.matcher(l);
                    if (mm.matches()) {
                        enMetodo = true;
                        metodoActual.setLength(0);
                        firmaMetodo = l;
                        metodoActual.append(l).append("\n");
                    }
                }
            } else {
                metodoActual.append(l).append("\n");
                if (l.equals("}")) {
                    // Fin del método
                    enMetodo = false;
                    // Parsear firma para obtener nombre, tipo y visibilidad
                    Matcher mm = patronMetodo.matcher(firmaMetodo);
                    if (mm.matches()) {
                        String vis = mm.group(1);
                        String tipoRet = mm.group(3);
                        String nombre = mm.group(4);
                        String params = mm.group(5);
                        Funcion fun = new Funcion(nombre, tipoRet, vis);
                        // --- PARSEAR PARÁMETROS ---
                        if (params != null && !params.trim().isEmpty()) {
                            String[] paramArr = params.split(",");
                            for (String param : paramArr) {
                                String[] parts = param.trim().split(" ");
                                if (parts.length == 2) {
                                    fun.agregarParametro(parts[0], parts[1]);
                                }
                            }
                        }
                        // --- GUARDAR CUERPO DEL MÉTODO ---
                        StringBuilder cuerpo = new StringBuilder();
                        boolean cuerpoIniciado = false;
                        for (String linea : metodoActual.toString().split("\\n")) {
                            if (linea.contains("{")) {
                                cuerpoIniciado = true;
                                continue;
                            }
                            if (linea.equals("}")) break;
                            if (cuerpoIniciado) cuerpo.append(linea).append("\n");
                        }
                        // Buscar return simple
                        String returnValue = "";
                        Pattern patronReturn = Pattern.compile("return (.+);\s*");
                        Matcher mRet = patronReturn.matcher(cuerpo.toString());
                        if (mRet.find()) {
                            returnValue = mRet.group(1).trim();
                        }
                        // Asignar el cuerpo y return como bloque de código
                        fun.agregarBloque(new Funcional.Funciones.BloqueCodigo() {
                            @Override
                            public String generarCodigo() {
                                return cuerpo.toString();
                            }
                        });
                        // (Opcional: podrías guardar el returnValue en un campo extendido si lo deseas)
                        clase.agregarFuncion(fun);
                    }
                }
            }
        }
        return clase;
    }
}
