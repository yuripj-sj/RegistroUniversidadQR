package com.mycompany.registrouniversidadqr;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroCivilService {

    public DatosCedula consultar(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(15000)
                .get();

        String texto = doc.body().text();

        String nombres = extraer(texto, "Nombres:\\s*(.*?)\\s*Apellidos:");
        String apellidos = extraer(texto, "Apellidos:\\s*(.*?)\\s*Fecha nacimiento:");
        String cedula = extraer(texto, "El documento\\s*(\\d+)\\s*se encuentra");

        if (nombres.isEmpty() || apellidos.isEmpty() || cedula.isEmpty()) {
            throw new IOException("No se pudieron extraer nombres, apellidos o cédula desde la página.");
        }

        return new DatosCedula(nombres + " " + apellidos, cedula);
    }

    private String extraer(String texto, String patron) {
        Pattern pattern = Pattern.compile(patron, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(texto);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "";
    }

    public static class DatosCedula {

        private String nombresApellidos;
        private String cedula;

        public DatosCedula(String nombresApellidos, String cedula) {
            this.nombresApellidos = nombresApellidos;
            this.cedula = cedula;
        }

        public String getNombresApellidos() {
            return nombresApellidos;
        }

        public String getCedula() {
            return cedula;
        }
    }
}