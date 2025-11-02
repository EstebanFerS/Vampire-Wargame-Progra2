/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Fichas;

/**
 *
 * @author esteb
 */

public class Zombie extends Pieza {

    public Zombie(String color) {
        super(color, 1, 1, 0);
        this.nombre = "Zombie";
    }

    @Override
    public String getTipo() {
        return "Zombie";
    }

    public String ataqueOrdenado(Pieza objetivo) {
        if (objetivo == null) return "No hay objetivo para que el zombie ataque.";
        objetivo.recibirDanio(this.ataque);
        return "Zombie ataca por orden de Muerte y le quita " + this.ataque + " al objetivo.";
    }
}
