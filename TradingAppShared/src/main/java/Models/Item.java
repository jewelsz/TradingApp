package Models;

public class Item
{
    private int id;
    private String itemName;
    private int inventoryId;

    public Item() {
    }

    public Item(int id, String itemName, int inventoryId) {
        this.id = id;
        this.itemName = itemName;
        this.inventoryId = inventoryId;
    }

    public Item(int id, String itemName) {
        this.id = id;
        this.itemName = itemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    @Override
    public String toString() {
        return itemName;
    }
}
