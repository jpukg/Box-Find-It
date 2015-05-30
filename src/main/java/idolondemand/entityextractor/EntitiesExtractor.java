package idolondemand.entityextractor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import idolondemand.Constants;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniyar Itegulov
 */
public class EntitiesExtractor {
    private static final String BASE_URL = "https://api.idolondemand.com/1/api/sync/extractentities/v1";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko)" +
            " Chrome/41.0.2228.0 Safari/537.36";

    public static Set<String> fetchByText(String text, EntityType... types) throws IOException {
        StringBuilder generated = new StringBuilder(BASE_URL).append("?apikey=")
                .append(Constants.API_KEY).append("&text=").append(URLEncoder.encode(text, "UTF-8"));
        return JSONParse(process(generated, types));
    }

    public static Set<String> fetchByURL(String url, EntityType... types) throws IOException {
        StringBuilder generated = new StringBuilder(BASE_URL).append("?apikey=")
                .append(Constants.API_KEY).append("&url=").append(URLEncoder.encode(url, "UTF-8"));
        return JSONParse(process(generated, types));
    }

    public static Set<String> fetchByFile(File file, EntityType... types) throws IOException {
        HttpPost request = new HttpPost(BASE_URL);
        request.addHeader("User-Agent", USER_AGENT);
        FileBody fileBody = new FileBody(file);
        StringBody apiKeyBody = new StringBody(Constants.API_KEY, ContentType.TEXT_PLAIN);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create()
                .addPart("file", fileBody)
                .addPart("apikey", apiKeyBody);
        for (EntityType entityType : types) {
            multipartEntityBuilder.addPart("entity_type",
                    new StringBody(entityType.getValue(), ContentType.TEXT_PLAIN));
        }
        HttpEntity entity = multipartEntityBuilder.build();
        request.setEntity(entity);
        HttpClient httpclient = HttpClientBuilder.create().build();
        try (CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(request)) {
            StatusLine statusLine = response.getStatusLine();
            System.out.println(statusLine);
            if (statusLine.getStatusCode() != 200) {
                throw new IllegalStateException("Got status code: " + statusLine.getStatusCode() + ", expected 200");
            }
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                return JSONParse(EntityUtils.toString(responseEntity));
            } else {
                throw new IllegalStateException("No entity");
            }
        }
    }

    private static String process(StringBuilder url, EntityType[] types) throws IOException {
        for (EntityType entityType : types) {
            url.append("&entity_type=").append(entityType.getValue());
        }
        System.out.println(url.toString());
        HttpGet request = new HttpGet(url.toString());
        request.addHeader("User-Agent", USER_AGENT);
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

    private static Set<String> JSONParse(String json) {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json).getAsJsonObject();
        JsonArray entities = object.getAsJsonArray("entities");
        Set<String> res = new HashSet<>();
        for (int i = 0; i < entities.size(); i++) {
            JsonObject jsonObject = entities.get(i).getAsJsonObject();
            res.add(jsonObject.get("normalized_text").getAsString());
        }
        return res;
    }

}
