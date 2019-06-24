package Websockets.ServerWSProcessor;

import DatabaseCommunicator.DatabaseController;
import Enums.TradeOperation;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import Messages.CommunicatorWebsocketMessage;
import Enums.MessageOperation;
import Messages.TradeItemMessage;

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

    public WebsocketsMessageProcessor()
    {
        dbComm = new DatabaseController();
    }

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
                        System.out.println("Register "+ property + "  Session: "+ session.getId());
                        System.out.println("SERVER WS AANTAL SESSIONS: "+ propertySessions.size());
                    }
                    else
                    {
                        System.out.println("SERVER WS PROPERTY IS NULL - CAN'T REGISTER");
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
                        System.out.println("Websockets - Name: "+ property + " session: "+ session.getId());
                        dbComm.setSessionTotal(propertySessions.get(property).size());
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
                        System.out.println("SERVER: ACCEPT TRADE");
                        acceptTrade(property, jsonMessage);
                    }
                    break;
                case TRADEITEMS:
                    if (propertySessions.get(property) != null)
                    {
                        System.out.println("SERVER: TRADE ITEMS");
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
                case GETSESSION:

                    if(propertySessions.get(property) != null)
                    {
                        returnSession(property, session);
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
            sess.getAsyncRemote().sendText(jsonMessage);
            System.out.println("\t\t >> Client associated with server side session ID: " + sess.getId());
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
        }
        System.out.println("[WebSocket end sending message to subscribers]");
    }

    private void returnSession(String property, Session session)
    {
        //List<Session> sessions = propertySessions.get(property);

        //Aantal sessions van de speler met deze property naam
        int totalSessions = propertySessions.get(property).size();
        System.out.println("SERVER WEBSOCKETS SIZE: "+ totalSessions);

        TradeItemMessage sessionMessage = new TradeItemMessage(totalSessions, TradeOperation.SESSIONS);
        String jsonSessionMessage = gson.toJson(sessionMessage);
        CommunicatorWebsocketMessage message = new CommunicatorWebsocketMessage();
        message.setOperation(MessageOperation.GETSESSION);
        message.setProperty(property);
        message.setContent(jsonSessionMessage);
        String jsonMessage = gson.toJson(message);
        System.out.println("SENDING MESSAGE BACK");
        for(Session sess : propertySessions.get(session))
        {
            sess.getAsyncRemote().sendText(jsonMessage);
        }
        session.getAsyncRemote().sendText(jsonMessage);
    }
}
