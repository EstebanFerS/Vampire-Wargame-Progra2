/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Ventanas;
import proyecto_parcial_1_vampire_wargame.Panel;
import proyecto_parcial_1_vampire_wargame.Player;
import proyecto_parcial_1_vampire_wargame.Almacenamiento.Manager;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author esteb
 */
public class BorrarCuenta extends JFrame {

    private final Player jugadorActual;

    public BorrarCuenta(Player jugadorActual) {
        this.jugadorActual = jugadorActual;
        setTitle("Borrar Cuenta - Vampire Wargame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        inicializarUI();
    }

    private void inicializarUI() {
        Panel panelFondo = new Panel("/Images/Fondo2.jpg");
        panelFondo.setLayout(new GridBagLayout());
        panelFondo.setOpaque(false);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;

        JLabel titulo = new JLabel("BORRAR CUENTA");
        titulo.setFont(new Font("Arial Black", Font.BOLD, 55));
        titulo.setForeground(new Color(230, 182, 50));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelCentral.add(titulo, gbc);

        gbc.gridy = 1;
        JTextArea aviso = new JTextArea(
                "ATENCIÓN: ESTA ACCIÓN ES IRREVERSIBLE.\n" +
                "Tu cuenta será desactivada y no podrás volver a iniciar sesión.");
        aviso.setEditable(false);
        aviso.setOpaque(false);
        aviso.setForeground(new Color(230, 200, 200));
        aviso.setFont(new Font("Arial", Font.BOLD, 20));
        aviso.setFocusable(false);
        aviso.setHighlighter(null);
        gbc.gridwidth = 2;
        panelCentral.add(aviso, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel lblConfirm = new JLabel("Escribe tu contraseña para confirmar:");
        lblConfirm.setForeground(Color.WHITE);
        lblConfirm.setFont(new Font("Arial", Font.BOLD, 22));
        panelCentral.add(lblConfirm, gbc);

        gbc.gridx = 1;
        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 20));
        txtPassword.setBackground(new Color(40, 40, 60));
        txtPassword.setForeground(Color.WHITE);
        panelCentral.add(txtPassword, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JTextArea info = new JTextArea(
                "Una vez confirmes, tu cuenta pasará a estado inactivo.\n" +
                "No podrás acceder nuevamente.");
        info.setEditable(false);
        info.setOpaque(false);
        info.setForeground(new Color(200, 200, 200));
        info.setFont(new Font("Arial", Font.ITALIC, 16));
        info.setFocusable(false);
        info.setHighlighter(null);
        panelCentral.add(info, gbc);

        gbc.gridy = 4;
        JPanel panelBtns = new JPanel(new GridBagLayout());
        panelBtns.setOpaque(false);
        GridBagConstraints b = new GridBagConstraints();
        b.insets = new Insets(12, 12, 12, 12);
        b.gridx = 0;
        b.gridy = 0;

        JButton btnBorrar = crearBoton("BORRAR CUENTA");
        btnBorrar.setBackground(new Color(180, 30, 30));
        btnBorrar.addActionListener(e -> procesarBorrado(txtPassword));
        panelBtns.add(btnBorrar, b);

        b.gridx++;
        JButton btnRegresar = crearBoton("CANCELAR");
        btnRegresar.addActionListener(e -> {
            new MiPerfil(jugadorActual).setVisible(true);
            dispose();
        });
        panelBtns.add(btnRegresar, b);

        panelCentral.add(panelBtns, gbc);

        panelFondo.add(panelCentral);
        setContentPane(panelFondo);
        revalidate();
        repaint();
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(300, 70));
        btn.setFont(new Font("Arial", Font.BOLD, 24));
        btn.setBackground(new Color(82, 36, 36));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        btn.setOpaque(true);

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

    private void procesarBorrado(JPasswordField txtPassword) {
        String ingresada = new String(txtPassword.getPassword()).trim();

        if (ingresada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingresa tu contraseña para confirmar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String almacenada = jugadorActual.getPassword();
        if (!almacenada.equals(ingresada)) {
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String confirmMsg =
                "¿Estás seguro de que quieres DESACTIVAR tu cuenta?\n" +
                "Esta acción es IRREVERSIBLE.\n" +
                "Tu estado pasará a 'inactivo' y no podrás iniciar sesión.\n\n" +
                "¿Confirmar desactivación?";
        int confirm = JOptionPane.showConfirmDialog(this, confirmMsg,
                "Confirmar borrado de cuenta", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        jugadorActual.setActivo(false);

        try {
            Manager.getInstance().actualizarPlayer(jugadorActual);
        } catch (Exception ex) {
            System.err.println("Warning: no se pudo actualizar Manager: " + ex.getMessage());
        }

        JOptionPane.showMessageDialog(this,
                "Cuenta desactivada correctamente.\nYa no podrás iniciar sesión con esta cuenta.",
                "Cuenta desactivada", JOptionPane.INFORMATION_MESSAGE);

        new MenuInicial().setVisible(true);
        dispose();
    }
}
