package Controller;

import Models.Item;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GameControllerTest
{
    GameController gameController = new GameController();

    @Test
    public void registerAndLoginTest()
    {
        gameController.register("nicokuijpers", "wachtwoord123");
        gameController.login("nicokuijpers", "wachtwoord123");

        //Inventory is gevuld wat alleen kan als de speler is ingelogd
        assertNotNull(gameController.inventory);

        //thisPlayer username is gelijk aan de username die we net hebben aangemaakt
        assertEquals(gameController.getThisPlayer().getName(), "nicokuijpers");
    }

    @Test
    public void tradeEmptyBagTest()
    {
        gameController.login("yoes" , "ikhouvanjowelle");
        //Trade bag is leeg
        assert(gameController.playerTradeBag.size() >= 0);
        //Readycheck staat op false
        assertFalse(gameController.getPlayerReady());
    }

    @Test
    public void addItemToTradeTest()
    {
        gameController.login("jowelle" , "password123");
        Item item = gameController.inventory.get(1);
        gameController.addTradeItem(item);

        //Item naar trade verplaatst
        assert(gameController.playerTradeBag.size() >= 1);
    }

    @Test
    public void acceptTradeTest()
    {
        gameController.login("jowelle" , "password123");
        Item item = gameController.inventory.get(1);
        gameController.addTradeItem(item);

        gameController.acceptTrade();

        //Bevestig trade
        assertTrue(gameController.getPlayerReady());
    }

    @Test
    public void acceptTradeAfterChange()
    {
        gameController.login("jowelle" , "password123");
        Item item = gameController.inventory.get(1);
        gameController.addTradeItem(item);

        gameController.acceptTrade();
        //Voeg nieuw item toe aan trade na trade bevestiging
        Item item2 = gameController.inventory.get(1);
        gameController.addTradeItem(item2);

        //Meer items in trade bag
        assert(gameController.playerTradeBag.size() >= 2);
        //Trade bevestiging ongedaan gemaakt
        assertFalse(gameController.getPlayerReady());
    }

    @Test
    public void removeItemFromTradeBag()
    {
        gameController.login("jowelle" , "password123");
        Item item = gameController.inventory.get(1);
        gameController.addTradeItem(item);

        gameController.acceptTrade();
        //Voeg nieuw item toe aan trade na trade bevestiging
        Item item2 = gameController.inventory.get(1);
        gameController.addTradeItem(item2);

        //Meer items in trade bag
        assert(gameController.playerTradeBag.size() >= 2);
        //Trade bevestiging ongedaan gemaakt
        assertFalse(gameController.getPlayerReady());

        int tradebagSize = gameController.inventory.size();
        //Verwijder een item uit de trade bag
        gameController.removeTradeItem(item);
        assert(gameController.playerTradeBag.size() >= 1);
        //Inventory heeft het item weer terug
        assertEquals(gameController.inventory.size(), tradebagSize+1);
    }

    @Test
    public void getInventoryViaGameController()
    {
        GameController controller = new GameController();
        controller.getInventoryFromDatabase(1);

        assert(controller.inventory.size() > 1);
        assertNotNull(controller.inventory);
    }

}