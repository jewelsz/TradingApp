module TradinAppClient
{
    requires javafx.fxml;
    requires javafx.controls;
    requires TradingAppShared;
    requires TradingAppClientWebsockets;
    requires slf4j.api;
    requires javax.websocket.client.api;
    requires gson;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires java.desktop;
    requires org.eclipse.jetty.websocket.common;
    opens Controller to javafx.fxml, javafx.graphics;
}
