package com.mycompany.registrouniversidadqr;

/**
 * Representa el registro de una persona dentro del sistema universitario.
 * Contiene nombres, cédula, fecha, hora de ingreso y hora de salida.
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
     * @param cedula número de cédula de la persona
     * @param fecha fecha del registro en formato año/mes/día
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
     * Convierte los datos del registro en una línea CSV.
     *
     * @return línea de texto en formato CSV
     */
    public String convertirCSV() {
        return nombresApellidos + "," + cedula + "," + fecha + "," + ingreso + "," + salida;
    }
}