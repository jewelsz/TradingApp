package Controller;

import Models.Item;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class GameController
{
    private List<Item> inventory;
    private List<Item> playerTradeBag;
    private List<Item> opponentTradeBag;

    private final PropertyChangeSupport support;
    //private final PropertyChangeSupport support2;

    public GameController()
    {
        inventory = new ArrayList<>();
        playerTradeBag = new ArrayList<>();
        opponentTradeBag = new ArrayList<>();

        support = new PropertyChangeSupport(this);
        //support2 = new PropertyChangeSupport(this);

        //fillPlayerInventory();
        //Item item = new Item(1,"zwaardje");
        //addItemToOpponentTrade(item);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        support.addPropertyChangeListener(propertyName, listener);
    }

    public void addItemToOpponentTrade(Item item)
    {
        opponentTradeBag.add(item);
        support.firePropertyChange("addItemOpponentEvent", null, opponentTradeBag);
    }

    public void fillPlayerInventory(Item item)
    {
        inventory.add(item);
        support.firePropertyChange("fillInventoryEvent", null, inventory);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public List<Item> getPlayerTradeBag() {
        return playerTradeBag;
    }

    public List<Item> getOpponentTradeBag() {
        return opponentTradeBag;
    }
}
