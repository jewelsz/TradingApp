package Interfaces;

import Shared_Models.Item;

import java.util.List;

public interface ITradeCommunicationService
{
    void acceptTrade(String property);
    void addTradeItem(Item item, String property);
    void removeTradeItem(Item item, String property);
    void tradeItems(List<Item> items, int playerid, String property);
}
