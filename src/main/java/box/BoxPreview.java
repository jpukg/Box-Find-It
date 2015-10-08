package box;

import org.apache.commons.codec.binary.Base64;
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
public class BoxPreview {
    private static final String THUMBNAIL_URL = "https://api.box.com/2.0/files/%d/thumbnail.png?min_height=256&min_width=256";
    private static final Base64 encoder = new Base64();
    public static String getThumbnail(long fileId, BoxAccount boxAccount) throws IOException {
        HttpGet request = new HttpGet(String.format(THUMBNAIL_URL, fileId));
        System.out.println("Get thumbnail by " + boxAccount);
        request.addHeader("Authorization", "Bearer " + boxAccount.accessToken);
        HttpClient httpclient = HttpClientBuilder.create().build();
        try (CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(request)) {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            System.out.println("getThumbnail: " + statusLine);
            if (statusLine.getStatusCode() == 200) {
                if (entity != null) {
                    byte[] data = EntityUtils.toByteArray(entity);
                    return "data:image/png;base64," + encoder.encodeToString(data);
                } else {
                    throw new IllegalStateException("No entity");
                }
            } else if (statusLine.getStatusCode() == 202) {
                Header location = response.getFirstHeader("Location");
                System.out.println("Get thumbnail: " + location);
                return location.getValue();
            } else {
                    System.out.println("Tried to get content of " + fileId + ", but something got wrong");
                    System.out.println("getThumbnail: " + EntityUtils.toString(entity));
                    throw new IllegalStateException("Got status code: " + statusLine.getStatusCode() + ", expected 200");
            }
        }
    }
}
