package proyecto_parcial_1_vampire_wargame.Ventanas;

import javax.swing.*;
import java.awt.*;
import proyecto_parcial_1_vampire_wargame.Panel;
import proyecto_parcial_1_vampire_wargame.Player;
import proyecto_parcial_1_vampire_wargame.Almacenamiento.Manager;
import java.util.List;
import java.util.ArrayList;

public class MenuPrincipal extends JFrame {

    private Player playerActual;

    public MenuPrincipal(Player playerActual) {
        this.playerActual = playerActual;

        setTitle("Vampire Wargame - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        mostrarMenu();
    }

    private void mostrarMenu() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 25, 25, 25);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel lblTitulo = new JLabel("BIENVENIDO, " + playerActual.getUsername().toUpperCase());
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 55));
        lblTitulo.setForeground(new Color(230, 182, 50));
        panelPrincipal.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        JButton btnJugar = crearBoton("JUGAR", 400, 80);
        btnJugar.addActionListener(e -> {
            Manager m = Manager.getInstance();
            List<Player> activos = m.getAllPlayers();

            List<Player> oponentes = new ArrayList<>();
            for (Player p : activos) {
                if (!p.getUsername().equals(playerActual.getUsername())) {
                    oponentes.add(p);
                }
            }

            if (oponentes.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay oponentes activos.\nDebe haber al menos dos jugadores activos para iniciar una partida.",
                        "Sin oponentes",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] opciones = new String[oponentes.size()];
            for (int i = 0; i < oponentes.size(); i++) {
                opciones[i] = oponentes.get(i).getUsername();
            }

            String seleccionado = (String) JOptionPane.showInputDialog(
                    this,
                    "Selecciona el oponente:",
                    "Elegir Oponente",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (seleccionado != null && !seleccionado.isEmpty()) {
                Player rival = m.getPlayer(seleccionado);
                if (rival == null) {
                    JOptionPane.showMessageDialog(this,
                            "El oponente ya no está activo.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                new Tablero(playerActual, rival).setVisible(true);
                this.dispose();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(btnJugar, gbc);

        JButton btnPerfil = crearBoton("MI PERFIL", 400, 80);
        btnPerfil.addActionListener(e -> {
            new MiPerfil(playerActual).setVisible(true);
            dispose();
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelPrincipal.add(btnPerfil, gbc);

        JButton btnReportes = crearBoton("REPORTES", 400, 80);
        btnReportes.addActionListener(e -> {
            Manager m = Manager.getInstance();
            new Reportes(playerActual, m);
            dispose();
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelPrincipal.add(btnReportes, gbc);

        JButton btnLogout = crearBoton("LOG OUT", 400, 80);
        btnLogout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Sesión cerrada correctamente.", "Hasta pronto", JOptionPane.INFORMATION_MESSAGE);
            new MenuInicial().setVisible(true);
            this.dispose();
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        panelPrincipal.add(btnLogout, gbc);

        Panel panelFondo = new Panel("/Images/Fondo3.jpg");
        panelFondo.add(panelPrincipal);

        setContentPane(panelFondo);
        revalidate();
        repaint();
    }

    private JButton crearBoton(String texto, int ancho, int alto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(ancho, alto));
        btn.setFont(new Font("Arial", Font.BOLD, 26));
        btn.setBackground(new Color(82, 36, 36));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(145, 38, 36));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(82, 36, 36));
            }
        });

        return btn;
    }
}
