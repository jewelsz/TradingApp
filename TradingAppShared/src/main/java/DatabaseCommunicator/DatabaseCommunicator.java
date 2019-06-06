package DatabaseCommunicator;

import Models.Item;
import Models.Player;
import Models.ResponseList;

import java.util.ArrayList;
import java.util.List;

public class DatabaseCommunicator
{
    IDatabasePlayerCommunication playerCommunication = new DatabasePlayer();
    IDatabaseItemCommunication itemCommunication = new DatabaseItems();
    private static DatabaseCommunicator instance;

    public DatabaseCommunicator() {
    }

    //TODO put registration in database.

    public static DatabaseCommunicator getInstance()
    {
        if (instance == null)
        {
            instance = new DatabaseCommunicator(); // zet hier de dingen die aangemaakt moeten worden in de constructor
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

    public ResponseList getInventory(int playerid)
    {
        List<Item> inventory = itemCommunication.getInventory(playerid);
        ResponseList responseList = new ResponseList();
        responseList.setItems(inventory);
        return responseList;
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
