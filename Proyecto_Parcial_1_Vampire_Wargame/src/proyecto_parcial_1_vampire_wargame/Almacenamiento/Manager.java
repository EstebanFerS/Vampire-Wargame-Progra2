/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Almacenamiento;
import java.util.ArrayList;
import java.util.List;
import proyecto_parcial_1_vampire_wargame.GameLog;
import proyecto_parcial_1_vampire_wargame.Player;

/**
 *
 * @author esteb
 */
public class Manager implements Almacenamiento {

    private static Manager instance;
    private List<Player> players;
    private List<GameLog> logs;

    public Manager() {
        this.players = new ArrayList<>();
        this.logs = new ArrayList<>();
    }

    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }
    
    public boolean playerHabilitado(String username) {
        if (username == null) {
            return false;
        }
        for (Player player : players) {
            if (player.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public void agregarPlayer(Player player) {
        if (player == null || player.getUsername() == null) {
            return;
        }

        if (!playerHabilitado(player.getUsername())) {
            players.add(player);
        }
    }

    public Player getPlayer(String username) {
        if (username == null) {
            return null;
        }
        for (Player player : players) {
            if (player.getUsername().equalsIgnoreCase(username) && player.isActivo()) {
                return player;
            }
        }
        return null;
    }

    public List<Player> getAllPlayers() {
        List<Player> activos = new ArrayList<>();
        for (Player player : players) {
            if (player.isActivo()) {
                activos.add(player);
            }
        }
        return activos;
    }

    @Override
    public void actualizarPlayer(Player player) {
        if (player == null || player.getUsername() == null) {
            return;
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equalsIgnoreCase(player.getUsername())) {
                players.set(i, player);
                return;
            }
        }
    }

    @Override
    public void eliminarPlayer(String username) {
        if (username == null) {
            return;
        }

        for (Player player : players) {
            if (player.getUsername().equalsIgnoreCase(username)) {
                player.setActivo(false);
                return;
            }
        }
    }

    @Override
    public void agregarLog(GameLog log) {
        if (log == null) {
            return;
        }
        logs.add(log);
    }

    @Override
    public List<GameLog> getAllLogs(String username) {
        List<GameLog> resultado = new ArrayList<>();
        if (username == null) {
            return resultado;
        }
        for (GameLog log : logs) {
            if (log.getGanador().equalsIgnoreCase(username) || log.getPerdedor().equalsIgnoreCase(username)) {
                resultado.add(0, log);
            }
        }
        return resultado;
    }
}
