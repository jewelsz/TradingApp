package Controller;

import Shared_Models.Item;
import Shared_Models.Player;
import REST.RESTClientCommunicatorController;
import WebsocketsClient.WebsocketsMessageController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Messages.TradeItemMessage;

import java.util.Observable;
import java.util.Observer;

public class GameController implements Observer
{
    public ObservableList<Player> playerList = FXCollections.observableArrayList();

    public InventoryController inventoryController;

    private Boolean playerReady;
    private Boolean opponentReady;

    private Player thisPlayer;

    private RESTClientCommunicatorController RESTController;
    private WebsocketsMessageController wsController;


    public GameController()
    {
        playerReady = false;
        opponentReady = false;

        RESTController = new RESTClientCommunicatorController();
        wsController = new WebsocketsMessageController(this);
        inventoryController = new InventoryController();
        playerList.setAll(RESTController.getAllPlayers());
    }

    public Boolean getPlayerReady() {
        return playerReady;
    }

    public Player getThisPlayer() {
        return thisPlayer;
    }

    public void addTradeItem(Item item)
    {
        playerReady = false;
        inventoryController.tradeBag.addItem(item);
        inventoryController.playerBag.removeItem(item);
        wsController.addTradeItem(item, thisPlayer.getName());
    }

    public void removeTradeItem(Item item)
    {
        inventoryController.playerBag.removeItem(item);
        inventoryController.playerBag.addItem(item);
        wsController.removeTradeItem(item, thisPlayer.getName());
    }

    public void acceptTrade()
    {
        if(inventoryController.tradeReadyCount())
        {
            playerReady = true;
            wsController.acceptTrade(thisPlayer.getName());
            if (playerReady && opponentReady) tradeItems();
        }
    }

    public void opponentAcceptTrade()
    {
        opponentReady = true;
        if(opponentReady && playerReady){tradeItems();}
        playerReady = false;
        opponentReady = false;
    }

    private void tradeItems()
    {
        System.out.println("TRADING ITEMS STARTED");
        wsController.tradeItems(inventoryController.tradeItems(), thisPlayer.getId(), thisPlayer.getName());
    }

    public void getInventoryFromDatabase(int playerid)
    {
        inventoryController.playerBag.inventory.setAll(RESTController.getInventory(playerid));
        inventoryController.playerBag.inventory.setAll(RESTController.getInventory(playerid));
    }

    public String login(String username, String password)
    {
        thisPlayer = RESTController.Login(username, password);
        if(thisPlayer.getId() != 0)
        {
            getInventoryFromDatabase(thisPlayer.getId());
            wsController.register(thisPlayer.getName());

        }
        return thisPlayer.getName();
    }

    public void register(String username, String password)
    {
        RESTController.register(username, password);
    }

    public void subscribe(String player)
    {
        System.out.println("CLIENT SUBSCRIBE TO " + player);
        wsController.subscribe(player);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        TradeItemMessage message = (TradeItemMessage) arg;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
        if (null != message.getOperation())
        {
            switch (message.getOperation())
            {
                case ACCEPT:
                    opponentAcceptTrade();
                    System.out.println("Opponent accepted trade");
                    break;
                case TRADE:
                    inventoryController.newTradeItems(RESTController.getInventory(thisPlayer.getId()));
                    System.out.println("Opponent trade items");
                    break;
                case ADD:
                    playerReady = false;
                    inventoryController.opponentBag.addItem(message.getItem());
                    System.out.println("Opponent added item");
                    break;
                case REMOVE:
                    playerReady = false;
                    inventoryController.opponentBag.inventory.remove(message.getItem());
                    System.out.println("Opponent removed item");
                    break;

                default:
                    System.out.println("ERROR: GameController update");
                    break;
            }
        }
            }
        });
    }
}
