module TradingAppClientWebsockets {
    requires gson;
    requires javax.websocket.client.api;
    requires TradingAppShared;
    exports Websockets;
}