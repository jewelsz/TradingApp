package Controller;

import Models.Item;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIController implements Initializable
{
    public TextField tbUsername, tbPassword;
    @FXML
    public  ListView<Item> listInventory, listTradeItems, listOpponentItems;
    public Label lblName, lblError;

    static GameController gameController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        gameController.inventory.addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("Inventory initialized");
            }
        });

        gameController.playerTradeBag.addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("Trade bag initialized");
            }
        });

        gameController.opponentTradeBag.addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("Opponent bag initialized");
            }
        });

        listInventory.setItems(gameController.inventory);
        listTradeItems.setItems(gameController.playerTradeBag);
        listOpponentItems.setItems(gameController.opponentTradeBag);
    }

    public GUIController()
    {
        gameController = new GameController();
    }

//
//    public void btnGetPlayerInventory()
//    {
//        gameController.fillPlayerInventory();
//    }

    public void btnAddTradeItem()
    {
        Item item = listInventory.getSelectionModel().getSelectedItem();
        gameController.playerTradeBag.add(item);
        gameController.inventory.remove(item);
    }

    public void btnRemoveTradeItem()
    {
        Item item = listTradeItems.getSelectionModel().getSelectedItem();
        gameController.playerTradeBag.remove(item);
        gameController.inventory.add(item);
    }

    public void btnLogin()
    {
        lblError.setVisible(false);
        String username = gameController.login(tbUsername.getText(), tbPassword.getText());
        System.out.println(username);
        if(username != null)
        {
            lblName.setText(username);
        }
        else lblError.setVisible(true);
    }

    public void btnRegister()
    {
        gameController.register(tbUsername.getText(), tbPassword.getText());
    }

}
