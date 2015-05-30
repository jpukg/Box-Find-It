package idolondemand.textextraction;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import idolondemand.Constants;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Daniyar Itegulov
 */
public class TextExtractor {
    private static final JsonParser parser = new JsonParser();
    private static final String BASE_URL = "https://api.idolondemand.com/1/api/sync/extracttext/v1";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko)" +
            " Chrome/41.0.2228.0 Safari/537.36";

    public static String fetchByFile(File file) throws IOException {
        HttpPost request = new HttpPost(BASE_URL);
        request.addHeader("User-Agent", USER_AGENT);
        FileBody fileBody = new FileBody(file);
        StringBody apiKeyBody = new StringBody(Constants.API_KEY, ContentType.TEXT_PLAIN);
        StringBody falseBody = new StringBody("false", ContentType.TEXT_PLAIN);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create()
                .addPart("file", fileBody)
                .addPart("apikey", apiKeyBody)
                .addPart("extract_metadata", falseBody);
        HttpEntity entity = multipartEntityBuilder.build();
        request.setEntity(entity);
        HttpClient httpclient = HttpClientBuilder.create().build();
        try (CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(request)) {
            StatusLine statusLine = response.getStatusLine();
            System.out.println(statusLine);
            if (statusLine.getStatusCode() != 200) {
                throw new IllegalStateException("TextExtractor got status code: " + statusLine.getStatusCode() + ", expected 200");
            }
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String string = EntityUtils.toString(responseEntity);
                System.out.println(string);
                JsonObject jsonObject = parser.parse(string).getAsJsonObject();
                JsonArray document = jsonObject.get("document").getAsJsonArray();
                return document.get(0).getAsJsonObject().get("content").getAsString();
            } else {
                throw new IllegalStateException("No entity");
            }
        }
    }
}
