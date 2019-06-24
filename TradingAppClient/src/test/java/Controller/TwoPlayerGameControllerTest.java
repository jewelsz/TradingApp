package Controller;

import DatabaseCommunicator.DatabaseController;
import Shared_Models.Item;
import com.sun.javafx.application.PlatformImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TwoPlayerGameControllerTest
{
    private static DatabaseController databaseController;

    private static GameController henksGame;
    private static GameController bobsGame;

    private static GUIController henksGUIController;
    private static GUIController bobsGUIController;

    private List<Item> henksItems;
    private List<Item> bobsItems;

//    @Before
//    public void init()
//    {
//        databaseController = new DatabaseController();
//
//        henksGame = new GameController();
//        bobsGame = new GameController();
//        henksGame.login("Henk", "wachtwoord123");
//        bobsGame.login("Bob", "123wachtwoord");
//
//        henksGame.subscribe(bobsGame.getThisPlayer().getName());
//        bobsGame.subscribe(henksGame.getThisPlayer().getName());
//    }

    @BeforeAll
    public static void setup()
    {
        databaseController = new DatabaseController();

//        henksGame = new GameController();
//        bobsGame = new GameController();

        henksGUIController = new GUIController();
        bobsGUIController = new GUIController();

        PlatformImpl.startup(() -> {});

        henksGame = henksGUIController.gameController;
        bobsGame = bobsGUIController.gameController;

        henksGame.login("Henk", "wachtwoord123");
        bobsGame.login("Bob", "123wachtwoord");

        henksGame.subscribe(bobsGame.getThisPlayer().getName());
        bobsGame.subscribe(henksGame.getThisPlayer().getName());

    }

    @AfterAll
    public static void cleanUp(){
//        DatabaseController db = new DatabaseController();
//        db.removePlayer("Henk");
//        db.removePlayer("Bob");
    }

    @Test
    public void loginTest()
    {
        //Log in al Henk en bob. Deze gegevens kloppen
       // henksGame.login("Henk", "wachtwoord123");
       // bobsGame.login("Bob", "123wachtwoord");

        //Henk id = 106   Bob id = 107
        assertEquals(106, henksGame.getThisPlayer().getId());
        assertEquals(107, bobsGame.getThisPlayer().getId());

        System.out.println("TEST NAME: "+ bobsGame.getThisPlayer().getName());
    }

    @Test
    public void subscribeToPlayerTest()
    {
//
//        henksGame.getSession();
//        bobsGame.getSession();
//
//        int henkSubscribersAfter = henksGame.totalSessions;
//        int bobSubscribersAfter = bobsGame.totalSessions;
//
//        System.out.println("AANTAL SUBSCRIBED: "+henkSubscribersAfter);
//        System.out.println("AANTAL SUBSCRIBED: "+bobSubscribersAfter);
//        assert (henkSubscribersAfter > 0);
//        assert (bobSubscribersAfter > 0);
    }

    @Test
    public void playerItemBagTest() throws InterruptedException {
        henksItems = henksGame.inventoryController.playerBag.inventory;
        assert(henksItems.size()>1);
        System.out.println("TEST BAG ITEMS: "+ henksItems);
        assertEquals(henksItems.size(), 4);

        bobsItems = bobsGame.inventoryController.playerBag.inventory;
        assert(bobsItems.size()>1);
        assertEquals(bobsItems.size(), 4);
        System.out.println("TEST BEFORE ITEM: "+henksItems);

        addItemsToTradeTest();
    }

    public void addItemsToTradeTest() throws InterruptedException {
        System.out.println("TEST ITEM: "+henksItems);
        henksGame.addTradeItem(henksGame.inventoryController.playerBag.inventory.get(1));
        TimeUnit.SECONDS.sleep(2);
        assertEquals(1, bobsGame.inventoryController.opponentBag.inventory.size());
//        assertEquals(0, henksGame.inventoryController.opponentBag.inventory.size());
        System.out.println("Henks opponent items: "+ henksGame.inventoryController.opponentBag.inventory);
        System.out.println("bobs opponent items: "+ bobsGame.inventoryController.opponentBag.inventory);
    }


    public void tradingItemsTest()
    {

        for(Item i : henksGame.inventoryController.playerBag.inventory)henksGame.addTradeItem(i);
        //for(Item b : bobsGame.inventoryController.playerBag.inventory) bobsGame.addTradeItem(b);

        henksGame.acceptTrade();

        assertTrue(bobsGame.getOpponentReady());

        bobsGame.acceptTrade();
    }

}