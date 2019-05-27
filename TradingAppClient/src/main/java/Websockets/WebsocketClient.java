package Websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.CommunicatorWebsocketMessage;
import shared.MessageOperation;

import java.util.Observable;
import java.util.Observer;

public class WebsocketClient implements Observer
{
    private static final Logger log = LoggerFactory.getLogger(WebsocketClient.class);

    void start() {
        try {
            TradingClientEndpoint tradingClientEndpoint = TradingClientEndpoint.getInstance();
            tradingClientEndpoint.addObserver(this);
            tradingClientEndpoint.start();
            log.info("Websocket client started");

            //tradingClientEndpoint.sendMessageToServer(new Item(1, "Shovel"));
            CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
            message.setOperation(MessageOperation.LOGIN);
            tradingClientEndpoint.sendMessageToServer(message);

            tradingClientEndpoint.stop();
            log.info("Websocket client stopped");
        } catch (Exception ex) {
            log.error("Client couldn't start.");
        }
    }

    public void update(Observable o, Object arg) {
        //log.info("Update called. {}", arg);
    }
}
