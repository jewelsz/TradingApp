package RESTServer;

import DatabaseCommunicator.DatabaseController;
import Messages.ItemResponse;
import Messages.PlayersList;
import Models.*;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/registration")
public class RESTServerEndpoint
{
    //private static final Logger log = LoggerFactory.getLogger(RESTServerEndpoint.class);
    private static DatabaseController databaseController = DatabaseController.getInstance();
    private final Gson gson;

    public RESTServerEndpoint() {
        gson = new Gson();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRegistration(Player player)
    {
        System.out.println("POST add called for key: " + player.getName());
        databaseController.addRegistration(player);
        return Response.status(200).entity(gson.toJson(player)).build();
    }

    @GET
    @Path("/player/{playername}/{playerpass}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayer(@PathParam("playername") String name, @PathParam("playerpass") String password)
    {
        Player myResponse = databaseController.getPlayer(name, password);
        System.out.println("GEVONDEN: "+ myResponse.getId() + " " + myResponse.getName());
        return Response.status(200).entity(gson.toJson(myResponse)).build();
    }

    @GET
    @Path("/items/{playerid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInventory(@PathParam("playerid") int playerid)
    {
        ItemResponse itemResponse = databaseController.getInventory(playerid);
        return Response.status(200).entity(gson.toJson(itemResponse)).build();
    }

//    @GET
//    @Path("/items/trade/{items}/{playerid}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response tradeItems(@PathParam("items") ItemResponse items, @PathParam("playerid") int playerid)
//    {
//        System.out.println("PUT trade called for playerid: " + playerid);
//        TradeResponse check = new TradeResponse(databaseController.updateItemsFromInventory(items.getItems(), playerid));
//        return Response.status(200).entity(gson.toJson(check)).build();
//    }

    @GET
    @Path("/player/players")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayers()
    {
        System.out.println("Get All Players List");
        PlayersList playersList = databaseController.getAllPlayers();

        return Response.status(200).entity(gson.toJson(playersList)).build();
    }

}
