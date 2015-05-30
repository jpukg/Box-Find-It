package box;

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
public class BoxList {
    private static final String BASE_URL = "https://api.box.com/2.0/folders/";

    public static String list(int folderId, String accessToken) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + folderId);
        request.addHeader("Authorization", "Bearer " + accessToken);
        HttpClient httpclient = HttpClientBuilder.create().build();
        try (CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(request)) {
            StatusLine statusLine = response.getStatusLine();
            System.out.println(statusLine);
            if (statusLine.getStatusCode() != 200) {
                throw new IllegalStateException("Got status code: " + statusLine.getStatusCode() + ", expected 200");
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            } else {
                throw new IllegalStateException("No entity");
            }
        }
    }
}
