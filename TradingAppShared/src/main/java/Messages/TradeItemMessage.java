package Messages;

import Models.Item;
import Enums.TradeOperation;

import java.util.List;

public class TradeItemMessage
{
    private Item item;
    private List<Item> items;
    private int playerid;
    private TradeOperation operation;

    public TradeItemMessage(Item item, TradeOperation operation) {
        this.item = item;
        this.operation = operation;
    }

    public TradeItemMessage(List<Item> items, int playerid, TradeOperation operation) {
        this.items = items;
        this.playerid = playerid;
        this.operation = operation;
    }

    public TradeItemMessage(TradeOperation operation) {
        this.operation = operation;
    }

    public Item getItem() {
        return item;
    }

    public TradeOperation getOperation() {
        return operation;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getPlayerid() {
        return playerid;
    }
}
