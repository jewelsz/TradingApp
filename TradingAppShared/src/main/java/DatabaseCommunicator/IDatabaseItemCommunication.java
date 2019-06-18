package DatabaseCommunicator;

import Shared_Models.Item;

import java.util.List;

public interface IDatabaseItemCommunication
{
    Item getItem(int id);
    List<Item> getInventory(int playerid);
    void removeItemsFromInventory(List<Item> items);
    void updateItemsFromInventory(Item item, int playerid);
    int getPlayerIDFromInventory(int inventoryid);
}
