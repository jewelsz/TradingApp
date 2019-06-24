package DatabaseCommunicator;

import Messages.ItemResponse;
import Messages.PlayersList;
import Shared_Models.Item;
import Shared_Models.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class DatabaseControllerTest
{
    DatabaseController dbController = new DatabaseController();
    @Test
    public void addRegistrationTest()
    {
        //Maak een speler aan en voeg deze toe aan het database via de controller class
        Player newPlayer = new Player("Coolusername", "geheimpass");
        dbController.addRegistration(newPlayer);

        //Haal de gebruiker op
        assertNotNull(dbController.getPlayer(newPlayer.getName(), newPlayer.getPassword()));
    }

    @Test
    public void getPlayer()
    {
        //Haal de gebruiker Jowelle op
        assertNotNull(dbController.getPlayer("jowelle", "password123"));
    }

    @Test
    public void getInventory()
    {
        //Haal de inventory op van de gebruiker met het id 3
        ItemResponse inventory = dbController.getInventory(22);
        //Tel de items in de inventory. Dit moeten er 6 zijn.
        assertEquals(inventory.getItems().size(), 6);
    }

    @Test
    public void getAllPlayers()
    {
        PlayersList players = dbController.getAllPlayers();
        assertNotNull(players.getPlayers());
    }

    @Test
    public void updateItemsFromInventory()
    {
        //Haal de items op van de spelers met het id 106 en 107
        ItemResponse inventory = dbController.getInventory(106);
        ItemResponse inventoryOther = dbController.getInventory(107);
        List<Item> items = inventory.getItems();
        List<Item> itemsOther = inventoryOther.getItems();

        System.out.println("INVENTORY: "+items);
        System.out.println("INVENTORY OTHER: "+itemsOther);

        int invSize = inventory.getItems().size();
        int invSizeOther = inventoryOther.getItems().size();

        int totalSize = invSize+invSizeOther;
        //Verplaats deze items naar het inventory van de speler met het id 5
        dbController.updateItemsFromInventory(items,107);
        dbController.updateItemsFromInventory(itemsOther,106);

        List<Item> newItems = dbController.getInventory(106).getItems();
        List<Item> newOtherItems = dbController.getInventory(107).getItems();

        System.out.println("INVENTORY: "+ dbController.getInventory(106));
        System.out.println("INVENTORY OTHER: "+ dbController.getInventory(107));
        int newSize = dbController.getInventory(4).getItems().size();
        //invSize + invSizeOther is even groot als de nieuwe inventory size van speler met id 5

        //itemTradeCheck moet 4 zijn
        int itemTradeCheck = 0;
        for(Item i : items)
        {
            for(Item b : newOtherItems)
            {
                if(i.getInventoryId() == b.getInventoryId())
                {
                    itemTradeCheck++;
                }
            }
        }
        //De count moet 4 zijn
        assertEquals(4, itemTradeCheck);

        //De size moet 4 zijn
        assertEquals(4, newOtherItems.size());
    }
}