package Interfaces;

import Messages.ItemResponse;
import Shared_Models.Item;

import java.util.List;

public interface IItemService
{
    ItemResponse getInventory(int playerid);
    //void tradeItems(List<Item> items, int playerid);
}
