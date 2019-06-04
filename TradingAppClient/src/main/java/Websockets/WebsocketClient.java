package Websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.CommunicatorWebsocketMessage;
import shared.MessageOperation;

import java.util.Observable;
import java.util.Observer;

public class WebsocketClient implements Observer
{

    void start() {
        try {
            TradingClientEndpoint tradingClientEndpoint = TradingClientEndpoint.getInstance();
            tradingClientEndpoint.addObserver(this);
            tradingClientEndpoint.start();
            System.out.println("Websocket client started");

            //tradingClientEndpoint.sendMessageToServer(new Item(1, "Shovel"));
            CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
            //message.setOperation(MessageOperation.LOGIN);
            tradingClientEndpoint.sendMessageToServer(message);

            tradingClientEndpoint.stop();
            System.out.println("Websocket client stopped");
        } catch (Exception ex) {
            System.out.println("Client couldn't start.");
        }
    }

    public void update(Observable o, Object arg) {
        //log.info("Update called. {}", arg);
    }
}
