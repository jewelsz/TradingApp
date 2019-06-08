package REST;

import Models.Item;
import Models.Player;
import Models.PlayersList;
import Models.ItemResponse;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.List;

public class RESTClientCommunicator
{
    private final String url = "http://localhost:8090/registration/"; // TODO Config file
    private final Gson gson = new Gson();

    public RESTClientCommunicator() {
    }

    Player postRegistration(Player player) {
        final String query = url + "add";
        System.out.println("POST: " + query);

        HttpPost httpPostQuery = new HttpPost(query);
        httpPostQuery.addHeader("content-type", "application/json");

        StringEntity params;

        try {
            params = new StringEntity(gson.toJson(player));
            httpPostQuery.setEntity(params);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return executeQuery(httpPostQuery);
    }

    Player getPlayer(String player, String password) {
        final String query = url + "/player/"+ player + "/" + password;
        System.out.println("GET: " + query);

        HttpGet httpGetQuery = new HttpGet(query);

        return executeQuery(httpGetQuery);
    }

    PlayersList getAllPlayers() {
        final String query = url + "/player/players";
        System.out.println("GET: " + query);

        HttpGet httpGetQuery = new HttpGet(query);

        return executePlayersQuery(httpGetQuery);
    }

    ItemResponse getInventory(int playerid) {
        final String query = url + "/items/" + playerid;
        System.out.println("GET: " + query);

        HttpGet httpGetQuery = new HttpGet(query);

        return executeItemQuery(httpGetQuery);
    }

    boolean tradeItems(List<Item>items, int playerid)
    {
        ItemResponse itemsList = new ItemResponse(items);
        final String query = url + "/items/trade/" + itemsList + playerid;
        System.out.println("PUT: " + query);

        HttpGet httpGetQuery = new HttpGet(query);

        return executeTradeQuery(httpGetQuery);
    }


    private Player executeQuery(HttpRequestBase requestBaseQuery) {
        Player player = null;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(requestBaseQuery)) {
            System.out.println("Status: " + response.getStatusLine());

            HttpEntity entity = response.getEntity();
            final String entityString = EntityUtils.toString(entity);
            System.out.println("JSON entity: " + entityString);

            player = gson.fromJson(entityString, Player.class);

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return player;
    }

    private boolean executeTradeQuery(HttpRequestBase requestBaseQuery)
    {
        boolean tradeSucces = false;

        System.out.println("execute query tradeItems");
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(requestBaseQuery)) {
            System.out.println("Status: " + response.getStatusLine());

            HttpEntity entity = response.getEntity();
            final String entityString = EntityUtils.toString(entity);
            System.out.println("JSON entity: " + entityString);

            tradeSucces = gson.fromJson(entityString, boolean.class);

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return tradeSucces;
    }

    private ItemResponse executeItemQuery(HttpRequestBase requestBaseQuery) {
        ItemResponse items = null;

        System.out.println("execute query getInventory");
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(requestBaseQuery)) {
            System.out.println("Status: " + response.getStatusLine());

            HttpEntity entity = response.getEntity();
            final String entityString = EntityUtils.toString(entity);
            System.out.println("JSON entity: " + entityString);

            items = gson.fromJson(entityString, ItemResponse.class);

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return items;
    }

    private PlayersList executePlayersQuery(HttpRequestBase requestBaseQuery) {
        PlayersList players = null;

        System.out.println("execute query getInventory");
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(requestBaseQuery)) {
            System.out.println("Status: " + response.getStatusLine());

            HttpEntity entity = response.getEntity();
            final String entityString = EntityUtils.toString(entity);
            System.out.println("JSON entity: " + entityString);

            players = gson.fromJson(entityString, PlayersList.class);

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return players;
    }
}
