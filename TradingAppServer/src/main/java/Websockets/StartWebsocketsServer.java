package Websockets;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import javax.websocket.server.ServerContainer;

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

        ServletContextHandler webSocketContext = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        webSocketContext.setContextPath("/");
        webSocketServer.setHandler(webSocketContext);

        try {
            ServerContainer serverContainer = WebSocketServerContainerInitializer
                    .configureContext(webSocketContext);
            System.out.println("Initialize javax.websocket layer");

            serverContainer.addEndpoint(WebsocketsServerEndpoint.class);
            System.out.println("Endpoint added");

            webSocketServer.start();
            System.out.println("Server started");

            webSocketServer.join();
            System.out.println("Server joined");

        } catch (Throwable t) {
            System.out.println("Error startWebSocketServer "+ t);
        }
    }
}
