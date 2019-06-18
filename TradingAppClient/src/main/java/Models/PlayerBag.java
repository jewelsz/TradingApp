package Models;


import Interfaces.IPlayerBag;
import Shared_Models.Item;

import java.util.List;

public class PlayerBag extends Bag implements IPlayerBag
{
    @Override
    public void fillInventory(List<Item> items) {
        super.inventory.setAll(items);
    }
}
