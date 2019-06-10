package REST;

import Models.Item;
import Models.Player;

import java.util.List;

public class RESTClientCommunicatorController
{
    RESTClientCommunicator communicator;

    public RESTClientCommunicatorController()
    {
        communicator = new RESTClientCommunicator();
    }

    public List<Item> getInventory(int playerid)
    {
        List<Item> inventory = communicator.getInventory(playerid).getItems();
        return inventory;
    }

    public List<Player> getAllPlayers()
    {
        List<Player> players = communicator.getAllPlayers().getPlayers();
        return players;
    }

    public boolean tradeItems(List<Item> items, int playerid)
    {
        return communicator.tradeItems(items, playerid);
    }

    public void register(String username, String password)
    {
        Player player = new Player(username, password);
        communicator.postRegistration(player);
    }

    public Player Login(String username, String password)
    {
        Player player = communicator.getPlayer(username, password);
        return player;
    }
}
