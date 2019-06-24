package Controller;

import DatabaseCommunicator.DatabaseController;
import Shared_Models.Item;
import com.sun.javafx.application.PlatformImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        henksGUIController = new GUIController();
        bobsGUIController = new GUIController();

        //PlatformImpl.startup(() -> {});

        henksGame = henksGUIController.gameController;
        bobsGame = bobsGUIController.gameController;

        henksGame.login("Henk", "wachtwoord123");
        bobsGame.login("Bob", "123wachtwoord");

        henksGame.subscribe(bobsGame.getThisPlayer().getName());
        bobsGame.subscribe(henksGame.getThisPlayer().getName());

    }

    @BeforeEach
    void init()
    {
        henksGame.getInventoryFromDatabase(106);
        bobsGame.getInventoryFromDatabase(107);

        henksItems = henksGame.inventoryController.playerBag.inventory;
        bobsItems = bobsGame.inventoryController.playerBag.inventory;
    }

    @AfterAll
    public static void cleanUp(){

    }

    @Test
    public void loginTest()
    {
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
        //Test bag is niet leeg:
        assert(henksItems.size()>1);
        System.out.println("TEST HENKS INVENTORY: "+ henksItems);
        assertEquals( 4, henksItems.size());

        assert(bobsItems.size()>1);
        assertEquals( 4, bobsItems.size());
        System.out.println("TEST BOBS INVENTORY: "+bobsItems);

        //addItemsToTradeTest();
    }

    @Test
    public void addItemsToTradeTest() throws InterruptedException {
        System.out.println("TEST ITEM: "+henksItems);
        System.out.println("TEST ITEM: "+bobsItems);
        henksGame.addTradeItem(henksGame.inventoryController.playerBag.inventory.get(1));
        TimeUnit.SECONDS.sleep(2);
// --add-modules javafx.application        assertEquals(1, henksGame.inventoryController.opponentBag.inventory.size());
        assertEquals(1, henksGame.inventoryController.tradeBag.inventory.size());
        assertEquals(3, henksGame.inventoryController.playerBag.inventory.size());
        System.out.println("bobs opponent items: "+ bobsGame.inventoryController.opponentBag.inventory);
    }

}