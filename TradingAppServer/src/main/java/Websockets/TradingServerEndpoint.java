package Websockets;

import Websockets.ServerWSProcessor.MessageProcessor;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint(value = "/trading/")
public class TradingServerEndpoint
{
    // All sessions
    private static final List<Session> sessions = new ArrayList<>();
    MessageProcessor msgProcessor = new MessageProcessor();


    @OnOpen
    public void onConnect(Session session) {
        System.out.println("Connected SessionID: "+ session.getId());

        sessions.add(session);
        System.out.println("Session added. Session count is "+ sessions.size());
    }

    @OnMessage
    public void onText(String message, Session session) {
        System.out.println("Session ID: {}" + session.getId() + " Received: "+  message);
        msgProcessor.handleMessageFromClient(message, session);
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
}
