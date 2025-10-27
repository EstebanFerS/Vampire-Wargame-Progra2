/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author esteb
 */
public class Panel extends JPanel{
    private Image imagenFondo;

    public Panel(String rutaImagen) {
        imagenFondo = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
        setLayout(new GridBagLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);

        g.setColor(new Color(0, 0, 0, 160)); 
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
