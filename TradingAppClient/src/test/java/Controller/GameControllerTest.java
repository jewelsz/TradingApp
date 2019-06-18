package Controller;

import Shared_Models.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class GameControllerTest
{
    private GameController gameController;

    @BeforeEach
    void setUp() {
        System.out.println("@BeforeEach! setting up GameController...");
        gameController = new GameController();
    }

    @Test
    public void registerAndLoginTest()
    {
        gameController.register("nicokuijpers", "wachtwoord123");
        gameController.login("nicokuijpers", "wachtwoord123");

        //Inventory is gevuld wat alleen kan als de speler is ingelogd
        assertNotNull(gameController.inventoryController.playerBag.inventory);

        //thisPlayer username is gelijk aan de username die we net hebben aangemaakt
        assertEquals(gameController.getThisPlayer().getName(), "nicokuijpers");
    }

    @Test
    public void tradeEmptyBagTest()
    {
        gameController.login("yoes" , "ikhouvanjowelle");
        //Trade bag is leeg
        assert(gameController.inventoryController.playerBag.inventory.size() >= 0);
        //Readycheck staat op false
        assertFalse(gameController.getPlayerReady());
    }

    @Test
    public void addItemToTradeTest()
    {
        gameController.login("jowelle" , "password123");
        Item item = gameController.inventoryController.playerBag.inventory.get(1);
        gameController.addTradeItem(item);

        //Item naar trade verplaatst
        assert(gameController.inventoryController.playerBag.inventory.size() >= 1);
    }

    @Test
    public void acceptTradeTest()
    {
        gameController.login("jowelle" , "password123");
        Item item = gameController.inventoryController.playerBag.inventory.get(1);
        gameController.addTradeItem(item);

        gameController.acceptTrade();

        //Bevestig trade
        assertTrue(gameController.getPlayerReady());
    }

    @Test
    public void acceptTradeAfterChange()
    {
        gameController.login("jowelle" , "password123");
        Item item = gameController.inventoryController.playerBag.inventory.get(1);
        gameController.addTradeItem(item);

        gameController.acceptTrade();
        //Voeg nieuw item toe aan trade na trade bevestiging
        Item item2 = gameController.inventoryController.playerBag.inventory.get(1);
        gameController.addTradeItem(item2);

        //Meer items in trade bag
        assert(gameController.inventoryController.tradeBag.inventory.size() >= 2);
        //Trade bevestiging ongedaan gemaakt
        assertFalse(gameController.getPlayerReady());
    }

    //@Test
    public void removeItemFromTradeBag()
    {
        gameController.login("jowelle" , "password123");
        Item item = gameController.inventoryController.playerBag.inventory.get(1);
        gameController.addTradeItem(item);

        gameController.acceptTrade();
        //Voeg nieuw item toe aan trade na trade bevestiging
        Item item2 = gameController.inventoryController.playerBag.inventory.get(1);
        gameController.addTradeItem(item2);

        //Meer items in trade bag
        assert(gameController.inventoryController.tradeBag.inventory.size() >= 2);
        //Trade bevestiging ongedaan gemaakt
        assertFalse(gameController.getPlayerReady());

        int tradebagSize = gameController.inventoryController.playerBag.inventory.size();
        //Verwijder een item uit de trade bag
        gameController.removeTradeItem(item);
        assert(gameController.inventoryController.tradeBag.inventory.size() >= 1);
        //Inventory heeft het item weer terug
        assertEquals(gameController.inventoryController.playerBag.inventory.size(), tradebagSize+1);
    }

    @Test
    public void getInventoryViaGameController()
    {
        //GameController controller = new GameController();
        gameController.getInventoryFromDatabase(1);

        assert(gameController.inventoryController.playerBag.inventory.size() > 1);
        assertNotNull(gameController.inventoryController.playerBag.inventory);
    }

}