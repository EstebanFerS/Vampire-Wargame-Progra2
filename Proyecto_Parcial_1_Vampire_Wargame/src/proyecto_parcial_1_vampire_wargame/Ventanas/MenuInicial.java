package proyecto_parcial_1_vampire_wargame.Ventanas;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import proyecto_parcial_1_vampire_wargame.Panel;
import proyecto_parcial_1_vampire_wargame.Player;

public class MenuInicial extends JFrame {

    private List<Player> players;

    public MenuInicial(List<Player> players) {
        this.players = players;
        setTitle("Vampire Wargame - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        mostrarMenuInicial();
    }
    
    public MenuInicial() {
        this(new ArrayList<>());
    }

    private void mostrarMenuInicial() {

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 25, 25, 25);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        JLabel lblTitulo = new JLabel("VAMPIRE WARGAME");
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 60));
        lblTitulo.setForeground(new Color(230, 182, 50));
        gbc.gridy = 0;
        panelPrincipal.add(lblTitulo, gbc);

        JButton btnLogin = crearBoton("INICIAR SESIÓN");
        btnLogin.addActionListener(e -> {
            new IniciarSesion(players).setVisible(true);
            this.dispose();
        });
        gbc.gridy = 1;
        panelPrincipal.add(btnLogin, gbc);

        JButton btnCrear = crearBoton("CREAR CUENTA");
        btnCrear.addActionListener(e -> {
            new CrearPlayer(players).setVisible(true);
            this.dispose();
        });
        gbc.gridy = 2;
        panelPrincipal.add(btnCrear, gbc);

        JButton btnSalir = crearBoton("SALIR DEL JUEGO");
        btnSalir.addActionListener(e -> System.exit(0));
        gbc.gridy = 3;
        panelPrincipal.add(btnSalir, gbc);

        Panel panelFondo = new Panel("/Images/Fondo1.jpg");
        panelFondo.add(panelPrincipal);

        setContentPane(panelFondo);
        revalidate();
        repaint();
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(300, 75));
        btn.setFont(new Font("Arial", Font.BOLD, 24));
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
