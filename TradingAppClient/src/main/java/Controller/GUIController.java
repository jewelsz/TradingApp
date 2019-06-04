package Controller;

import Models.Item;
import javafx.scene.control.ListView;

import java.awt.*;
import java.beans.PropertyChangeEvent;

public class GUIController
{
    //public TextField tbUsername, tbPassword;
    public ListView listInventory, listTradeItems, listOpponentItems;

    GameController gameController;

    public GUIController()
    {
        gameController = new GameController();
        gameController.addPropertyChangeListener("addItemOpponentEvent", GUIController::addItemOpponentEvent);
        //gameController.addPropertyChangeListener("removeItemOpponentEvent", GUIController::addItemOpponentEvent);
        gameController.addPropertyChangeListener("fillInventoryEvent", GUIController::fillInventoryEvent);
        //gameController.addPropertyChangeListener("addTradeItemEvent", GUIController::addItemOpponentEvent);
        Item item = new Item(1,"zwaardje");
        Item item1 = new Item(1,"helm");
        Item item2 = new Item(1,"schild");
        gameController.addItemToOpponentTrade(item);
        gameController.addItemToOpponentTrade(item1);
        gameController.addItemToOpponentTrade(item2);

        Item item3 = new Item(1,"ondergoed");
        Item item4 = new Item(1,"sokken");
        Item item5 = new Item(1,"horloge");
        gameController.fillPlayerInventory(item3);
        gameController.fillPlayerInventory(item4);
        gameController.fillPlayerInventory(item5);

        gameController.addItemToOpponentTrade(item4);

        //tbUsername.setText("testtekst");
    }

    private static void addItemOpponentEvent(PropertyChangeEvent evt)
    {
        System.out.println(evt.getNewValue());
    }

    private static void fillInventoryEvent(PropertyChangeEvent evt)
    {
        System.out.println(evt.getNewValue());
    }
}
