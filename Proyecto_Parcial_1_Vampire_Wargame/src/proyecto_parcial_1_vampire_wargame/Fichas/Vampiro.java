/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Fichas;

/**
 *
 * @author esteb
 */

public class Vampiro extends Pieza {

    public Vampiro(String color) {
        super(color, 3, 4, 5);
        this.nombre = "Vampiro";
    }

    @Override
    public String getTipo() {
        return "Vampiro";
    }

    public String ataqueNormal(Pieza objetivo) {
        if (objetivo == null) return "No hay objetivo.";
        objetivo.recibirDanio(this.ataque);
        return "Vampiro ataca normalmente a " + objetivo.getTipo() + " y le quita " + this.ataque + " puntos.";
    }

    public String chuparSangre(Pieza objetivo) {
        if (objetivo == null) return "No hay objetivo para chupar sangre.";

        objetivo.recibirDanio(1);
        this.vida += 1;

        return "Vampiro chupa sangre a " + objetivo.getTipo()
             + ": -1 al objetivo, +1 vida al vampiro";
    }
}
