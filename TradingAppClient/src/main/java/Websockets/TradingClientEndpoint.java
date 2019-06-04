package Websockets;

import Models.Item;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.CommunicatorWebsocketMessage;
import shared.MessageOperation;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;


@ClientEndpoint
public class TradingClientEndpoint extends Communicator
{

    private static final Logger log = LoggerFactory.getLogger(TradingClientEndpoint.class);
    private static TradingClientEndpoint instance = null;
    private static final String URI = "ws://localhost:8095/trading/"; // TODO Config file
    private Session session;
    private Gson gson;
    private boolean isRunning = false;
    private String message;

    private TradingClientEndpoint() {
        gson = new Gson();
    }

    public static TradingClientEndpoint getInstance() {
        if (instance == null) {
            instance = new TradingClientEndpoint();
            log.info("Websockets.TradingClientEndpoint singleton instantiated");
        }
        return instance;
    }

    public void start() {
        if (!isRunning) {
            startClient();
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            stopClient();
            isRunning = false;
        }
    }

    public void addTradeItem(String property, Item item)
    {
        String jsonContent = gson.toJson(item);
        CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
        message.setOperation(MessageOperation.ADDTRADEITEM);
        message.setProperty(property);
        message.setContent(jsonContent);
        sendMessageToServer(message);
    }

    public void removeTradeItem(String property, Item item)
    {
        String jsonContent = gson.toJson(item);
        CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
        message.setOperation(MessageOperation.REMOVETRADEITEM);
        message.setProperty(property);
        message.setContent(jsonContent);
        sendMessageToServer(message);
    }

    public void acceptTrade(String property, boolean accept)
    {
        String jsonContent = gson.toJson(accept);
        CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
        message.setOperation(MessageOperation.ACCEPTTRADE);
        message.setProperty(property);
        message.setContent(jsonContent);
        sendMessageToServer(message);
    }

    public void register(String property)
    {
        CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
        message.setOperation(MessageOperation.REGISTERPROPERTY);
        message.setProperty(property);
        sendMessageToServer(message);
    }

    public void subscribe(String property)
    {
        CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
        message.setOperation(MessageOperation.SUBSCRIBETOPROPERTY);
        message.setProperty(property);
        sendMessageToServer(message);
    }

    public void unsubscribe(String property)
    {
        CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
        message.setOperation(MessageOperation.UNSUBSCRIBEFROMPROPERTY);
        message.setProperty(property);
        sendMessageToServer(message);
    }

    public void update(CommunicatorMessage message)
    {
        CommunicatorWebsocketMessage wsMessage = new CommunicatorWebsocketMessage();
        wsMessage.setOperation(MessageOperation.UPDATEPROPERTY);
        wsMessage.setProperty(message.getProperty());
        wsMessage.setContent(message.getContent());
        sendMessageToServer(wsMessage);
    }

    @OnOpen
    public void onWebSocketConnect(Session session) {
        log.info("Client open session {}", session.getRequestURI());
        this.session = session;
    }

    @OnMessage
    public void onWebSocketText(String message, Session session) {
        this.message = message;
        log.info("Client message received {}", message);
        processMessage(message);
    }

    @OnError
    public void onWebSocketError(Session session, Throwable cause) {
        log.info("Client connection error {}", cause.toString());
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        log.info("Client close session {} for reason {} ", session.getRequestURI(), reason);
        session = null;
    }

    public void sendMessageToServer(CommunicatorWebsocketMessage message) {
        String jsonMessage = gson.toJson(message);
        log.info("Sending message to server: {}", message);
        session.getAsyncRemote().sendText(jsonMessage);
    }

    //catch (IOException | URISyntaxException | DeploymentException ex)
    private void startClient() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(URI));
            log.info("Connected to server at {}", URI);

        } catch (Exception ex) {
            log.error("Error in startClient: {}", ex.getMessage());
        }
    }

    private void stopClient() {
        try {
            session.close();
            log.info("Session closed");

        } catch (IOException ex) {
            log.error("Error in stopClient {}", ex.getMessage());
        }
    }

    private void processMessage(String jsonMessage) {
        Gson gson = new Gson();
        CommunicatorWebsocketMessage wbMessage = null;
        try
        {
            wbMessage = gson.fromJson(jsonMessage, CommunicatorWebsocketMessage.class);
        } catch (JsonSyntaxException ex)
        {
            System.out.println("[WebSocket ERROR: cannot parse Json message " + jsonMessage);
            return;
        }

        // Operation defined in message
        MessageOperation operation;
        operation = wbMessage.getOperation();

        // Process message based on operation
        String property = wbMessage.getProperty();
        if (null != operation && null != property && !"".equals(property))
        {
            switch (operation)
            {

                case ACCEPTTRADE:
                    // Accept trade

                    break;
                case TRADEITEMS:
                    // trade items

                    break;
                case ADDTRADEITEM:
                    // Add opponents items to trade list
                    break;
                case REMOVETRADEITEM:
                    // Remove item from opponent list
                    break;

                default:
                    System.out.println("[WebSocket ERROR: cannot process Json message " + jsonMessage);
                    break;
            }
        }
    }
}
