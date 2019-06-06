package Controller;

import Models.Item;
import Models.Player;
import REST.RESTClientCommunicator;
import REST.RESTClientCommunicatorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class GameController
{
    public ObservableList<Item> inventory = FXCollections.observableArrayList();
    public ObservableList<Item> playerTradeBag = FXCollections.observableArrayList();
    public ObservableList<Item> opponentTradeBag = FXCollections.observableArrayList();

    RESTClientCommunicatorController RESTController;

    Player thisPlayer;

    public GameController()
    {
        RESTController = new RESTClientCommunicatorController();
    }

    public void setRESTController(RESTClientCommunicatorController RESTController) {
        this.RESTController = RESTController;
    }

    public void addItemToOpponentTrade(Item item)
    {
        opponentTradeBag.add(item);
    }

    public void getInventoryFromDatabase(int playerid)
    {
        inventory.setAll(RESTController.getInventory(playerid));
    }

    public String login(String username, String password)
    {
        thisPlayer = RESTController.Login(username, password);
        if(thisPlayer.getId() != 0)
        {
            getInventoryFromDatabase(thisPlayer.getId());
        }
        return thisPlayer.getName();
    }

    public void register(String username, String password)
    {
        RESTController.register(username, password);
    }
}
