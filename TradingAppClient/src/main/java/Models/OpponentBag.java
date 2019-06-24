package Models;

import Interfaces.IOpponentBag;
import Shared_Models.Item;
import javafx.application.Platform;

public class OpponentBag extends Bag implements IOpponentBag
{
    @Override
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
