package box;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
/**
 * @author Daniyar Itegulov
 */
public class BoxFile implements BoxElement {
    private static final JsonParser parser = new JsonParser();

    private long id;
    private String name;
    private long size;
    private DateTime createdAt;
    private DateTime modifiedAt;
    private BoxAccount boxAccount;

    public BoxFile(JsonObject jsonObject, BoxAccount boxAccount) {
        id = jsonObject.get("id").getAsLong();
        name = jsonObject.get("name").getAsString();
        size = jsonObject.get("size").getAsLong();
        JsonElement created = jsonObject.get("created_at");
        createdAt = !created.isJsonNull() ? ISODateTimeFormat.dateTime().parseDateTime(created.getAsString()) : null;
        JsonElement modified = jsonObject.get("modified_at");
        modifiedAt = !modified.isJsonNull() ? ISODateTimeFormat.dateTime().parseDateTime(modified.getAsString()) : null;
        this.boxAccount = boxAccount;
    }

    public BoxFile(String json, BoxAccount boxAccount) {
        this(parser.parse(json).getAsJsonObject(), boxAccount);
    }

    @Override
    public String toString() {
        return "BoxFile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
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
}
