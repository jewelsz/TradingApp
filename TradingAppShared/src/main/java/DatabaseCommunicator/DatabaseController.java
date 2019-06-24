package DatabaseCommunicator;

import Shared_Models.Item;
import Shared_Models.Player;
import Messages.PlayersList;
import Messages.ItemResponse;

import java.util.List;

public class DatabaseController
{
    private DatabaseConnection dbConnection;
    private IDatabasePlayerCommunication playerCommunication;
    private IDatabaseItemCommunication itemCommunication;
    private static DatabaseController instance;

    public int sessionTotal;

    public DatabaseController()
    {
        dbConnection = new DatabaseConnection();
        playerCommunication = new DatabasePlayerService(dbConnection);
        itemCommunication = new DatabaseItemsService(dbConnection);

        sessionTotal = 0;
    }

    public static DatabaseController getInstance()
    {
        if (instance == null)
        {
            instance = new DatabaseController();
        }
        return instance;
    }

    public void addRegistration(Player player)
    {
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

    //Trade
    public void updateItemsFromInventory(List<Item> items, int playerid)
    {
        for(Item i : items)
        {
            System.out.println("Trade - Player id: "+ playerid +" item: "+ i.getItemName() );
            itemCommunication.updateItemsFromInventory(i,playerid);
        }
    }

    public void addItemToPlayer(int itemid, int playerid)
    {
        itemCommunication.addItemToPlayer(itemid, playerid);
    }

    public void removePlayer(String name)
    {
        playerCommunication.removePlayer(name);
    }

    public int getSessionTotal() {
        return sessionTotal;
    }

    public void setSessionTotal(int sessionTotal) {
        this.sessionTotal = sessionTotal;
    }
}
