/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Fichas;

/**
 *
 * @author esteb
 */

public class Muerte extends Pieza {

    public Muerte(String color) {
        super(color, 4, 5, 4);
        this.nombre = "Muerte";
    }

    @Override
    public String getTipo() {
        return "Muerte";
    }

    public String ataqueNormal(Pieza objetivo) {
        if (objetivo == null) return "No hay objetivo.";
        objetivo.recibirDanio(this.ataque);
        return "Muerte ataca normalmente a " + objetivo.getTipo() + " y le quita " + this.ataque + " puntos.";
    }

    public String lanzarLanza(Pieza objetivo) {
        if (objetivo == null) return "No hay objetivo para la lanza.";
        int danio = Math.max(1, this.ataque / 2);
        objetivo.recibirDanioSinEscudo(danio);
        return "Muerte lanza su lanza a " + objetivo.getTipo() + " y le quita " + danio + " vidas (ignora escudo).";
    }

    public Zombie conjurarZombie() {
        return new Zombie(this.color);
    }
}
