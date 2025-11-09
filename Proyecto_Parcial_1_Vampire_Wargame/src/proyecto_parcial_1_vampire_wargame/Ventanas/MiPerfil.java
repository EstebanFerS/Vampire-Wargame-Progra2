/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Ventanas;

import proyecto_parcial_1_vampire_wargame.Panel;
import proyecto_parcial_1_vampire_wargame.Player;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author esteb
 */
public class MiPerfil extends JFrame {

    private final Player jugadorActual;
    private JLabel lblUsername;
    private JLabel lblPuntos;
    private JLabel lblEstadoTexto;
    private StatusDot lblEstadoDot;

    public MiPerfil(Player jugadorActual) {
        this.jugadorActual = jugadorActual;
        setTitle("Mi Perfil - Vampire Wargame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        inicializarMiPerfil();
    }

    private void inicializarMiPerfil() {
        Panel panelFondo = new Panel("/Images/Fondo2.jpg");
        panelFondo.setLayout(new GridBagLayout());
        panelFondo.setOpaque(false);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;

        JLabel lblTitulo = new JLabel("MI CUENTA");
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 50));
        lblTitulo.setForeground(new Color(230, 182, 50));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelCentral.add(lblTitulo, gbc);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panelCentral.add(lblUser, gbc);

        lblUsername = new JLabel(obtenerUsuarioSeguro());
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 22));
        gbc.gridx = 1;
        panelCentral.add(lblUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblPts = new JLabel("Puntos:");
        lblPts.setForeground(Color.WHITE);
        lblPts.setFont(new Font("Arial", Font.BOLD, 22));
        panelCentral.add(lblPts, gbc);

        lblPuntos = new JLabel(String.valueOf(obtenerPuntosSeguros()));
        lblPuntos.setForeground(new Color(100, 220, 100));
        lblPuntos.setFont(new Font("Arial", Font.PLAIN, 22));
        gbc.gridx = 1;
        panelCentral.add(lblPuntos, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 22));
        panelCentral.add(lblEstado, gbc);

        JPanel filaEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filaEstado.setOpaque(false);
        lblEstadoDot = new StatusDot(estaActivoJugador());
        lblEstadoTexto = new JLabel(estaActivoJugador() ? "Activo" : "Inactivo");
        lblEstadoTexto.setFont(new Font("Arial", Font.PLAIN, 20));
        lblEstadoTexto.setForeground(Color.WHITE);
        filaEstado.add(lblEstadoDot);
        filaEstado.add(lblEstadoTexto);

        gbc.gridx = 1;
        panelCentral.add(filaEstado, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JPanel panelBtns = new JPanel(new GridBagLayout());
        panelBtns.setOpaque(false);
        GridBagConstraints b = new GridBagConstraints();
        b.insets = new Insets(8, 8, 8, 8);
        b.gridx = 0;
        b.gridy = 0;
        b.fill = GridBagConstraints.HORIZONTAL;

        JButton btnCambiar = crearBoton("CAMBIAR CONTRASEÑA");
        btnCambiar.addActionListener(e -> {
            new CambiarContrasenia(jugadorActual).setVisible(true);
            dispose();
        });
        panelBtns.add(btnCambiar, b);

        b.gridy++;
        JButton btnBorrar = crearBoton("BORRAR CUENTA");
        btnBorrar.setEnabled(false);
        btnBorrar.setToolTipText("Función no implementada aún");
        panelBtns.add(btnBorrar, b);

        b.gridy++;
        JButton btnVolver = crearBoton("VOLVER");
        btnVolver.addActionListener(e -> {
            try {
                new proyecto_parcial_1_vampire_wargame.Ventanas.MenuPrincipal(jugadorActual).setVisible(true);
            } catch (Exception ex) {
                System.err.println("No se pudo abrir MenuPrincipal: " + ex.getMessage());
            }
            dispose();
        });
        panelBtns.add(btnVolver, b);

        panelCentral.add(panelBtns, gbc);

        panelFondo.add(panelCentral);
        setContentPane(panelFondo);
        revalidate();
        repaint();
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(300, 65));
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setBackground(new Color(82, 36, 36));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(140, 50, 50));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(82, 36, 36));
                }
            }
        });

        return btn;
    }

    private String obtenerUsuarioSeguro() {
        try {
            return jugadorActual != null && jugadorActual.getUsername() != null ? jugadorActual.getUsername() : "Desconocido";
        } catch (Exception ex) {
            return "Desconocido";
        }
    }

    private int obtenerPuntosSeguros() {
        try {
            return jugadorActual != null ? jugadorActual.getPuntos() : 0;
        } catch (Exception ex) {
            return 0;
        }
    }

    private boolean estaActivoJugador() {
        try {
            return jugadorActual != null && jugadorActual.isActivo();
        } catch (Exception ex) {
            return true;
        }
    }

    public void actualizarVista() {
        if (lblPuntos != null) {
            lblPuntos.setText(String.valueOf(obtenerPuntosSeguros()));
        }
        if (lblEstadoTexto != null && lblEstadoDot != null) {
            boolean activo = estaActivoJugador();
            lblEstadoTexto.setText(activo ? "Activo" : "Inactivo");
            lblEstadoDot.establecerActivo(activo);
            repaint();
        }
    }

    private static class StatusDot extends JComponent {

        private boolean activo;
        private final int size = 16;

        public StatusDot(boolean activo) {
            this.activo = activo;
            setPreferredSize(new Dimension(size + 6, size + 6));
            setOpaque(false);
        }

        public void establecerActivo(boolean a) {
            this.activo = a;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fill = activo ? new Color(0, 200, 0) : new Color(120, 120, 120);
            g2.setColor(fill);
            g2.fillOval(2, 2, size, size);
            g2.setColor(new Color(30, 30, 30, 120));
            g2.setStroke(new BasicStroke(1f));
            g2.drawOval(2, 2, size, size);
            g2.dispose();
        }
    }
}
