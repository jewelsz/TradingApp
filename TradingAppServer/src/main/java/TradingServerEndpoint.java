
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

public class TradingServerEndpoint
{
    private static final Logger log = LoggerFactory.getLogger(TradingServerEndpoint.class);
    private static final List<Session> sessions = new ArrayList<Session>();

    @OnOpen
    public void onConnect(Session session) {
        //log.info("Connected SessionID: {}", session.getId());

        sessions.add(session);
        //log.info("Session added. Session count is {}", sessions.size());
    }

    @OnMessage
    public void onText(String message, Session session) {
        //log.info("Session ID: {} Received: {}", session.getId(), message);
        handleMessageFromClient(message, session);
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        //log.info("Session ID: {} Closed. Reason: {}", session.getId(), reason);
        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable cause, Session session) {
        log.error("Session ID: {} Error: {}", session.getId(), cause.getMessage());
    }

    private void handleMessageFromClient(String jsonMessage, Session session) {
        Gson gson = new Gson();
        //log.info("Session ID: {} Handling message: {}", session.getId(), jsonMessage);

        try {
            Item message = gson.fromJson(jsonMessage, Item.class);
            //log.info("Session ID: {} Message handled: {}", session.getId(), message);
        } catch (JsonSyntaxException ex) {
            log.error("Can't process message: {0}", ex);
        }
    }
}
