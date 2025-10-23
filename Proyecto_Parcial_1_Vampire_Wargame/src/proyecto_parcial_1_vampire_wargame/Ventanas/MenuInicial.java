package proyecto_parcial_1_vampire_wargame.Ventanas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import proyecto_parcial_1_vampire_wargame.Player;


public class MenuInicial extends JFrame {

    private List<Player> players = new ArrayList<>();
    
    public MenuInicial() {
        setTitle("Vampire Wargame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        mostrarMenuInicial();
    }

    private void mostrarMenuInicial() {
        PanelFondo panelFondo = new PanelFondo();
        panelFondo.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 25, 25, 25);
        gbc.gridx = 0;

        JLabel titulo = new JLabel("VAMPIRE WARGAME");
        titulo.setFont(new Font("Arial Black", Font.BOLD, 52));
        titulo.setForeground(new Color(200, 0, 0));
        gbc.gridy = 0;
        panelFondo.add(titulo, gbc);

        JButton btnLogin = crearBoton("LOGIN");
        btnLogin.addActionListener(e -> {
            new IniciarSesion().setVisible(true);
            this.dispose();
        });
        gbc.gridy = 1;
        panelFondo.add(btnLogin, gbc);

        JButton btnCrear = crearBoton("CREAR CUENTA");
        btnCrear.addActionListener(e -> {
            new CrearPlayer(players).setVisible(true);
            this.dispose();
        });
        gbc.gridy = 2;
        panelFondo.add(btnCrear, gbc);

        JButton btnSalir = crearBoton("SALIR");
        btnSalir.addActionListener(e -> System.exit(0));
        gbc.gridy = 3;
        panelFondo.add(btnSalir, gbc);

        setContentPane(panelFondo);
        revalidate();
        repaint();
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(250, 65));
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(new Color(200, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private static class PanelFondo extends JPanel {
        private Image fondo;

        public PanelFondo() {
            setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (fondo != null) {
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuInicial().setVisible(true));
    }
}
