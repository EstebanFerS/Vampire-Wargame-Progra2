/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Ventanas;
import proyecto_parcial_1_vampire_wargame.Panel;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author esteb
 */
public class Inicio extends JFrame {

    public Inicio() {
        setTitle("Inicio - Vampire Wargame");
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("VAMPIRE WARGAME");
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 60));
        lblTitulo.setForeground(new Color(230, 182, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelPrincipal.add(lblTitulo, gbc);

        JButton btnIniciar = crearBoton("INICIAR JUEGO", 400, 100);
        btnIniciar.addActionListener(e -> {
            new MenuInicial().setVisible(true);
            this.dispose();
        });

        gbc.gridy = 1;
        panelPrincipal.add(btnIniciar, gbc);

        Panel panelFondo = new Panel("/Images/Fondo2.jpg");
        panelFondo.add(panelPrincipal);
        setContentPane(panelFondo);
        revalidate();
        repaint();
    }

    private JButton crearBoton(String texto, int ancho, int alto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(ancho, alto));
        btn.setFont(new Font("Arial", Font.BOLD, 28));
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