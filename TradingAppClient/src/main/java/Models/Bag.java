package Models;

import Shared_Models.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Bag
{
    public ObservableList<Item> inventory;

    public Bag()
    {
        inventory = FXCollections.observableArrayList();
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public void clear() {
        inventory.clear();
    }

    public void setInventory(ObservableList<Item> inventory) {
        this.inventory = inventory;
    }
}
