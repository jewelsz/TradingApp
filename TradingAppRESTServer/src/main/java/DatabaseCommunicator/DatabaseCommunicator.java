package DatabaseCommunicator;

import Models.Player;

public class DatabaseCommunicator
{
    IDatabaseCommunication communication = new DatabasePlayer();
    private static DatabaseCommunicator instance;

    public DatabaseCommunicator() {
    }

    //TODO put registration in database.

    public static DatabaseCommunicator getInstance()
    {
        if (instance == null)
        {
            instance = new DatabaseCommunicator(); // zet hier de dingen die aangemaakt moeten worden in de constructor
        }
        return instance;
    }

    public void addRegistration(Player player)
    {
        //TODO add to database.
        System.out.println("Put in db");
        communication.insertPlayer(player);
    }

    public Player getPlayer(String name, String password)
    {
        Player p = communication.getPlayer(name, password);

        return p;
    }
}
