/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Almacenamiento;
import java.util.List;
import proyecto_parcial_1_vampire_wargame.GameLog;
import proyecto_parcial_1_vampire_wargame.Player;
/**
 *
 * @author esteb
 */
public interface Almacenamiento {
    void agregarPlayer(Player player);
    Player getPlayer(String username);
    List<Player> getAllPlayers();
    void actualizarPlayer(Player player);
    void eliminarPlayer(String username);
    boolean playerHabilitado(String username);
    
    void agregarLog(GameLog log);
    List<GameLog> getAllLogs(String username);
}
