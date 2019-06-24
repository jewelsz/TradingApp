package REST;

import Interfaces.IItemService;
import Interfaces.IplayerService;
import Shared_Models.Item;
import Shared_Models.Player;

import java.util.List;

public class RESTClientCommunicatorController
{
    IplayerService playerService;
    IItemService itemService;

    public RESTClientCommunicatorController()
    {
        playerService = new RESTClientCommunicator();
        itemService = new RESTClientCommunicator();
    }

    public List<Item> getInventory(int playerid)
    {
        List<Item> inventory = itemService.getInventory(playerid).getItems();
        return inventory;
    }

    public List<Player> getAllPlayers()
    {
        List<Player> players = playerService.getAllPlayers().getPlayers();
        return players;
    }

//    public boolean tradeItems(List<Item> items, int playerid)
//    {
//        return communicator.tradeItems(items, playerid);
//    }

    public void register(String username, String password)
    {
        Player player = new Player(username, password);
        playerService.postRegistration(player);
    }

    public Player Login(String username, String password)
    {
        Player player = playerService.getPlayer(username, password);
        return player;
    }
}
