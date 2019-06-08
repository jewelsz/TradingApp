package Controller;

import Models.Item;
import Models.Player;
import REST.RESTClientCommunicatorController;
import Websockets.TradingClientEndpoint;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.CommunicatorWebsocketMessage;
import shared.TradeItemMessage;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GameController implements Observer
{
    public ObservableList<Item> inventory = FXCollections.observableArrayList();
    public ObservableList<Item> playerTradeBag = FXCollections.observableArrayList();
    public ObservableList<Item> opponentTradeBag = FXCollections.observableArrayList();
    public ObservableList<Player> playerList = FXCollections.observableArrayList();

    private Boolean playerReady;
    private Boolean opponentReady;

    private Player thisPlayer;

    private RESTClientCommunicatorController RESTController;
    private TradingClientEndpoint websocketCommunicator = null;


    public GameController()
    {
        playerReady = false;
        opponentReady = false;

        RESTController = new RESTClientCommunicatorController();
        websocketCommunicator = TradingClientEndpoint.getInstance();
        websocketCommunicator.addObserver(this);
        websocketCommunicator.start();
        playerList.setAll(RESTController.getAllPlayers());
    }

    public void addTradeItem(Item item)
    {
        playerTradeBag.add(item);
        inventory.remove(item);
        websocketCommunicator.addTradeItem(item, thisPlayer.getName());
    }

    public void removeTradeItem(Item item)
    {
        playerTradeBag.remove(item);
        inventory.add(item);
        websocketCommunicator.removeTradeItem(item, thisPlayer.getName());
    }

    public void acceptTrade()
    {
        if(playerTradeBag.size() >= 1)
        {
            playerReady = true;
            websocketCommunicator.acceptTrade(thisPlayer.getName());
            if (playerReady && opponentReady)
            {
                tradeItems();
            }
        }
    }

    private void tradeItems()
    {
        System.out.println("TRADING ITEMS STARTED");
        websocketCommunicator.tradeItems(playerTradeBag, thisPlayer.getId(), thisPlayer.getName());
    }

    public void getInventoryFromDatabase(int playerid)
    {
        inventory.setAll(RESTController.getInventory(playerid));
    }

    public String login(String username, String password)
    {
        thisPlayer = RESTController.Login(username, password);
        if(thisPlayer.getId() != 0)
        {
            getInventoryFromDatabase(thisPlayer.getId());

            if(websocketCommunicator != null)
            {
               websocketCommunicator.register(thisPlayer.getName());
            }
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
        websocketCommunicator.subscribe(player);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        System.out.println("Observable: " + o + "  object: "+ arg);
        TradeItemMessage message = (TradeItemMessage) arg;
        System.out.println(message);
        System.out.println(message.getOperation());
        if (null != message.getOperation())
        {
            switch (message.getOperation())
            {

                case ACCEPT:
                    // Accept trade
                    opponentReady = true;
                    Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("OPPONENT READY TO TRADE");
                        if(opponentReady && playerReady) tradeItems();
                        playerReady = false;
                        opponentReady = false;
                    }
                });
                    break;
                case TRADE:
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            inventory.setAll(RESTController.getInventory(thisPlayer.getId()));
                            playerTradeBag.clear();
                            opponentTradeBag.clear();
                        }
                    });
                    break;
                case ADD:
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            playerReady = false;
                            opponentTradeBag.add(message.getItem());
                        }
                    });
                    break;
                case REMOVE:
                    playerReady = false;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for(Item i : opponentTradeBag)
                            {
                                if(i.getId() == message.getItem().getId())
                                {
                                    opponentTradeBag.remove(i);
                                }
                            }
                        }
                    });

                    System.out.println("Opponent removed item");
                    System.out.println(opponentTradeBag);
                    break;

                default:
                    System.out.println("ERROR: GameController update");
                    break;
            }
        }
    }
}
