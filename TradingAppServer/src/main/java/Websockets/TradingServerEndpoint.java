package Websockets;

import DatabaseCommunicator.DatabaseCommunicator;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import shared.CommunicatorWebsocketMessage;
import shared.MessageOperation;
import shared.TradeItemMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerEndpoint(value = "/trading/")
public class TradingServerEndpoint
{
    private DatabaseCommunicator dbComm = new DatabaseCommunicator();
    // All sessions
    private static final List<Session> sessions = new ArrayList<>();

    // Map each property to list of sessions that are subscribed to that property
    private static final Map<String,List<Session>> propertySessions = new HashMap<>();

    @OnOpen
    public void onConnect(Session session) {
        System.out.println("Connected SessionID: "+ session.getId());

        sessions.add(session);
        System.out.println("Session added. Session count is "+ sessions.size());
    }

    @OnMessage
    public void onText(String message, Session session) {
        System.out.println("Session ID: {}" + session.getId() + " Received: "+  message);
        handleMessageFromClient(message, session);
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        System.out.println("Session ID: " + session.getId() + " Closed. Reason: " + reason);
        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable cause, Session session) {
        System.out.println("Session ID: "+ session.getId() + " Error: "+ cause.getMessage());
    }

    private void handleMessageFromClient(String jsonMessage, Session session) {
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
                case REGISTERPROPERTY:
                    // Register property if not registered yet
                    if (propertySessions.get(property) == null)
                    {
                        propertySessions.put(property, new ArrayList<Session>());
                        System.out.println("Register "+ property + "Session: "+ propertySessions);
                    }
                    break;
                case UNREGISTERPROPERTY:
                    // Do nothing as property may also have been registered by
                    // another client
                    break;
                case SUBSCRIBETOPROPERTY:
                    // Subsribe to property if the property has been registered
                    if (propertySessions.get(property) != null)
                    {
                        propertySessions.get(property).add(session);
                    }
                    break;
                case UNSUBSCRIBEFROMPROPERTY:
                    // Unsubsribe from property if the property has been registered
                    if (propertySessions.get(property) != null)
                    {
                        propertySessions.get(property).remove(session);
                    }
                    break;
                case ACCEPTTRADE:
                    // Accept trade
                    if (propertySessions.get(property) != null)
                    {
                        System.out.println("[WebSocket send ] " + jsonMessage + " to:");
                        for (Session sess : propertySessions.get(property))
                        {
                            // Use asynchronous communication
                            System.out.println("\t\t >> Client associated with server side session ID: " + sess.getId());
                            sess.getAsyncRemote().sendText(jsonMessage);
                        }
                        System.out.println("[WebSocket end sending message to subscribers]");
                    }
                    break;
                case TRADEITEMS:
                    if (propertySessions.get(property) != null)
                    {
                        System.out.println("Reading trade message");
                        TradeItemMessage trademsg = null;
                        try
                        {
                            trademsg = gson.fromJson(wbMessage.getContent(), TradeItemMessage.class);
                        }
                        catch (JsonSyntaxException ex)
                        {
                            System.out.println("[WebSocket ERROR: cannot parse Json message " + jsonMessage);
                            return;
                        }

                        System.out.println("Send items and playerid to database");
                        dbComm.updateItemsFromInventory(trademsg.getItems(), trademsg.getPlayerid());

                        System.out.println("[WebSocket send ] " + jsonMessage + " to:");
                        for (Session sess : propertySessions.get(property))
                        {
                            // Use asynchronous communication
                            System.out.println("\t\t >> Client associated with server side session ID: " + sess.getId());
                            sess.getAsyncRemote().sendText(jsonMessage);
                        }
                        System.out.println("[WebSocket end sending message to subscribers] " );
                    }
                    break;
                case ADDTRADEITEM:
                    // Send the message to all clients that are subscribed to this property
                    if (propertySessions.get(property) != null) {
                        System.out.println("[WebSocket send ] " + jsonMessage + " to:");
                        for (Session sess : propertySessions.get(property)) {
                            // Use asynchronous communication
                            System.out.println("\t\t >> Client associated with server side session ID: " + sess.getId());
                            sess.getAsyncRemote().sendText(jsonMessage);
                            System.out.println(sess.getAsyncRemote());
                        }
                        System.out.println("[WebSocket end sending message to subscribers]");
                    }
                    break;
                case REMOVETRADEITEM:
                    // Send the message to all clients that are subscribed to this property
                    if (propertySessions.get(property) != null)
                    {
                        System.out.println("[WebSocket send ] " + jsonMessage + " to:");
                        for (Session sess : propertySessions.get(property))
                        {
                            System.out.println("[WebSocket send ] " + jsonMessage + " to:"+ sess.getAsyncRemote());
                            // Use asynchronous communication
                            System.out.println("\t\t >> Client associated with server side session ID: " + sess.getId());
                            sess.getAsyncRemote().sendText(jsonMessage);
                        }
                        System.out.println("[WebSocket end sending message to subscribers]");
                    }
                    break;
                case UPDATEPROPERTY:
                    // Send the message to all clients that are subscribed to this property
                    if (propertySessions.get(property) != null)
                    {
                        System.out.println("[WebSocket send ] " + jsonMessage + " to:");
                        for (Session sess : propertySessions.get(property))
                        {
                            // Use asynchronous communication
                            System.out.println("\t\t >> Client associated with server side session ID: " + sess.getId());
                            sess.getAsyncRemote().sendText(jsonMessage);
                        }
                        System.out.println("[WebSocket end sending message to subscribers]");
                    }
                    break;
                default:
                    System.out.println("[WebSocket ERROR: cannot process Json message " + jsonMessage);
                    break;
            }
        }
    }
}
