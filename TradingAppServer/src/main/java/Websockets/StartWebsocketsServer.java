package Websockets;

import javax.websocket.server.ServerContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartWebsocketsServer
{
    private static final Logger log = LoggerFactory.getLogger(StartWebsocketsServer.class);
    private static final int PORT = 8095;

    public static void main(String[] args) {
        startWebSocketServer();
    }

    private static void startWebSocketServer() {
        Server webSocketServer = new Server();
        ServerConnector connector = new ServerConnector(webSocketServer);
        connector.setPort(PORT);
        webSocketServer.addConnector(connector);
        log.info("Connector added");

        ServletContextHandler webSocketContext = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        webSocketContext.setContextPath("/");
        webSocketServer.setHandler(webSocketContext);
        log.info("Context handler set");

        try {
            ServerContainer serverContainer = WebSocketServerContainerInitializer
                    .configureContext(webSocketContext);
            log.info("Initialize javax.websocket layer");

            serverContainer.addEndpoint(TradingServerEndpoint.class);
            log.info("Endpoint added");

            webSocketServer.start();
            log.info("Server started");

            webSocketServer.join();
            log.info("Server joined");

        } catch (Throwable t) {
            log.error("Error startWebSocketServer {0}", t);
        }
    }
}
