package box;

import com.google.gson.JsonObject;

import java.io.IOException;

/**
 * @author Daniyar Itegulov
 */
public class BoxParser {
    public static BoxElement parse(JsonObject jsonObject, BoxAccount boxAccount) throws IOException {
        if (jsonObject.get("type").getAsString().equals("folder")) {
            return new BoxDirectory(jsonObject, boxAccount);
        } else {
            return new BoxFile(jsonObject, boxAccount);
        }
    }

    public static BoxElement parse(String json, BoxAccount boxAccount) throws IOException {
        return parse(SingletoneJsonParser.parser.parse(json).getAsJsonObject(), boxAccount);
    }
}
