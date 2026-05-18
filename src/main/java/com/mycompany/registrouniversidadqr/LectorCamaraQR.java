package com.mycompany.registrouniversidadqr;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class LectorCamaraQR extends JDialog {

    private Webcam webcam;
    private Timer timer;
    private Consumer<String> accionQRDetectado;

    private JLabel lblEstado;
    private JLabel lblResultado;
    private OverlayQR overlayQR;
    private boolean qrDetectado = false;

    public LectorCamaraQR(JFrame parent, Consumer<String> accionQRDetectado) {
        super(parent, "Escáner QR por Cámara", true);
        this.accionQRDetectado = accionQRDetectado;

        setSize(760, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        iniciarCamara();
    }

    private void iniciarCamara() {
        webcam = Webcam.getDefault();

        if (webcam == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró ninguna cámara conectada.",
                    "Cámara no disponible",
                    JOptionPane.ERROR_MESSAGE
            );
            dispose();
            return;
        }

        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();

        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));
        panelSuperior.setBackground(new Color(25, 80, 150));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("LECTOR QR ACTIVO", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        lblEstado = new JLabel("Coloque el QR dentro del recuadro verde", SwingConstants.CENTER);
        lblEstado.setForeground(new Color(220, 235, 255));
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panelSuperior.add(titulo);
        panelSuperior.add(lblEstado);

        WebcamPanel panelCamara = new WebcamPanel(webcam);
        panelCamara.setFPSDisplayed(true);
        panelCamara.setMirrored(false);

        JLayeredPane capaCamara = new JLayeredPane();
        capaCamara.setPreferredSize(new Dimension(640, 480));

        panelCamara.setBounds(0, 0, 640, 480);

        overlayQR = new OverlayQR();
        overlayQR.setBounds(0, 0, 640, 480);
        overlayQR.setOpaque(false);

        capaCamara.add(panelCamara, Integer.valueOf(0));
        capaCamara.add(overlayQR, Integer.valueOf(1));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panelInferior.setBackground(Color.WHITE);

        lblResultado = new JLabel("Buscando código QR...", SwingConstants.CENTER);
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblResultado.setForeground(new Color(70, 70, 70));

        JButton btnCerrar = new JButton("Cerrar cámara");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.addActionListener(e -> cerrarCamara());

        panelInferior.add(lblResultado, BorderLayout.CENTER);
        panelInferior.add(btnCerrar, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);
        add(capaCamara, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        timer = new Timer(600, e -> leerQR());
        timer.start();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cerrarCamara();
            }
        });
    }

    private void leerQR() {
        if (qrDetectado || webcam == null || !webcam.isOpen()) {
            return;
        }

        lblEstado.setText("Escaneando... coloque el QR dentro del recuadro.");
        lblResultado.setText("Buscando código QR...");

        BufferedImage imagen = webcam.getImage();

        if (imagen == null) {
            return;
        }

        try {
            BinaryBitmap bitmap = new BinaryBitmap(
                    new HybridBinarizer(
                            new BufferedImageLuminanceSource(imagen)
                    )
            );

            Result resultado = new MultiFormatReader().decode(bitmap);

            if (resultado != null) {
                String textoQR = resultado.getText();

                if (textoQR != null && !textoQR.trim().isEmpty()) {
                    qrDetectado = true;
                    timer.stop();

                    overlayQR.setDetectado(true);
                    lblEstado.setText("QR detectado correctamente.");
                    lblResultado.setText("Información detectada: " + textoQR);

                    JOptionPane.showMessageDialog(
                            this,
                            "QR detectado correctamente.\n\n"
                            + "Información leída:\n"
                            + textoQR
                            + "\n\nAhora presione Registrar Ingreso o Registrar Salida para guardar.",
                            "Lectura exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    accionQRDetectado.accept(textoQR.trim());
                    cerrarCamara();
                }
            }

        } catch (NotFoundException e) {
            // Sigue escaneando sin mostrar error.
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al leer el código QR:\n" + e.getMessage(),
                    "Error de lectura",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cerrarCamara() {
        if (timer != null) {
            timer.stop();
        }

        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }

        dispose();
    }

    private static class OverlayQR extends JComponent {

        private boolean detectado = false;

        public void setDetectado(boolean detectado) {
            this.detectado = detectado;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            int ancho = getWidth();
            int alto = getHeight();

            int tamano = Math.min(ancho, alto) / 2;
            int x = (ancho - tamano) / 2;
            int y = (alto - tamano) / 2;

            g2.setColor(new Color(0, 0, 0, 85));
            g2.fillRect(0, 0, ancho, y);
            g2.fillRect(0, y + tamano, ancho, alto - y - tamano);
            g2.fillRect(0, y, x, tamano);
            g2.fillRect(x + tamano, y, ancho - x - tamano, tamano);

            g2.setStroke(new BasicStroke(6));
            g2.setColor(detectado ? new Color(0, 220, 80) : new Color(0, 255, 100));

            int largoEsquina = 55;

            g2.drawLine(x, y, x + largoEsquina, y);
            g2.drawLine(x, y, x, y + largoEsquina);

            g2.drawLine(x + tamano, y, x + tamano - largoEsquina, y);
            g2.drawLine(x + tamano, y, x + tamano, y + largoEsquina);

            g2.drawLine(x, y + tamano, x + largoEsquina, y + tamano);
            g2.drawLine(x, y + tamano, x, y + tamano - largoEsquina);

            g2.drawLine(x + tamano, y + tamano, x + tamano - largoEsquina, y + tamano);
            g2.drawLine(x + tamano, y + tamano, x + tamano, y + tamano - largoEsquina);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2.setColor(Color.WHITE);

            String texto = detectado ? "QR detectado" : "Coloque el QR aquí";
            FontMetrics fm = g2.getFontMetrics();
            int textoX = x + (tamano - fm.stringWidth(texto)) / 2;
            int textoY = y + tamano + 28;

            g2.drawString(texto, textoX, textoY);

            g2.dispose();
        }
    }
}