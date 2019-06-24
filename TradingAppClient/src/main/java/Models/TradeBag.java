package Models;

import Interfaces.ITradeBag;
import Shared_Models.Item;

import java.util.List;

public class TradeBag extends Bag implements ITradeBag
{
    @Override
    public List<Item> trade() {

        List<Item> tradeItems = super.inventory;
        return tradeItems;
    }
}
