package Messages;

import Models.Item;

import java.util.List;

public class TradeMessage
{
    private List<Item> items;
    private int playerid;

    public TradeMessage(List<Item> items, int playerid) {
        this.items = items;
        this.playerid = playerid;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getPlayerid() {
        return playerid;
    }
}
