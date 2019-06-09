package DatabaseCommunicator;

import Models.Item;
import Models.Player;
import Models.PlayersList;
import Models.ItemResponse;

import java.util.ArrayList;
import java.util.List;

public class DatabaseController
{
    IDatabasePlayerCommunication playerCommunication = new DatabasePlayerCommunicator();
    IDatabaseItemCommunication itemCommunication = new DatabaseItemsCommunicator();
    private static DatabaseController instance;

    public DatabaseController() {
    }

    //TODO put registration in database.

    public static DatabaseController getInstance()
    {
        if (instance == null)
        {
            instance = new DatabaseController(); // zet hier de dingen die aangemaakt moeten worden in de constructor
        }
        return instance;
    }

    public void addRegistration(Player player)
    {
        //TODO add to database.
        System.out.println("Put in db");
        playerCommunication.insertPlayer(player);
    }

    public Player getPlayer(String name, String password)
    {
        Player p = playerCommunication.getPlayer(name, password);

        return p;
    }

    public ItemResponse getInventory(int playerid)
    {
        List<Item> inventory = itemCommunication.getInventory(playerid);
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setItems(inventory);
        return itemResponse;
    }

    public PlayersList getAllPlayers()
    {
        List<Player> players = playerCommunication.getAllPlayers();
        PlayersList playersList = new PlayersList(players);
        return playersList;
    }

    public boolean tradeCheck(List<Item> items, int playerid)
    {
        boolean check = true;
        for(Item i : items)
        {
            if(itemCommunication.getPlayerIDFromInventory(i.getInventoryId()) != playerid)
            {
                check = false;
            }
        }

        return check;
    }

    public void deleteFromInventory(ArrayList<Item> items, int playerid)
    {
        itemCommunication.removeItemsFromInventory(items);
    }

    //Trade
    public void updateItemsFromInventory(List<Item> items, int playerid)
    {
        itemCommunication.updateItemsFromInventory(items, playerid);
    }

}
