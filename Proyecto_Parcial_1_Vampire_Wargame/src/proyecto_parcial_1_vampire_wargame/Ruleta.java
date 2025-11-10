package proyecto_parcial_1_vampire_wargame;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public final class Ruleta extends JPanel {

    public interface ResultListener {
        void onResult(String ficha);
    }

    private final RuletaPanel ruletaPanel;
    private final JLabel lblResultado;
    private final JLabel lblSpins;
    private final JLabel lblSelectedPiece;
    private final JButton btnGirar;

    private Timer timer;
    private double angulo = 0;
    private double velocidad = 0;
    private boolean girando = false;

    private final String[] fichas = {
        "Hombre Lobo", "Muerte", "Vampiro",
        "Hombre Lobo", "Muerte", "Vampiro"
    };

    private final Random random = new Random();
    private ResultListener listener;

    private int spinsAllowed = 1;
    private int spinsUsed = 0;
    private String selectedPieceLabel = null;

    public Ruleta() {
        setLayout(new BorderLayout(6, 6));
        setOpaque(false);

        lblResultado = new JLabel("Presiona girar", SwingConstants.CENTER);
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblResultado.setForeground(Color.WHITE);
        lblResultado.setOpaque(false);
        add(lblResultado, BorderLayout.NORTH);

        ruletaPanel = new RuletaPanel();
        ruletaPanel.setPreferredSize(new Dimension(200, 200));
        add(ruletaPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 0;
        gbc.gridy = 0;

        btnGirar = new JButton("GIRAR RULETA");
        btnGirar.setPreferredSize(new Dimension(160, 36));
        btnGirar.addActionListener(e -> attemptSpin());
        bottom.add(btnGirar, gbc);

        gbc.gridy++;
        lblSpins = new JLabel("", SwingConstants.CENTER);
        lblSpins.setForeground(Color.WHITE);
        lblSpins.setFont(new Font("Arial", Font.PLAIN, 12));
        bottom.add(lblSpins, gbc);

        gbc.gridy++;
        lblSelectedPiece = new JLabel("Ficha seleccionada: —", SwingConstants.CENTER);
        lblSelectedPiece.setForeground(Color.WHITE);
        lblSelectedPiece.setFont(new Font("Arial", Font.PLAIN, 12));
        bottom.add(lblSelectedPiece, gbc);

        add(bottom, BorderLayout.SOUTH);

        iniciarAnimacion();
        actualizarLabels();
    }

    public void setResultListener(ResultListener l) {
        this.listener = l;
    }

    public void setSpinsAllowed(int n) {
        this.spinsAllowed = Math.max(1, n);
        if (spinsUsed > spinsAllowed) spinsUsed = spinsAllowed;
        actualizarLabels();
        updateButtonState();
    }

    public final void resetSpins() {
        this.spinsUsed = 0;
        this.selectedPieceLabel = null;
        lblResultado.setText("Presiona girar");
        lblSelectedPiece.setText("Ficha seleccionada: —");
        actualizarLabels();
        updateButtonState();
    }

    public int getSpinsAllowed() { return spinsAllowed; }
    public int getSpinsUsed() { return spinsUsed; }

    public boolean hasSpinsLeft() { return spinsUsed < spinsAllowed; }

    private void attemptSpin() {
        if (girando) return;
        if (!hasSpinsLeft()) {
            lblResultado.setText("No quedan giros");
            updateButtonState();
            return;
        }
        spinsUsed++;
        actualizarLabels();
        girarAleatorio();
    }

    public void enableSpinButton(boolean enable) {
        btnGirar.setEnabled(enable && !girando && hasSpinsLeft());
    }

    public void setSelectedPieceLabel(String texto) {
        this.selectedPieceLabel = texto;
        lblSelectedPiece.setText("Ficha seleccionada: " + (texto == null ? "—" : texto));
    }

    private void girarAleatorio() {
        if (girando) return;
        girando = true;
        velocidad = 10 + random.nextDouble() * 10;
        lblResultado.setText("Girando...");
    }

    private void iniciarAnimacion() {
        timer = new Timer(20, e -> {
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
        sector = (sector % fichas.length + fichas.length) % fichas.length;
        String ficha = fichas[sector];
        lblResultado.setText("Salió: " + ficha);
        setSelectedPieceLabel(ficha);

        if (listener != null) {
            SwingUtilities.invokeLater(() -> listener.onResult(ficha));
        }

        updateButtonState();
    }

    private void updateButtonState() {
        btnGirar.setEnabled(!girando && hasSpinsLeft());
        actualizarLabels();
    }

    private void actualizarLabels() {
        lblSpins.setText("Giros: " + spinsUsed + "/" + spinsAllowed);
        lblSelectedPiece.setText("Ficha seleccionada: " + (selectedPieceLabel == null ? "—" : selectedPieceLabel));
    }

    private class RuletaPanel extends JPanel {
        private double angulo = 0;
        public void setAngulo(double a) { this.angulo = a; repaint(); }
        public RuletaPanel() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            int size = Math.min(w,h) - 10;
            int x = (w - size)/2, y = (h - size)/2;
            double startAngle = angulo;
            Color[] colores = { new Color(255,150,150), new Color(100,100,100), new Color(150,150,255) };
            double sectorDeg = 360.0 / fichas.length;
            for (int i=0;i<fichas.length;i++){
                g2.setColor(colores[i % colores.length]);
                g2.fillArc(x,y,size,size,(int)startAngle,(int)sectorDeg);
                startAngle += sectorDeg;
            }
            g2.setColor(Color.BLACK); g2.setStroke(new BasicStroke(2)); g2.drawOval(x,y,size,size);
            startAngle = angulo + sectorDeg/2.0;
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            for (int i=0;i<fichas.length;i++){
                double rad = Math.toRadians(startAngle);
                int tx = (int)(w/2 + (size/2.4)*Math.cos(rad));
                int ty = (int)(h/2 - (size/2.4)*Math.sin(rad));
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
            g2.fillPolygon(px,py,3);
            g2.dispose();
        }
    }
}
