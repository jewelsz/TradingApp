package DatabaseCommunicator;

import Models.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabasePlayer implements IDatabasePlayerCommunication
{
    DatabaseConnection con = new DatabaseConnection();

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
}
