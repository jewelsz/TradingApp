package DatabaseCommunicator;

import Models.Player;

public interface IDatabaseCommunication
{
    void insertPlayer(Player player);
    Player getPlayer(String name, String password);
}
