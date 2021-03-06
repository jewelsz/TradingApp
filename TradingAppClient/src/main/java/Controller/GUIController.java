package Controller;

import Shared_Models.Item;
import Shared_Models.Player;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class GUIController implements Initializable
{
    public TextField tbUsername, tbPassword;
    @FXML
    public  ListView<Item> listInventory, listTradeItems, listOpponentItems;
    public ListView<Player> listPlayers;
    public Label lblName, lblError, lblTradeReady, lblOpponentName;

    public GameController gameController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        gameController.inventoryController.playerBag.inventory.addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("Inventory initialized");
            }
        });

        gameController.inventoryController.tradeBag.inventory.addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("Trade bag initialized");
            }
        });

        gameController.inventoryController.opponentBag.inventory.addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("Opponent bag initialized");
            }
        });

        //playerlist
        gameController.playerList.addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("Players list initialized");
            }
        });

        listInventory.setItems(gameController.inventoryController.playerBag.inventory);
        listTradeItems.setItems(gameController.inventoryController.tradeBag.inventory);
        listOpponentItems.setItems(gameController.inventoryController.opponentBag.inventory);
        listPlayers.setItems(gameController.playerList);
    }

    public GUIController()
    {
        gameController = new GameController();
    }


    public void btnAddTradeItem()
    {
        Item item = listInventory.getSelectionModel().getSelectedItem();
        gameController.addTradeItem(item);
    }

    public void btnRemoveTradeItem()
    {
        Item item = listTradeItems.getSelectionModel().getSelectedItem();
        gameController.removeTradeItem(item);
    }

    public void btnAcceptTrade()
    {
        gameController.acceptTrade();
    }

    public void btnLogin()
    {
        lblError.setVisible(false);
        String usernametxt = tbUsername.getText();
        String passwordtxt = tbPassword.getText();

        if(tbUsername.getText().isEmpty() || tbPassword.getText().isEmpty())
        {
            lblError.setVisible(true);
        }
        else
        {
            String username = gameController.login(tbUsername.getText(), tbPassword.getText());
            if (username != null && tbPassword.getText() != "")
            {
                lblName.setText(username);
                tbUsername.clear();
                tbPassword.clear();
            }
        }
    }

    public GameController getGameController() {
        return gameController;
    }

    public void btnSelectTrader()
    {
        System.out.println("Subscribe button clicked");
        gameController.subscribe(listPlayers.getSelectionModel().getSelectedItem().getName());
        lblOpponentName.setText(listPlayers.getSelectionModel().getSelectedItem().getName());
    }

    public void btnRegister()
    {
        gameController.register(tbUsername.getText(), tbPassword.getText());
    }

}
