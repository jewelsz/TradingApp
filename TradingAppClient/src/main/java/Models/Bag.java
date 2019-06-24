package Models;

import Shared_Models.Item;
import javafx.application.Platform;
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
        System.out.println("Bag - add: "+item);
        inventory.add(item);
    }

    public void removeItem(Item item) {
        System.out.println("Bag - remove: "+item);
        inventory.remove(item);

    }

    public void clear() {
        inventory.clear();
    }

    public void setInventory(ObservableList<Item> inventory) {
        this.inventory = inventory;
    }

    public void opponentRemoveItem(Item item) {
        System.out.println("Bag - remove: "+item);
        for(Item i : inventory)
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
            if(i.getInventoryId() == item.getInventoryId())
            {
                inventory.remove(i);
            }

                }});
        }
    }
}
