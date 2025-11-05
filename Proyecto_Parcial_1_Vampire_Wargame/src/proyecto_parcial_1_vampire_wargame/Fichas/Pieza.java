/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Fichas;

/**
 *
 * @author esteb
 */
public abstract class Pieza {

    protected String nombre;
    protected String color;
    protected int ataque;
    protected int vida;
    protected int escudo;

    public Pieza(String color, int ataque, int vida, int escudo) {
        this.color = color;
        this.ataque = ataque;
        this.vida = vida;
        this.escudo = escudo;
    }

    public String getColor() {
        return color;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getVida() {
        return vida;
    }

    public int getEscudo() {
        return escudo;
    }

    public boolean isVivo() {
        return vida > 0;
    }

    public void recibirDanio(int danio) {
        if (danio <= 0) return;

        if (escudo > 0) {
            int absorbido = Math.min(escudo, danio);
            escudo -= absorbido;
            danio -= absorbido;
        }

        if (danio > 0) {
            vida -= danio;
            if (vida < 0) vida = 0;
        }
    }

    public void recibirDanioSinEscudo(int danio) {
        if (danio <= 0) return;
        vida -= danio;
        if (vida < 0) vida = 0;
    }

    public void curar(int cantidad) {
        if (cantidad <= 0) return;
        vida += cantidad;
    }
    
    public boolean moverDosCasillas() {
        return false;
    }
    
    public abstract String getTipo();

    @Override
    public String toString() {
        return nombre + " [" + color + "] (Vida: " + vida + ", Escudo: " + escudo + ", Atq: " + ataque + ")";
    }
}