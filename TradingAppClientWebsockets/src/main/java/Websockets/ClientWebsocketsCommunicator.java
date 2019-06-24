package Websockets;

import Messages.CommunicatorWebsocketMessage;
import Messages.TradeItemMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;

@ClientEndpoint
public class ClientWebsocketsCommunicator extends Observable
{
    private static ClientWebsocketsCommunicator instance = null;
    private static final String URI = "ws://localhost:8095/trading/"; // TODO Config file
    private Session session;
    private Gson gson;
    private boolean isRunning = false;
    private String message;

    private ClientWebsocketsCommunicator() {
        gson = new Gson();
    }

    public static ClientWebsocketsCommunicator getInstance() {
        if (instance == null) {
            instance = new ClientWebsocketsCommunicator();
            System.out.println("ClientEndpoint singleton instantiated");
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
        System.out.println("Client open session "+session.getRequestURI());
        this.session = session;
    }

    @OnMessage
    public void onWebSocketText(String message, Session session) {
        this.message = message;
        System.out.println("Client message received "+ message);
        processMessage(message);
    }

    @OnError
    public void onWebSocketError(Session session, Throwable cause) {
        System.out.println("Client connection error "+ cause.toString());
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        System.out.println("Client close session "+ session.getRequestURI() +   " for reason {} "+ reason);
        session = null;
    }

    public void sendMessageToServer(CommunicatorWebsocketMessage message) {
        String jsonMessage = gson.toJson(message);
        System.out.println("Sending message to server: "+ message);
        session.getAsyncRemote().sendText(jsonMessage);
    }

    private void startClient() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(URI));
            System.out.println("Connected to server at "+ URI);

        } catch (IOException | URISyntaxException | DeploymentException ex) {
            System.out.println("Error in startClient: "+ex.getMessage());
        }
    }

    private void stopClient() {
        try
        {
            session.close();
            System.out.println("Session closed");

        } catch (IOException ex)
        {
            System.out.println("Error in stopClient " + ex.getMessage());
        }
    }

    private void processMessage(String jsonMessage) {
        System.out.println("PROCESSING MSG");
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

        TradeItemMessage itemMessage = null;
        try
        {
            System.out.println("Creating itemMessage msg");
            itemMessage = gson.fromJson(wbMessage.getContent(), TradeItemMessage.class);
        } catch (JsonSyntaxException ex)
        {
            System.out.println("[WebSocket ERROR: cannot parse Json message " + jsonMessage);
            return;
        }

        System.out.println("Incoming message: "+ itemMessage.getOperation());
        this.setChanged();
        this.notifyObservers(itemMessage);
    }
}
