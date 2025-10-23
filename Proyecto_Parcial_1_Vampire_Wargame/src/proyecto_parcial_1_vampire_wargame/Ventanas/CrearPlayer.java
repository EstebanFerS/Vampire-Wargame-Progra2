package proyecto_parcial_1_vampire_wargame.Ventanas;

import javax.swing.*;
import java.awt.*;
import proyecto_parcial_1_vampire_wargame.Player;
import java.util.List;

public class CrearPlayer extends JFrame {

    private List<Player> players;

    public CrearPlayer(List<Player> players) {
        this.players = players;
        setTitle("Crear Cuenta - Vampire Wargame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        mostrarCrearCuenta();
    }

    private void mostrarCrearCuenta() {

        JPanel panelTemporal = new JPanel(new GridBagLayout());
        panelTemporal.setBackground(new Color(20, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("CREAR CUENTA");
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 48));
        lblTitulo.setForeground(Color.YELLOW);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelTemporal.add(lblTitulo, gbc);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panelTemporal.add(lblUser, gbc);

        JTextField txtUser = new JTextField(20);
        txtUser.setBackground(new Color(40, 40, 60));
        txtUser.setForeground(Color.WHITE);
        txtUser.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        panelTemporal.add(txtUser, gbc);

        JLabel lblPassword = new JLabel("Contraseña (mínimo 5 caracteres):");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelTemporal.add(lblPassword, gbc);

        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setBackground(new Color(40, 40, 60));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        panelTemporal.add(txtPassword, gbc);

        JCheckBox chkMostrar = new JCheckBox("Mostrar contraseña");
        chkMostrar.setBackground(new Color(20, 20, 30));
        chkMostrar.setForeground(Color.WHITE);
        chkMostrar.setFont(new Font("Arial", Font.PLAIN, 16));

        chkMostrar.addActionListener(e -> {
            if (chkMostrar.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•'); 
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 3;
        panelTemporal.add(chkMostrar, gbc);

        JLabel lblInfo = new JLabel("Debe incluir caracteres especiales");
        lblInfo.setForeground(new Color(180, 180, 180));
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelTemporal.add(lblInfo, gbc);

        JLabel lblEj = new JLabel("Ejemplo: Pass@12345");
        lblEj.setForeground(new Color(100, 220, 100));
        lblEj.setFont(new Font("Arial", Font.ITALIC, 15));
        gbc.gridy = 5;
        panelTemporal.add(lblEj, gbc);

        JButton btnCrear = crearBoton("CREAR CUENTA");
        btnCrear.addActionListener(e -> {
            String user = txtUser.getText();
            String password = new String(txtPassword.getPassword());

            if (!validarUsername(user)) {
                JOptionPane.showMessageDialog(null, "Usuario no valido o ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!validarPassword(password)) {
                JOptionPane.showMessageDialog(this, "Contraseña: 5 caracteres + caracteres especiales\nEjemplo: Pass!123", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Player player = new Player(user, password);
                players.add(player);
                JOptionPane.showMessageDialog(null, "Cuenta Creada Exitosamente");
                new MenuInicial().setVisible(true);
                this.dispose();
            }
        });
        gbc.gridy = 6;
        panelTemporal.add(btnCrear, gbc);

        JButton btnVolver = crearBoton("VOLVER");
        btnVolver.addActionListener(e -> {
            new MenuInicial().setVisible(true);
            this.dispose();
        });
        gbc.gridy = 7;
        panelTemporal.add(btnVolver, gbc);

        setContentPane(panelTemporal);
        revalidate();
        repaint();
    }

    private boolean validarUsername(String username) {
        if (username.isEmpty() || username.length() < 3) {
            return false;
        }
        for (Player p : players) {
            if (p.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    private boolean validarPassword(String password) {
        return password.length() == 5
                && password.matches(".*[!@#$%^&*()_+=\\-\\[\\]{};':\"\\\\|,.<>?].*");
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(300, 65));
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setBackground(new Color(200, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        return btn;
    }

}
