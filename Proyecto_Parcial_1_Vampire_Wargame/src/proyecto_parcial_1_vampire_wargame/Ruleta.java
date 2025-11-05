/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame;

/**
*
* @author esteb
*/

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Ruleta extends JPanel {

    public interface ResultListener {
        void onResult(String ficha);
    }

    private RuletaPanel ruletaPanel;
    private JLabel lblResultado;
    private javax.swing.Timer timer;
    private double angulo = 0;
    private double velocidad = 0;
    private boolean girando = false;

    private final String[] fichas = {
        "Hombre Lobo", "Muerte", "Vampiro",
        "Hombre Lobo", "Muerte", "Vampiro"
    };

    private Random random = new Random();
    private ResultListener listener;

    public Ruleta() {
        setLayout(new BorderLayout(4,4));
        setOpaque(false);

        lblResultado = new JLabel("Presiona girar", SwingConstants.CENTER);
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblResultado.setForeground(Color.WHITE);
        lblResultado.setOpaque(false);
        add(lblResultado, BorderLayout.NORTH);

        ruletaPanel = new RuletaPanel();
        ruletaPanel.setPreferredSize(new Dimension(200, 200));
        add(ruletaPanel, BorderLayout.CENTER);

        iniciarAnimacion();
    }

    public void setResultListener(ResultListener l) {
        this.listener = l;
    }

    public void girarAleatorio() {
        if (girando) return;
        girando = true;
        velocidad = 10 + random.nextDouble() * 10;
        lblResultado.setText("Girando...");
    }

    private void iniciarAnimacion() {
        timer = new javax.swing.Timer(20, e -> {
            if (girando) {
                angulo += velocidad;
                if (angulo > 360) angulo -= 360;
                velocidad *= 0.98;
                if (velocidad < 0.2) {
                    girando = false;
                    velocidad = 0;
                    determinarResultado();
                }
                ruletaPanel.setAngulo(angulo);
            }
        });
        timer.start();
    }

    private void determinarResultado() {
        double anguloFlecha = (450 - angulo) % 360;
        if (anguloFlecha < 0) anguloFlecha += 360;
        double sectorAngle = 360.0 / fichas.length;
        int sector = (int) (anguloFlecha / sectorAngle);
        sector = sector % fichas.length;
        String ficha = fichas[sector];
        lblResultado.setText("Salió: " + ficha);

        if (listener != null) {
            SwingUtilities.invokeLater(() -> listener.onResult(ficha));
        }
    }

    private class RuletaPanel extends JPanel {
        private double angulo = 0;
        public void setAngulo(double a) {
            this.angulo = a;
            repaint();
        }

        public RuletaPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int size = Math.min(w, h) - 10;
            int x = (w - size) / 2;
            int y = (h - size) / 2;

            double startAngle = angulo;
            Color[] colores = {
                new Color(255, 150, 150),
                new Color(100, 100, 100),
                new Color(150,150,255)
            };

            double sectorDeg = 360.0 / fichas.length;
            for (int i = 0; i < fichas.length; i++) {
                g2.setColor(colores[i % colores.length]);
                g2.fillArc(x, y, size, size, (int) startAngle, (int) sectorDeg);
                startAngle += sectorDeg;
            }

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x, y, size, size);

            startAngle = angulo + sectorDeg / 2.0;
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            for (int i = 0; i < fichas.length; i++) {
                double rad = Math.toRadians(startAngle);
                int tx = (int) (w / 2 + (size / 2.4) * Math.cos(rad));
                int ty = (int) (h / 2 - (size / 2.4) * Math.sin(rad));
                String label = fichas[i];
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(label);
                g2.setColor(Color.WHITE);
                g2.drawString(label, tx - tw/2, ty + fm.getAscent()/2 - 2);
                startAngle += sectorDeg;
            }

            g2.setColor(Color.RED);
            int[] px = {w/2 - 8, w/2 + 8, w/2};
            int[] py = {y - 8, y - 8, y + 8};
            g2.fillPolygon(px, py, 3);

            g2.dispose();
        }
    }
}
