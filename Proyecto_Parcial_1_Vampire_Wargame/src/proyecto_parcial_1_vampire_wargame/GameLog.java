/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame;

import java.util.Date;

/**
 *
 * @author esteb
 */
public class GameLog {

    private String ganador;
    private String perdedor;
    private String resultado;
    private String fecha;

    public GameLog(String ganador, String perdedor, String resultado) {
        this.ganador = ganador;
        this.perdedor = perdedor;
        this.resultado = resultado;
        this.fecha = new Date().toString();
    }

    public String getGanador() {
        return ganador;
    }

    public String getPerdedor() {
        return perdedor;
    }

    @Override
    public String toString() {
        return resultado;
    }
}
