package com.mycompany.registrouniversidadqr;

/**
 * Representa el registro de una persona dentro del sistema universitario.
 */
public class Registro {

    private String nombresApellidos;
    private String cedula;
    private String fecha;
    private String ingreso;
    private String salida;

    /**
     * Constructor de la clase Registro.
     *
     * @param nombresApellidos nombres y apellidos de la persona
     * @param cedula número de cédula
     * @param fecha fecha del registro
     * @param ingreso hora de ingreso
     * @param salida hora de salida
     */
    public Registro(String nombresApellidos, String cedula, String fecha, String ingreso, String salida) {
        this.nombresApellidos = nombresApellidos;
        this.cedula = cedula;
        this.fecha = fecha;
        this.ingreso = ingreso;
        this.salida = salida;
    }

    /**
     * Convierte el registro a formato CSV.
     *
     * @return línea CSV
     */
    public String convertirCSV() {
        return escaparCSV(nombresApellidos) + ","
                + escaparCSV(cedula) + ","
                + escaparCSV(fecha) + ","
                + escaparCSV(ingreso) + ","
                + escaparCSV(salida);
    }

    private String escaparCSV(String valor) {
        if (valor == null) {
            return "";
        }

        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            valor = valor.replace("\"", "\"\"");
            return "\"" + valor + "\"";
        }

        return valor;
    }
}