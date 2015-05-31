package box;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author Daniyar Itegulov
 */
public class BoxCreateSharedLink {
    private static final JsonParser parser = new JsonParser();
    private static final String URL = "https://api.box.com/2.0/files/";

    public static String createPrivateLink(long fileId, BoxAccount boxAccount) throws IOException {
        HttpPut request = new HttpPut(URL + fileId);
        System.out.println("Create private link by " + boxAccount);
        request.addHeader("Authorization", "Bearer " + boxAccount.accessToken);
        JsonObject jsonObject = new JsonObject();
        JsonObject sharedLink = new JsonObject();
        sharedLink.addProperty("access", "collaborators");
        jsonObject.add("shared_link", sharedLink);
        System.out.println(jsonObject.toString());
        request.setEntity(new StringEntity(jsonObject.toString()));
        HttpClient httpclient = HttpClientBuilder.create().build();
        try (CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(request)) {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            System.out.println("createPrivateLink: " + statusLine);
            if (statusLine.getStatusCode() != 200) {
                System.out.println("Tried to get content of " + fileId + ", but something got wrong");
                System.out.println("createPrivateLink: " + EntityUtils.toString(entity));
                throw new IllegalStateException("Got status code: " + statusLine.getStatusCode() + ", expected 200");
            }
            if (entity != null) {
                String ret = EntityUtils.toString(entity);
                System.out.println(ret);
                return parser.parse(ret).getAsJsonObject().get("shared_link")
                        .getAsJsonObject().get("url").getAsString();
            } else {
                throw new IllegalStateException("No entity");
            }
        }
    }
}
