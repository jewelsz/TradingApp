package Messages;

import Models.Item;

import java.util.List;

public class ItemResponse
{
    private List<Item> items;

    public ItemResponse(List<Item> items) {
        this.items = items;
    }

    public ItemResponse() {
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
