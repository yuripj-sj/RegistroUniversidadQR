package com.mycompany.registrouniversidadqr;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Ventana principal del sistema de registro universitario.
 */
public class VentanaRegistro extends JFrame {

    private JTextField txtNombres;
    private JTextField txtCedula;
    private JTextField txtQR;
    private RegistroCSV registroCSV;

    public VentanaRegistro() {
        registroCSV = new RegistroCSV();

        setTitle("Sistema de Registro Universitario QR");
        setSize(650, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        crearInterfaz();
    }

    private void crearInterfaz() {
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(new Color(245, 247, 250));

        JPanel encabezado = new JPanel();
        encabezado.setBackground(new Color(25, 80, 150));
        encabezado.setPreferredSize(new Dimension(650, 90));
        encabezado.setLayout(new GridLayout(2, 1));

        JLabel titulo = new JLabel("REGISTRO UNIVERSITARIO", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel subtitulo = new JLabel("Control de ingreso y salida mediante código QR", SwingConstants.CENTER);
        subtitulo.setForeground(new Color(220, 230, 245));
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        encabezado.add(titulo);
        encabezado.add(subtitulo);

        JPanel contenedor = new JPanel();
        contenedor.setBackground(new Color(245, 247, 250));
        contenedor.setBorder(BorderFactory.createEmptyBorder(30, 55, 30, 55));
        contenedor.setLayout(new BorderLayout());

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblQR = crearEtiqueta("Lectura QR / Cédula:");
        txtQR = crearCampoTexto();

        JLabel lblNombres = crearEtiqueta("Nombres y apellidos:");
        txtNombres = crearCampoTexto();

        JLabel lblCedula = crearEtiqueta("Cédula:");
        txtCedula = crearCampoTexto();

        agregarFila(formulario, gbc, 0, lblQR, txtQR);
        agregarFila(formulario, gbc, 1, lblNombres, txtNombres);
        agregarFila(formulario, gbc, 2, lblCedula, txtCedula);

        JButton btnIngreso = crearBoton("Registrar Ingreso", new Color(35, 130, 80));
        JButton btnSalida = crearBoton("Registrar Salida", new Color(190, 70, 70));
        JButton btnLimpiar = crearBoton("Limpiar Campos", new Color(90, 100, 115));

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 15, 15));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        panelBotones.add(btnIngreso);
        panelBotones.add(btnSalida);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formulario.add(panelBotones, gbc);

        JLabel nota = new JLabel("Formato QR recomendado: Nombres Apellidos;Cédula", SwingConstants.CENTER);
        nota.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nota.setForeground(new Color(100, 100, 100));

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formulario.add(nota, gbc);

        contenedor.add(formulario, BorderLayout.CENTER);

        fondo.add(encabezado, BorderLayout.NORTH);
        fondo.add(contenedor, BorderLayout.CENTER);

        add(fondo);

        btnIngreso.addActionListener(e -> registrar("INGRESO"));
        btnSalida.addActionListener(e -> registrar("SALIDA"));
        btnLimpiar.addActionListener(e -> limpiarCampos());

        txtQR.addActionListener(e -> procesarQR());
        txtQR.requestFocus();
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campo.setPreferredSize(new Dimension(280, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 200, 210)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return campo;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        return boton;
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila, JLabel label, JTextField campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.35;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        panel.add(campo, gbc);
    }

    private void procesarQR() {
        String datosQR = txtQR.getText().trim();

        if (datosQR.contains(";")) {
            String[] datos = datosQR.split(";");

            if (datos.length >= 2) {
                txtNombres.setText(datos[0].trim());
                txtCedula.setText(datos[1].trim());
            }
        } else {
            txtCedula.setText(datosQR);
        }
    }

    private void registrar(String tipoRegistro) {
        String nombres = txtNombres.getText().trim();
        String cedula = txtCedula.getText().trim();

        if (nombres.isEmpty() || cedula.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar los nombres y la cédula.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        String ingreso = "";
        String salida = "";

        if (tipoRegistro.equals("INGRESO")) {
            ingreso = hora;
        } else {
            salida = hora;
        }

        Registro registro = new Registro(nombres, cedula, fecha, ingreso, salida);

        try {
            registroCSV.guardarRegistro(registro);
            JOptionPane.showMessageDialog(
                    this,
                    tipoRegistro + " registrado correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            limpiarCampos();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar el registro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarCampos() {
        txtQR.setText("");
        txtNombres.setText("");
        txtCedula.setText("");
        txtQR.requestFocus();
    }
}
