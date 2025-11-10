package proyecto_parcial_1_vampire_wargame;

import java.util.Date;

public class GameLog {

    private String ganador;
    private String perdedor;
    private String resultado; // descripción libre: p.ej. "ana venció a juan" o "Empate entre ana y juan"
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

    public String getResultado() {
        return resultado;
    }

    public String getFecha() {
        return fecha;
    }

    /**
     * Devuelve el oponente del username en este log.
     * Si username es el ganador -> devuelve perdedor.
     * Si username es el perdedor -> devuelve ganador.
     * Si username no participa -> devuelve "".
     */
    public String getOponente(String username) {
        if (username == null) return "";
        if (ganador != null && username.equalsIgnoreCase(ganador)) {
            return perdedor == null ? "" : perdedor;
        }
        if (perdedor != null && username.equalsIgnoreCase(perdedor)) {
            return ganador == null ? "" : ganador;
        }
        return "";
    }

    /**
     * Resultado relativo para el username: "Victoria", "Derrota", "Empate" o "" si no participó.
     * Detecta empate si la cadena resultado contiene "empate" (ignora mayúsculas).
     */
    public String getResultadoPara(String username) {
        if (username == null) return "";
        // detectar empate por texto (mantén la costumbre de poner "Empate" en resultado si hay empate)
        if (resultado != null && resultado.toLowerCase().contains("empate")) {
            if ((ganador != null && username.equalsIgnoreCase(ganador)) ||
                (perdedor != null && username.equalsIgnoreCase(perdedor))) {
                return "Empate";
            } else {
                return "";
            }
        }
        if (ganador != null && username.equalsIgnoreCase(ganador)) {
            return "Victoria";
        }
        if (perdedor != null && username.equalsIgnoreCase(perdedor)) {
            return "Derrota";
        }
        return "";
    }

    @Override
    public String toString() {
        // mantengo toString como descripción legible (la que ya usabas en Reportes)
        return resultado == null ? "" : resultado;
    }
}
