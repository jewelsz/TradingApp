package WebsocketsClient;

import Controller.GameController;
import Models.Item;
import Websockets.ClientWebsocketsCommunicator;
import com.google.gson.Gson;
import shared.CommunicatorWebsocketMessage;
import shared.MessageOperation;
import shared.TradeItemMessage;
import shared.TradeOperation;

import java.util.List;

public class WebsocketsMessageController
{
    Gson gson = new Gson();
    private ClientWebsocketsCommunicator wsCommunicator = null;

    public WebsocketsMessageController(GameController gameController)
    {
        wsCommunicator = wsCommunicator.getInstance();
        wsCommunicator.addObserver(gameController);
        wsCommunicator.start();
    }

    public void addTradeItem(Item item, String property) {
        TradeItemMessage trademsg = new TradeItemMessage(item, TradeOperation.ADD);
        String jsonMessage = gson.toJson(trademsg);
        CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
        message.setOperation(MessageOperation.ADDTRADEITEM);
        message.setProperty(property);
        message.setContent(jsonMessage);
        wsCommunicator.sendMessageToServer(message);
    }

    public void removeTradeItem(Item item, String property) {
        TradeItemMessage trademsg = new TradeItemMessage(item, TradeOperation.REMOVE);
        String jsonMessage = gson.toJson(trademsg);
        CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
        message.setOperation(MessageOperation.REMOVETRADEITEM);
        message.setProperty(property);
        message.setContent(jsonMessage);
        wsCommunicator.sendMessageToServer(message);
    }

    public void register(String property) {
        if(wsCommunicator != null)
        {
            CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
            message.setOperation(MessageOperation.REGISTERPROPERTY);
            message.setProperty(property);
            wsCommunicator.sendMessageToServer(message);
        }
    }

    public void subscribe(String property) {
        CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
        message.setOperation(MessageOperation.SUBSCRIBETOPROPERTY);
        message.setProperty(property);
        wsCommunicator.sendMessageToServer(message);
    }

    public void acceptTrade(String property)
    {
        TradeItemMessage trademsg = new TradeItemMessage(TradeOperation.ACCEPT);
        String jsonMessage = gson.toJson(trademsg);
        CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
        message.setOperation(MessageOperation.ACCEPTTRADE);
        message.setProperty(property);
        message.setContent(jsonMessage);
        wsCommunicator.sendMessageToServer(message);
    }


    public void tradeItems(List<Item> tradeItems, int playerid, String property)
    {
        TradeItemMessage trademsg = new TradeItemMessage(tradeItems, playerid, TradeOperation.TRADE);
        String jsonMessage = gson.toJson(trademsg);
        CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
        message.setOperation(MessageOperation.TRADEITEMS);
        message.setProperty(property);
        message.setContent(jsonMessage);
        wsCommunicator.sendMessageToServer(message);
    }
}
