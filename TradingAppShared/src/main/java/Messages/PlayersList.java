package Messages;

import Shared_Models.Player;

import java.util.List;

public class PlayersList
{
    List<Player> players;

    public PlayersList(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
