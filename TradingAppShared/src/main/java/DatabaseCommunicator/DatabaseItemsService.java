package DatabaseCommunicator;

import Shared_Models.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseItemsService implements IDatabaseItemCommunication
{
    DatabaseConnection con;

    public DatabaseItemsService(DatabaseConnection con) {
        this.con = con;
    }

    @Override
    public Item getItem(int id) {
        String sql = "SELECT * FROM `item` WHERE id = ? ";

        Item dbitem = new Item();

        try (Connection conn = this.con.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs    = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name"));
                dbitem.setId(rs.getInt("id"));
                dbitem.setItemName(rs.getString("name"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return dbitem;
    }

    @Override
    public List<Item> getInventory(int playerid)
    {
        String sql = "SELECT item.id, item.name, inventory.id\n" +
                "FROM item, inventory\n" +
                "WHERE item.id = inventory.itemid \n" +
                "AND inventory.playerid = ?";

        List<Item> dbitems = new ArrayList<Item>();

        try (Connection conn = this.con.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, playerid);
            ResultSet rs    = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                Item item = new Item(rs.getInt("item.id"), rs.getString("item.name"), rs.getInt("inventory.id"));
                dbitems.add(item);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return dbitems;
    }

    @Override
    public void removeItemsFromInventory(List<Item> items) 
    {
        String sql = "DELETE FROM inventory WHERE id = ?";

        for(Item i : items)
        {
            try (Connection conn = this.con.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                pstmt.setInt(1, i.getInventoryId());
                pstmt.executeUpdate();
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void updateItemsFromInventory(Item item, int playerid)
    {
        String sql = "UPDATE inventory SET playerid = ? WHERE id = ?";

        try (Connection conn = this.con.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, playerid);
            pstmt.setInt(2, item.getInventoryId());

            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getPlayerIDFromInventory(int inventoryid)
    {
        String sql = "SELECT playerid FROM inventory WHERE id = ?";

        int playerid = 0;

        try (Connection conn = this.con.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, inventoryid);
            ResultSet rs    = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("playerid"));
                playerid = (rs.getInt("playerid"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return playerid;
    }

    @Override
    public void addItemToPlayer(int itemid, int playerid)
    {
        String sql = "INSERT INTO inventory(playerid, itemid) VALUES(?,?)";

        try (Connection conn = this.con.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, playerid);
            pstmt.setInt(1, itemid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
