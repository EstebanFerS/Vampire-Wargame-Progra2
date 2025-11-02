/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Fichas;

/**
 *
 * @author esteb
 */

/**
 * Muerte (Necromancer)
 * - Ataque normal
 * - Lanzar lanza: ignora escudo (usa recibirDanioSinEscudo)
 * - Conjurar zombie: crea un Zombie; la colocación en tablero la gestiona Tablero/UI.
 *
 * Comportamiento configurable:
 * - Por defecto permite conjurar ilimitados (maxZombies = Integer.MAX_VALUE).
 * - Puedes limitar con setMaxZombies(n) y/o resetear contador con resetZombiesConjurados().
 */
public class Muerte extends Pieza {

    // Por defecto, ilimitado.
    private long zombiesConjurados = 0;
    private long maxZombies = Integer.MAX_VALUE; // si quieres ilimitado: mantener Integer.MAX_VALUE

    public Muerte(String color) {
        super(color, 4, 5, 4); // ataque=4, vida=5, escudo=4 (ajustable)
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

    /**
     * Lanzar lanza: ignora escudo, quita la mitad del ataque (redondeo hacia abajo).
     */
    public String lanzarLanza(Pieza objetivo) {
        if (objetivo == null) return "No hay objetivo para la lanza.";
        int danio = Math.max(1, this.ataque / 2);
        objetivo.recibirDanioSinEscudo(danio);
        return "Muerte lanza su lanza a " + objetivo.getTipo() + " y le quita " + danio + " vidas (ignora escudo).";
    }

    /**
     * Conjura un zombie: devuelve un nuevo Zombie si todavía se permite conjurar según maxZombies.
     * La colocación en tablero debe hacerla la capa superior (Tablero/UI). Si no es posible
     * devolverá null (se recomienda que UI muestre mensaje al jugador).
     */
    public Zombie conjurarZombie() {
        if (zombiesConjurados >= maxZombies) {
            // ya alcanzó el máximo permitido
            return null;
        }
        zombiesConjurados++;
        return new Zombie(this.color);
    }

    /** Permite configurar un máximo de zombies. Poner Integer.MAX_VALUE para ilimitado. */
    public void setMaxZombies(long max) {
        if (max < 0) max = 0;
        this.maxZombies = max;
    }

    public long getMaxZombies() {
        return maxZombies;
    }

    public long getZombiesConjurados() {
        return zombiesConjurados;
    }

    /** Resetea el contador (útil al iniciar una nueva partida). */
    public void resetZombiesConjurados() {
        zombiesConjurados = 0;
    }
}
