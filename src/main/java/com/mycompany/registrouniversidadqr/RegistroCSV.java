package com.mycompany.registrouniversidadqr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Clase encargada de guardar los registros universitarios en un archivo CSV.
 */
public class RegistroCSV {

    private static final String CARPETA = System.getProperty("user.home")
            + File.separator + "RegistroUniversidadQR"
            + File.separator + "Datos";

    private static final String ARCHIVO = CARPETA
            + File.separator + "registros.csv";

    public void guardarRegistro(Registro registro) throws IOException {
        File carpeta = new File(CARPETA);

        if (!carpeta.exists()) {
            boolean creada = carpeta.mkdirs();

            if (!creada) {
                throw new IOException("No se pudo crear la carpeta: " + CARPETA);
            }
        }

        File archivo = new File(ARCHIVO);
        boolean archivoNuevo = !archivo.exists() || archivo.length() == 0;

        try (BufferedWriter writer = Files.newBufferedWriter(
                archivo.toPath(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            if (archivoNuevo) {
                writer.write("Nombres y Apellidos,Cedula,Fecha,Ingreso,Salida");
                writer.newLine();
            }

            writer.write(registro.convertirCSV());
            writer.newLine();
        }
    }

    public String obtenerRutaArchivo() {
        return ARCHIVO;
    }
}