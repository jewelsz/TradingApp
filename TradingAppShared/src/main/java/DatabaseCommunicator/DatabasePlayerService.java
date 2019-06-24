package DatabaseCommunicator;

import Shared_Models.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabasePlayerService implements IDatabasePlayerCommunication
{
    DatabaseConnection con;

    public DatabasePlayerService(DatabaseConnection con) {
        this.con = con;
    }

    @Override
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

    @Override
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
    public List<Player> getAllPlayers()
    {
        String sql = "SELECT * FROM player";

        List<Player> dbplayers = new ArrayList<>();

        try (Connection conn = this.con.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs    = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                Player player = new Player(rs.getInt("id"), rs.getString("name"), rs.getString("password"));
                dbplayers.add(player);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return dbplayers;
    }

    @Override
    public void removePlayer(String name)
    {
        String sql = "DELETE FROM player WHERE name = ?";

        try (Connection conn = this.con.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Deleted DB succesfully");
    }
}
