package Websockets;

import Models.Item;

public interface ICommunicator
{
    /**
     * Start the connection.
     */
    public void start();

    /**
     * Stop the connection.
     */
    public void stop();

    /**
    * Send new selected item to the list
    */
    public void addTradeItem(String property, Item item);

    /**
     * Remove selected item from the list
     */
    public void removeTradeItem(String property, Item item);

    /**
     * Accept for trading
     */
    public void acceptTrade(String property, boolean accept);

    /**
     * Register a property.
     * @param property
     */
    public void register(String property);

    /**
     * Unregister a property.
     * @param property
     */

    public void subscribe(String property);

    /**
     * Unsubscribe from a property.
     * @param property
     */
    public void unsubscribe(String property);

    /**
     * Update a property by sending a message to all clients
     * that are subscribed to the property of the message.
     * @param message the message to be sent
     */
    public void update(CommunicatorMessage message);
}
