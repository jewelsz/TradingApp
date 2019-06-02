package DatabaseCommunicator;

import Models.Item;

import java.util.ArrayList;
import java.util.List;

public interface IDatabaseItemCommunication
{
    Item getItem(int id);
    ArrayList<Item> getInventory(int playerid);
    void removeItemsFromInventory(List<Item> items);
    void updateItemsFromInventory(List<Item> items, int playerid);
    void addItemsFromInventory(List<Item> items);
}
