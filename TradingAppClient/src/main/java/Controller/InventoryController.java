package Controller;

import Models.Bag;
import Models.OpponentBag;
import Models.PlayerBag;
import Models.TradeBag;
import Shared_Models.Item;

import java.util.List;

public class InventoryController
{
    public Bag playerBag;
    public Bag tradeBag;
    public Bag opponentBag;

    public InventoryController()
    {
        playerBag = new PlayerBag();
        tradeBag = new TradeBag();
        opponentBag = new OpponentBag();
    }

    public void newTradeItems(List<Item> items)
    {
        playerBag.inventory.setAll(items);
        tradeBag.clear();
        opponentBag.clear();
    }

    public List<Item> tradeItems()
    {
        List<Item> items = opponentBag.inventory;
        System.out.println("INVENTORY TRADE ITEMS BAG INHOUD: " + items);
        return items;
    }

    public void tradeClear()
    {
        tradeBag.clear();
        opponentBag.clear();
    }

    public boolean tradeReadyCount()
    {
        if (tradeBag.inventory.size() >= 1) return true;
        return false;
    }

    public Bag getPlayerBag() {
        return playerBag;
    }

    public Bag getTradeBag() {
        return tradeBag;
    }

    public Bag getOpponentBag() {
        return opponentBag;
    }
}
