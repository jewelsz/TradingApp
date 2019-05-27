package REST;

import Models.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RESTConnection
{
    private static final Logger log = LoggerFactory.getLogger(RESTConnection.class);

    public static void main(String[] args) {

        RESTClientCommunicator client = new RESTClientCommunicator();
        final String name = "Jowelle";
        final String password = "password123";

        // Post new greeting
        //Player player = client.postRegistration(new Player(name, password));
        //logPlayer(player);
        int id = client.getPlayer("jowelle", "password123").getId();
        System.out.println(id);

        // Get a greeting
//        greeting = client.getGreeting(key);
//        logGreeting(greeting);
    }

    private static void logPlayer(Player player) {
        if (player != null) {
            log.info("{} {}", player.getName(), player.getPassword());
        } else {
            log.info("No player found.");
        }
    }
}
