package REST;

import Models.Item;
import Models.Player;
import Models.ResponseList;
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
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RESTClientCommunicator
{
    private final String playerurl = "http://localhost:8090/registration/"; // TODO Config file
    private final Gson gson = new Gson();

    public RESTClientCommunicator() {
    }

    Player postRegistration(Player player) {
        final String query = playerurl + "add";
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
        final String query = playerurl + "/player/"+ player + "/" + password;
        System.out.println("GET: " + query);

        HttpGet httpGetQuery = new HttpGet(query);

        return executeQuery(httpGetQuery);
    }

    ResponseList getInventory(int playerid) {
        final String query = playerurl + "/items/" + playerid;
        System.out.println("GET: " + query);

        HttpGet httpGetQuery = new HttpGet(query);

        return executeItemQuery(httpGetQuery);
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

    private ResponseList executeItemQuery(HttpRequestBase requestBaseQuery) {
        ResponseList items = null;

        System.out.println("execute query getInventory");
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(requestBaseQuery)) {
            System.out.println("Status: " + response.getStatusLine());

            HttpEntity entity = response.getEntity();
            final String entityString = EntityUtils.toString(entity);
            System.out.println("JSON entity: " + entityString);

            items = gson.fromJson(entityString, ResponseList.class);

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return items;
    }
}
