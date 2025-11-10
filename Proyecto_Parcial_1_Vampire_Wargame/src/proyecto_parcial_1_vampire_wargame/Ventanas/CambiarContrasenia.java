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
public class CambiarContrasenia extends JFrame{

    private final Player jugadorActual;
    private JTextField txtContraseniaActual;
    private JTextField txtContraseniaNueva;

    public CambiarContrasenia(Player jugadorActual) {
        this.jugadorActual = jugadorActual;
        setTitle("Cambiar Contraseña - Vampire Wargame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
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

        JLabel titulo = new JLabel("CAMBIAR CONTRASEÑA");
        titulo.setFont(new Font("Arial Black", Font.BOLD, 55));
        titulo.setForeground(new Color(230, 182, 50));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelCentral.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel lblActual = new JLabel("Contraseña actual:");
        lblActual.setForeground(Color.WHITE);
        lblActual.setFont(new Font("Arial", Font.BOLD, 22));
        panelCentral.add(lblActual, gbc);

        txtContraseniaActual = new JTextField(20);
        txtContraseniaActual.setFont(new Font("Arial", Font.PLAIN, 20));
        txtContraseniaActual.setBackground(new Color(40, 40, 60));
        txtContraseniaActual.setForeground(Color.WHITE);
        gbc.gridx = 1;
        panelCentral.add(txtContraseniaActual, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel lblNueva = new JLabel("Contraseña nueva:");
        lblNueva.setForeground(Color.WHITE);
        lblNueva.setFont(new Font("Arial", Font.BOLD, 22));
        panelCentral.add(lblNueva, gbc);

        txtContraseniaNueva = new JTextField(20);
        txtContraseniaNueva.setFont(new Font("Arial", Font.PLAIN, 20));
        txtContraseniaNueva.setBackground(new Color(40, 40, 60));
        txtContraseniaNueva.setForeground(Color.WHITE);
        gbc.gridx = 1;
        panelCentral.add(txtContraseniaNueva, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel lblInfo = new JLabel("La nueva contraseña no puede ser igual a la actual.");
        lblInfo.setForeground(new Color(200, 200, 200));
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 18));
        panelCentral.add(lblInfo, gbc);

        gbc.gridy = 4;
        JPanel panelBtns = new JPanel(new GridBagLayout());
        panelBtns.setOpaque(false);
        GridBagConstraints b = new GridBagConstraints();
        b.insets = new Insets(12, 12, 12, 12);
        b.gridx = 0;
        b.gridy = 0;

        JButton btnAceptar = crearBoton("ACEPTAR");
        btnAceptar.addActionListener(e -> procesarCambio());
        panelBtns.add(btnAceptar, b);

        b.gridx++;
        JButton btnRegresar = crearBoton("REGRESAR");
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

    private void procesarCambio() {
        String actual = txtContraseniaActual.getText().trim();
        String nueva = txtContraseniaNueva.getText().trim();

        if (actual.isEmpty() || nueva.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor completa ambos campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String almacenada = jugadorActual.getPassword();

        if (!almacenada.equals(actual)) {
            JOptionPane.showMessageDialog(this, "Contraseña actual incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (actual.equals(nueva)) {
            JOptionPane.showMessageDialog(this, "La nueva contraseña no puede ser igual a la actual.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        jugadorActual.setPassword(nueva);
        Manager.getInstance().actualizarPlayer(jugadorActual);

        JOptionPane.showMessageDialog(this, "Contraseña cambiada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        new MiPerfil(jugadorActual).setVisible(true);
        dispose();
    }
}
