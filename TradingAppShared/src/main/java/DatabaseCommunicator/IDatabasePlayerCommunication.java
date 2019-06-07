package DatabaseCommunicator;

import Models.Player;

import java.util.List;

public interface IDatabasePlayerCommunication
{
    void insertPlayer(Player player);
    Player getPlayer(String name, String password); //login
    List<Player> getAllPlayers();
}
