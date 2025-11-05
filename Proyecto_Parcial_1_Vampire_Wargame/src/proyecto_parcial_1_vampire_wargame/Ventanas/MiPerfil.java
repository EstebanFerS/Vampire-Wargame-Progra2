/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Ventanas;
import javax.swing.*;
import proyecto_parcial_1_vampire_wargame.Player;

/**
 *
 * @author esteb
 */
public class MiPerfil extends JFrame{
    private Player playerActual;
    
    public MiPerfil(){
        setTitle("Mi Perfil");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        mostrarMiPerfil();
    }
    
    private void mostrarMiPerfil(){
        
    }
}
