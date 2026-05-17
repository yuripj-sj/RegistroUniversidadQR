package com.mycompany.registrouniversidadqr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Clase encargada de guardar los registros universitarios en un archivo CSV.
 */
public class RegistroCSV {

    private static final String ARCHIVO = "registros.csv";

    /**
     * Guarda un registro en el archivo CSV.
     *
     * @param registro registro que se desea guardar
     * @throws IOException si ocurre un error al escribir el archivo
     */
    public void guardarRegistro(Registro registro) throws IOException {
        File archivo = new File(ARCHIVO);
        boolean archivoNuevo = !archivo.exists() || archivo.length() == 0;

        try (FileWriter writer = new FileWriter(archivo, true)) {

            if (archivoNuevo) {
                writer.write("Nombres y Apellidos,Cedula,Fecha,Ingreso,Salida\n");
            }

            writer.write(registro.convertirCSV() + "\n");
        }
    }
}