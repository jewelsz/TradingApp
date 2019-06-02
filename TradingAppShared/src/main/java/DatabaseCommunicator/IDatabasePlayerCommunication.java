package DatabaseCommunicator;

import Models.Player;

public interface IDatabasePlayerCommunication
{
    void insertPlayer(Player player);
    Player getPlayer(String name, String password); //login
}
