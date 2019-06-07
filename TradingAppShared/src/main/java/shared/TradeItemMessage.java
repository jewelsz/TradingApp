package shared;

import Models.Item;

public class TradeItemMessage
{
    private Item item;
    private TradeOperation operation;

    public TradeItemMessage(Item item, TradeOperation operation) {
        this.item = item;
        this.operation = operation;
    }

    public Item getItem() {
        return item;
    }

    public TradeOperation getOperation() {
        return operation;
    }
}
