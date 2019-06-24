package Interfaces;

import Messages.PlayersList;
import Shared_Models.Player;

public interface IplayerService
{
    Player getPlayer(String player, String password);
    PlayersList getAllPlayers();
    Player postRegistration(Player player);
}
