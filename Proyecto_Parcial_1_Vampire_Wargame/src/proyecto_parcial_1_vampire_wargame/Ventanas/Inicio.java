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

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setOpaque(false);
        panelPrincipal.setLayout(new GridBagLayout());
        panelPrincipal.setBackground(new Color(20, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        JLabel lblTitulo = new JLabel("VAMPIRE WARGAME");
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 60));
        lblTitulo.setForeground(new Color(230, 182, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelPrincipal.add(lblTitulo, gbc);

        JButton btnIniciar = new JButton("INICIAR JUEGO");
        btnIniciar.setPreferredSize(new Dimension(400, 100));
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 28));
        btnIniciar.setBackground(new Color(82, 36, 36));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIniciar.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        btnIniciar.addActionListener(e -> {
            new MenuInicial().setVisible(true);
            this.dispose();
        });

        gbc.gridy = 1;
        panelPrincipal.add(btnIniciar, gbc);


        Panel panelFondo = new Panel("/Images/Fondo2.jpg");
        panelFondo.add(panelPrincipal);
        setContentPane(panelFondo);
    }
}
