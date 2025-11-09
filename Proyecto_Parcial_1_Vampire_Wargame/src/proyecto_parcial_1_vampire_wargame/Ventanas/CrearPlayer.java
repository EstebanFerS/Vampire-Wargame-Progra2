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
public class CrearPlayer extends JFrame {

    public CrearPlayer() {
        setTitle("Crear Cuenta - Vampire Wargame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        mostrarCrearCuenta();
    }

    private void mostrarCrearCuenta() {

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;

        JLabel lblTitulo = new JLabel("CREAR CUENTA");
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 55));
        lblTitulo.setForeground(new Color(230, 182, 50));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panelPrincipal.add(lblUser, gbc);

        JTextField txtUser = new JTextField(20);
        txtUser.setBackground(new Color(40, 40, 60));
        txtUser.setForeground(Color.WHITE);
        txtUser.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        panelPrincipal.add(txtUser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblPassword = new JLabel("Contraseña (mínimo 5 caracteres):");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 22));
        panelPrincipal.add(lblPassword, gbc);

        gbc.gridx = 1;
        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setBackground(new Color(40, 40, 60));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 20));
        txtPassword.setEchoChar('•');
        panelPrincipal.add(txtPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JCheckBox chkMostrar = new JCheckBox("Mostrar contraseña");
        chkMostrar.setOpaque(false);
        chkMostrar.setForeground(Color.WHITE);
        chkMostrar.setFont(new Font("Arial", Font.PLAIN, 18));
        chkMostrar.addActionListener(e -> {
            if (chkMostrar.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });
        panelPrincipal.add(chkMostrar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel lblInfo = new JLabel("Debe incluir caracteres especiales");
        lblInfo.setForeground(new Color(180, 180, 180));
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 18));
        panelPrincipal.add(lblInfo, gbc);

        gbc.gridy = 5;
        JLabel lblEj = new JLabel("Ejemplo: Pas!1");
        lblEj.setForeground(new Color(100, 220, 100));
        lblEj.setFont(new Font("Arial", Font.ITALIC, 17));
        panelPrincipal.add(lblEj, gbc);

        gbc.gridy = 6;
        JButton btnCrear = crearBoton("CREAR CUENTA");
        btnCrear.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (!validarUsername(user)) {
                JOptionPane.showMessageDialog(this, "Usuario no válido o ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!validarPassword(password)) {
                JOptionPane.showMessageDialog(this,
                        "La contraseña debe tener:\n"
                        + "- Exactamente 5 caracteres\n"
                        + "- Al menos una mayúscula\n"
                        + "- Al menos un número\n"
                        + "- Al menos un símbolo especial",
                        "Error", JOptionPane.ERROR_MESSAGE);

            } else {
                Player player = new Player(user, password);
                Manager.getInstance().agregarPlayer(player);
                JOptionPane.showMessageDialog(this, "Cuenta creada exitosamente");
                new MenuPrincipal(player).setVisible(true);
                this.dispose();
            }
        });
        panelPrincipal.add(btnCrear, gbc);

        gbc.gridy = 7;
        JButton btnVolver = crearBoton("VOLVER");
        btnVolver.addActionListener(e -> {
            new MenuInicial().setVisible(true);
            this.dispose();
        });
        panelPrincipal.add(btnVolver, gbc);

        Panel panelFondo = new Panel("/Images/Fondo2.jpg");
        panelFondo.add(panelPrincipal);
        setContentPane(panelFondo);
        revalidate();
        repaint();
    }

    private boolean validarUsername(String username) {
        if (username == null) {
            return false;
        }
        username = username.trim();
        if (username.isEmpty() || username.length() < 3) {
            return false;
        }

        return Manager.getInstance().getPlayer(username) == null;
    }

    private boolean validarPassword(String password) {
        if (password == null) {
            return false;
        }

        if (password.length() != 5) {
            return false;
        }

        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneNumero = password.matches(".*\\d.*");
        boolean tieneSimbolo = password.matches(".*[!@#$%^&*()_+=\\-\\[\\]{};':\"\\\\|,.<>?].*");

        return tieneMayuscula && tieneNumero && tieneSimbolo;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(400, 80));
        btn.setFont(new Font("Arial", Font.BOLD, 26));
        btn.setBackground(new Color(82, 36, 36));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(140, 50, 50));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(82, 36, 36));
            }
        });
        return btn;
    }
}
