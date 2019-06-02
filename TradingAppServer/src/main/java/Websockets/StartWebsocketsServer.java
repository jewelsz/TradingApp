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
    private static final int PORT = 8095;

    public static void main(String[] args) {
        startWebSocketServer();
    }

    private static void startWebSocketServer() {
        Server webSocketServer = new Server();
        ServerConnector connector = new ServerConnector(webSocketServer);
        connector.setPort(PORT);
        webSocketServer.addConnector(connector);
        System.out.println("Websocket Connector added");

        ServletContextHandler webSocketContext = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        webSocketContext.setContextPath("/");
        webSocketServer.setHandler(webSocketContext);
        System.out.println("Context handler set");

        try {
            ServerContainer serverContainer = WebSocketServerContainerInitializer
                    .configureContext(webSocketContext);
            System.out.println("Initialize javax.websocket layer");

            serverContainer.addEndpoint(TradingServerEndpoint.class);
            System.out.println("Endpoint added");

            webSocketServer.start();
            System.out.println("Server started");

            webSocketServer.join();
            System.out.println("Server joined");

        } catch (Throwable t) {
            System.out.println("Error startWebSocketServer " + t);
        }
    }
}
