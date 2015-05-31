package box;

import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author Daniyar Itegulov
 */
public class BoxUserInfo {
    private static final JsonParser parser = new JsonParser();
    private static final String URL = "https://api.box.com/2.0/users/me";

    public static long getUserId(BoxAccount boxAccount) throws IOException {
        HttpGet request = new HttpGet(URL);
        System.out.println("Get user ID by " + boxAccount);
        request.addHeader("Authorization", "Bearer " + boxAccount.accessToken);
        HttpClient httpclient = HttpClientBuilder.create().build();
        try (CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(request)) {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            System.out.println("getUserId: " + statusLine);
            if (statusLine.getStatusCode() == 200) {
                if (entity != null) {
                    String string = EntityUtils.toString(entity);
                    return parser.parse(string).getAsJsonObject().get("id").getAsLong();
                } else {
                    throw new IllegalStateException("No entity");
                }
            } else {
                System.out.println("Tried to get content of " + boxAccount + ", but something got wrong");
                System.out.println("response: " + EntityUtils.toString(entity));
                throw new IllegalStateException("Got status code: " + statusLine.getStatusCode() + ", expected 200");
            }
        }
    }
}
