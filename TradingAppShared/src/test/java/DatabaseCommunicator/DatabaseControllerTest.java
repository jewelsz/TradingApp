package DatabaseCommunicator;

import Messages.ItemResponse;
import Messages.PlayersList;
import Models.Item;
import Models.Player;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

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

   // @Test
    public void updateItemsFromInventory()
    {
        //Haal de items op van de speler met het id 4
        ItemResponse inventory = dbController.getInventory(4);
        ItemResponse inventoryOther = dbController.getInventory(3);
        List<Item> items = inventory.getItems();

        int invSize = inventory.getItems().size();
        int invSizeOther = inventoryOther.getItems().size();

        int totalSize = invSize+invSizeOther;
        //Verplaats deze items naar het inventory van de speler met het id 5
        dbController.updateItemsFromInventory(items,4);
        int newSize = dbController.getInventory(4).getItems().size();
        //invSize + invSizeOther is even groot als de nieuwe inventory size van speler met id 5
        assertEquals(totalSize, newSize);
    }
}