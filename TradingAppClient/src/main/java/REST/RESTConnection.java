package REST;

import Models.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RESTConnection
{
    public static void main(String[] args) {

        RESTClientCommunicator client = new RESTClientCommunicator();
        final String name = "test";
        final String password = "password123";

        // Post new greeting
       // Player player = client.postRegistration(new Player(name, password));
      //  logPlayer(player);
      //  int id =
        //client.getPlayer("test", "password123").getId();
       // System.out.println(id);
        //client.getInventory();
          // System.out.println(client.getInventory(1));
        // Get a greeting
//        greeting = client.getGreeting(key);
//        logGreeting(greeting);
    }

    private static void logPlayer(Player player) {
        if (player != null) {
            System.out.println( player.getName() + " " + player.getPassword());
        } else {
            System.out.println("No player found.");
        }
    }
}
