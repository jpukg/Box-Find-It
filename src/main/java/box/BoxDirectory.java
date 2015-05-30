package box;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniyar Itegulov
 */
public class BoxDirectory implements BoxElement {
    private static final JsonParser parser = new JsonParser();

    private long id;
    private String name;
    private long size;
    private DateTime createdAt;
    private DateTime modifiedAt;
    private BoxAccount boxAccount;
    private List<BoxElement> elementList = new ArrayList<>();

    public BoxDirectory(JsonObject jsonObject, BoxAccount boxAccount) throws IOException {
        id = jsonObject.get("id").getAsLong();
        name = jsonObject.get("name").getAsString();
        size = jsonObject.get("size").getAsLong();
        JsonElement created = jsonObject.get("created_at");
        createdAt = !created.isJsonNull() ? ISODateTimeFormat.dateTimeNoMillis().parseDateTime(created.getAsString()) : null;
        JsonElement modified = jsonObject.get("modified_at");
        modifiedAt = !modified.isJsonNull() ? ISODateTimeFormat.dateTimeNoMillis().parseDateTime(modified.getAsString()) : null;
        JsonArray entities = jsonObject.get("item_collection").getAsJsonObject().get("entries").getAsJsonArray();
        this.boxAccount = boxAccount;
        for (JsonElement element : entities) {
            JsonObject elementObject = element.getAsJsonObject();
            if (elementObject.get("type").getAsString().equals("file")) {
                elementList.add(BoxParser.parse(boxAccount.getFile(elementObject.get("id").getAsLong()), boxAccount));
            } else {
                elementList.add(BoxParser.parse(boxAccount.list(elementObject.get("id").getAsLong()), boxAccount));
            }
        }
    }

    public BoxDirectory(String json, BoxAccount boxAccount) throws IOException {
        this(parser.parse(json).getAsJsonObject(), boxAccount);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getModifiedAt() {
        return modifiedAt;
    }

    public BoxAccount getBoxAccount() {
        return boxAccount;
    }

    public List<BoxElement> getElementList() {
        return elementList;
    }

    @Override
    public String toString() {
        return "BoxDirectory{" +
                "elementList=" + elementList +
                ", modifiedAt=" + modifiedAt +
                ", createdAt=" + createdAt +
                ", size=" + size +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
