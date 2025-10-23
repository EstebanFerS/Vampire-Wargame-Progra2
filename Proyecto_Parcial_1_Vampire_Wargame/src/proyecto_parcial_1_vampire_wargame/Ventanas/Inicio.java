/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Ventanas;
import javax.swing.*;
/**
 *
 * @author esteb
 */
public class Inicio extends JFrame{
    public Inicio(){
        setTitle("Inicio");
        setSize(1300,850);
        setLayout(null);
        
        JButton btnIniciar = new JButton("Iniciar Juego");
        btnIniciar.setBounds(450, 300, 400, 150);
        btnIniciar.addActionListener( e -> {
            new MenuInicial().setVisible(true);
            this.dispose();
        });
        add(btnIniciar);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }
}
