package REST;

import Models.Player;
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

public class RESTClientCommunicator
{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RESTClientCommunicator.class);
    private final String url = "http://localhost:8090/registration/"; // TODO Config file
    private final Gson gson = new Gson();

    RESTClientCommunicator() {
    }

    Player postRegistration(Player player) {
        final String query = url + "add";
        log.info("POST: " + query);

        HttpPost httpPostQuery = new HttpPost(query);
        httpPostQuery.addHeader("content-type", "application/json");

        StringEntity params;

        try {
            params = new StringEntity(gson.toJson(player));
            httpPostQuery.setEntity(params);
        } catch (Exception e) {
            log.error(e.toString());
        }

        return executeQuery(httpPostQuery);
    }

    Player getPlayer(String player, String password) {
        final String query = url + "/player/"+ player + "/" + password;
        log.info("GET: " + query);

        HttpGet httpGetQuery = new HttpGet(query);

        return executeQuery(httpGetQuery);
    }

    private Player executeQuery(HttpRequestBase requestBaseQuery) {
        Player greeting = null;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(requestBaseQuery)) {
            log.info("Status: " + response.getStatusLine());

            HttpEntity entity = response.getEntity();
            final String entityString = EntityUtils.toString(entity);
            log.info("JSON entity: " + entityString);

            greeting = gson.fromJson(entityString, Player.class);

        } catch (Exception e) {
            log.error(e.toString());
        }

        return greeting;
    }
}
