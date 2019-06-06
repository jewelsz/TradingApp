package RESTServer;

import DatabaseCommunicator.DatabaseCommunicator;
import Models.Item;
import Models.Player;
import Models.ResponseList;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/registration")
public class RESTServerEndpoint
{
    //private static final Logger log = LoggerFactory.getLogger(RESTServerEndpoint.class);
    private static DatabaseCommunicator databaseCommunicator = DatabaseCommunicator.getInstance();
    private final Gson gson;

    public RESTServerEndpoint() {
        gson = new Gson();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRegistration(Player player) {
        System.out.println("POST add called for key: " + player.getName());

        databaseCommunicator.addRegistration(player);

        return Response.status(200).entity(gson.toJson(player)).build();
    }

    @GET
    @Path("/player/{playername}/{playerpass}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayer(@PathParam("playername") String name, @PathParam("playerpass") String password) {
        System.out.println("GET id for key: "+ name + " and " + password);

        Player myResponse = databaseCommunicator.getPlayer(name, password);

        System.out.println("GEVONDEN: "+ myResponse.getId() + " " + myResponse.getName());
        return Response.status(200).entity(gson.toJson(myResponse)).build();
    }

    @GET
    @Path("/items/{playerid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInventory(@PathParam("playerid") int playerid) {
//        System.out.println("GET inventory for playerid: "+ playerid);
        System.out.println("connectie");
        ResponseList responseList = databaseCommunicator.getInventory(playerid);

        return Response.status(200).entity(gson.toJson(responseList)).build();
    }

}
