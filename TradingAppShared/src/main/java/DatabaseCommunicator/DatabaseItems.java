package DatabaseCommunicator;

import Models.Item;
import Models.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseItems implements IDatabaseItemCommunication
{
    DatabaseConnection con = new DatabaseConnection();

    public void insertPlayer(Player player) {

        String sql = "INSERT INTO player(name,password) VALUES(?,?)";

        try (Connection conn = this.con.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getName());
            pstmt.setString(2, player.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("inserted into DB succesfully");
    }

    public Player getPlayer(String name, String password) {
        String sql = "SELECT * FROM `player` WHERE name = ? and password = ?";

        Player dbplayer = new Player();

        try (Connection conn = this.con.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            ResultSet rs    = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("password"));
                dbplayer.setId(rs.getInt("id"));
                dbplayer.setName(rs.getString("name"));
                dbplayer.setPassword(rs.getString("password"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return dbplayer;
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
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name")+  "\t" +
                        rs.getString("id"));
                Item item = new Item(rs.getInt("id"), rs.getString("name"), rs.getInt("id"));
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
    public void updateItemsFromInventory(List<Item> items, int playerid)
    {
        String sql = "UPDATE inventory SET playerid = ? WHERE id = ?";

        for(Item i : items)
        {
            try (Connection conn = this.con.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                pstmt.setInt(1, playerid);
                pstmt.setInt(2, i.getInventoryId());
                pstmt.executeUpdate();
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
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

}
