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
public class Manager implements Almacenamiento{

    private List<Player> players;
    private List<GameLog> logs;

    public Manager(){
        this.players = new ArrayList<>();
        this.logs = new ArrayList<>();
    }
    
    public boolean playerHabilitado(String username){
        for(Player player: players){
            if(player.getUsername().equals(username))
                return true;
        }
        return false;
    }
    
    public void agregarPlayer(Player player){
        if(!playerHabilitado(player.getUsername())){
            players.add(player);
        }
    }
    
    public Player getPlayer(String username){
        for(Player player: players){
            if(player.getUsername().equals(username) && player.isActivo())
                return player;
        }
        return null;
    }
    
    public List<Player> getAllPlayers(){
        List<Player> activos = new ArrayList<>();
        for(Player player: players){
            if(player.isActivo())
                activos.add(player);
        }
        return activos;
    }

    @Override
    public void actualizarPlayer(Player player) {
        
    }

    @Override
    public void eliminarPlayer(String username) {

    }

    @Override
    public void agregarLog(GameLog log) {
        
    }

    @Override
    public List<GameLog> getAllLogs(String username) {
        List<GameLog> resultado = new ArrayList<>();
        for (GameLog log : logs) {
            if (log.getGanador().equals(username) || log.getPerdedor().equals(username)) {
                resultado.add(0, log);
            }
        }
        return resultado;
    }
    
    
    
}
