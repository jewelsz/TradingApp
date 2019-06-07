package Models;

import java.util.List;

public class PlayersList
{
    List<Player> player;

    public PlayersList(List<Player> player) {
        this.player = player;
    }

    public List<Player> getPlayer() {
        return player;
    }

    public void setPlayer(List<Player> player) {
        this.player = player;
    }
}
