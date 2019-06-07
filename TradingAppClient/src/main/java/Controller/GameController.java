package Controller;

import Models.Item;
import Models.Player;
import REST.RESTClientCommunicatorController;
import Websockets.TradingClientEndpoint;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.CommunicatorWebsocketMessage;
import shared.TradeItemMessage;

import java.util.Observable;
import java.util.Observer;

public class GameController implements Observer
{
    public ObservableList<Item> inventory = FXCollections.observableArrayList();
    public ObservableList<Item> playerTradeBag = FXCollections.observableArrayList();
    public ObservableList<Item> opponentTradeBag = FXCollections.observableArrayList();
    public ObservableList<Player> playerList = FXCollections.observableArrayList();

    RESTClientCommunicatorController RESTController;

    TradingClientEndpoint websocketCommunicator = null;

    Player thisPlayer;

    public GameController()
    {
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

    public void removeItemFromOpponentTrade(Item item)
    {
        opponentTradeBag.remove(item);
    }

    public void addItemToOpponentTrade(Item item)
    {
        System.out.println("ITEM ADDED TO OPPONENT BAG");
        opponentTradeBag.add(item);
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

                    break;
                case TRADE:
                    // trade items

                    break;
                case ADD:
                    System.out.println("Opponent added new item");
                    opponentTradeBag.add(message.getItem());
                    break;
                case REMOVE:
                    System.out.println("Opponent removed item");
                    opponentTradeBag.remove(message.getItem());
                    break;

                default:
                    System.out.println("ERROR: GameController update");
                    break;
            }
        }
    }
}
