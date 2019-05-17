import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;
import javax.websocket.*;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

//import nl.fhict.s3.websocketshared.Greeting;

@ClientEndpoint
public class TradingClientEndpoint extends Observable
{

    private static final Logger log = LoggerFactory.getLogger(TradingClientEndpoint.class);
    private static TradingClientEndpoint instance = null;
    private static final String URI = "ws://localhost:8095/greeter/"; // TODO Config file
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
            //log.info("GreeterClientEndpoint singleton instantiated");
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

    @OnOpen
    public void onWebSocketConnect(Session session) {
        //log.info("Client open session {}", session.getRequestURI());
        this.session = session;
    }

    @OnMessage
    public void onWebSocketText(String message, Session session) {
        this.message = message;
        //log.info("Client message received {}", message);
        processMessage(message);
    }

    @OnError
    public void onWebSocketError(Session session, Throwable cause) {
        //log.info("Client connection error {}", cause.toString());
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        //log.info("Client close session {} for reason {} ", session.getRequestURI(), reason);
        session = null;
    }

    public void sendMessageToServer(Item message) {
        String jsonMessage = gson.toJson(message);
        //log.info("Sending message to server: {}", message);
        session.getAsyncRemote().sendText(jsonMessage);
    }

    private void startClient() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(URI));
            //log.info("Connected to server at {}", URI);

        } catch (Exception ex) {
            log.error("Error in startClient: {}", ex.getMessage());
        }
    }

    private void stopClient() {
        try {
            session.close();
            //log.info("Session closed");

        } catch (IOException ex) {
            log.error("Error in stopClient {}", ex.getMessage());
        }
    }

    private void processMessage(String jsonMessage) {
        Item greeting;
        //log.info("Processing message: {}", jsonMessage);

        try {
            greeting = gson.fromJson(jsonMessage, Item.class);
            //log.info("Message processed: {}", greeting);
        } catch (JsonSyntaxException ex) {
            log.error("Can't process message: {}", ex.getMessage());
            return;
        }

        String content = greeting.getItemName();
        if (content == null || "".equals(content)) {
            log.error("Message is empty");
            return;
        }

        Item commMessage = new Item();
        commMessage.setItemName(content);

        this.setChanged();
        this.notifyObservers(commMessage);
    }
}
