/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Fichas;

/**
 *
 * @author esteb
 */
public class HombreLobo extends Pieza{
    public HombreLobo(String color){
        super(color, 5, 4, 5);
        this.nombre= "Hombre Lobo";
    }
    
    public String getTipo(){
        return "HombreLobo";
    }
    
    public String ataqueNormal(Pieza objetivo){
        if(objetivo == null)
            return "No hay objetivo";
        objetivo.recibirDanio(ataque);
        return "Hombre Lobo ataca a " + objetivo.getTipo() + " y le quita " + this.ataque + " de puntos de vida"; 
    }
    
    public boolean moverDosCasillas(){
        return true;
    }
    
}
