module TradinAppClient
{
    requires javafx.fxml;
    requires javafx.controls;
    requires TradingAppShared;
    requires slf4j.api;
    requires javax.websocket.client.api;
    requires gson;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires java.desktop;
    opens Controller to javafx.graphics, javafx.fxml;
}
