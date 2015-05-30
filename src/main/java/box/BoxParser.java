package box;

import com.google.gson.JsonObject;

/**
 * @author Daniyar Itegulov
 */
public class BoxParser {
    public static BoxElement parse(JsonObject jsonObject) {
        if (jsonObject.get("type").getAsString().equals("folder")) {
            return new BoxDirectory(jsonObject);
        } else {
            return new BoxFile(jsonObject);
        }
    }

    public static BoxElement parse(String json) {
        return parse(SingletoneJsonParser.parser.parse(json).getAsJsonObject());
    }
}
