package proyecto_parcial_1_vampire_wargame.Ventanas;

import javax.swing.*;
import java.awt.*;
import proyecto_parcial_1_vampire_wargame.Panel;
import proyecto_parcial_1_vampire_wargame.Player;
import proyecto_parcial_1_vampire_wargame.Almacenamiento.Manager;

/**
 *
 * @author esteb
 */
public class IniciarSesion extends JFrame {

    public IniciarSesion() {
        setTitle("Iniciar Sesión - Vampire Wargame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        mostrarLogin();
    }

    private void mostrarLogin() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("INICIAR SESIÓN");
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 55));
        lblTitulo.setForeground(new Color(230, 182, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panelPrincipal.add(lblUser, gbc);

        JTextField txtUser = new JTextField(20);
        txtUser.setBackground(new Color(40, 40, 60));
        txtUser.setForeground(Color.WHITE);
        txtUser.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        panelPrincipal.add(txtUser, gbc);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridy = 2;
        gbc.gridx = 0;
        panelPrincipal.add(lblPassword, gbc);

        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setBackground(new Color(40, 40, 60));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        panelPrincipal.add(txtPassword, gbc);

        JCheckBox chkMostrar = new JCheckBox("Mostrar contraseña");
        chkMostrar.setOpaque(false);
        chkMostrar.setForeground(Color.WHITE);
        chkMostrar.setFont(new Font("Arial", Font.PLAIN, 18));
        chkMostrar.addActionListener(e -> txtPassword.setEchoChar(chkMostrar.isSelected() ? (char)0 : '•'));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panelPrincipal.add(chkMostrar, gbc);

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 0, 20));
        panelBotones.setOpaque(false);

        JButton btnIniciar = crearBoton("INICIAR SESIÓN", 300, 65);
        JButton btnVolver = crearBoton("VOLVER", 300, 65);

        panelBotones.add(btnIniciar);
        panelBotones.add(btnVolver);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(panelBotones, gbc);

        btnIniciar.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPassword.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Manager es la fuente única de verdad
            Player jugadorActual = Manager.getInstance().getPlayer(user);
            if (jugadorActual == null) {
                JOptionPane.showMessageDialog(this, "Usuario no existe o no está activo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!jugadorActual.getPassword().equals(pass)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Login exitoso
            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
            new MenuPrincipal(jugadorActual).setVisible(true);
            this.dispose();
        });

        btnVolver.addActionListener(e -> {
            new MenuInicial().setVisible(true);
            this.dispose();
        });

        Panel panelFondo = new Panel("/Images/Fondo2.jpg");
        panelFondo.add(panelPrincipal);
        setContentPane(panelFondo);
        revalidate();
        repaint();
    }

    private JButton crearBoton(String texto, int ancho, int alto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(ancho, alto));
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setBackground(new Color(82, 36, 36));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(145, 38, 36)); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(82, 36, 36)); }
        });
        return btn;
    }
}
