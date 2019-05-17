public class Item
{
    private int id;
    private String itemName;

    public Item() {
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

    @Override
    public String toString() {
        return "Greeting{" +
                "id='" + id + '\'' +
                ", Item=" + itemName +
                '}';
    }
}
