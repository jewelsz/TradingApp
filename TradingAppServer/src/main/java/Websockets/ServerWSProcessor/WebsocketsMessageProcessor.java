package Websockets.ServerWSProcessor;

import DatabaseCommunicator.DatabaseController;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import shared.CommunicatorWebsocketMessage;
import shared.MessageOperation;
import shared.TradeItemMessage;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebsocketsMessageProcessor
{
    private DatabaseController dbComm = new DatabaseController();
    // Map each property to list of sessions that are subscribed to that property
    private static final Map<String,List<Session>> propertySessions = new HashMap<>();

    //Gson
    Gson gson = new Gson();

    public void handleMessageFromClient(String jsonMessage, Session session) {

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
                        acceptTrade(property, jsonMessage);
                    }
                    break;
                case TRADEITEMS:
                    if (propertySessions.get(property) != null)
                    {
                        tradeItems(property, wbMessage, jsonMessage);
                    }
                    break;
                case ADDTRADEITEM:
                    // Send the message to all clients that are subscribed to this property
                    if (propertySessions.get(property) != null) {
                        sendItemMessage(property, jsonMessage);
                    }
                    break;
                case REMOVETRADEITEM:
                    // Send the message to all clients that are subscribed to this property
                    if (propertySessions.get(property) != null)
                    {
                        sendItemMessage(property, jsonMessage);
                    }
                    break;
                default:
                    System.out.println("[WebSocket ERROR: cannot process Json message " + jsonMessage);
                    break;
            }
        }
    }

    private void acceptTrade(String property, String jsonMessage)
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

    private void tradeItems(String property, CommunicatorWebsocketMessage wbMessage, String jsonMessage)
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

    private void sendItemMessage(String property, String jsonMessage)
    {
        System.out.println("[WebSocket send ] " + jsonMessage + " to:");
        for (Session sess : propertySessions.get(property)) {
            // Use asynchronous communication
            System.out.println("\t\t >> Client associated with server side session ID: " + sess.getId());
            sess.getAsyncRemote().sendText(jsonMessage);
            System.out.println(sess.getAsyncRemote());
        }
        System.out.println("[WebSocket end sending message to subscribers]");
    }
}
