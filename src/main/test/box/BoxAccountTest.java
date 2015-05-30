package box;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Daniyar Itegulov
 */
public class BoxAccountTest {

    @Test
    public void testList() throws Exception {
        BoxDirectory boxDirectory = new BoxDirectory("{\"type\":\"folder\",\"id\":\"0\",\"sequence_id\":null,\"etag\":null," +
                "\"name\":\"All Files\",\"created_at\":\"2015-05-30T03:06:04-07:00\",\"modified_at\":null,\"description\":\"\",\"size\":244," +
                "\"path_collection\":{\"total_count\":0,\"entries\":[]},\"created_by\":{\"type\":\"user\",\"id\":\"\"," +
                "\"name\":\"\",\"login\":\"\"},\"modified_by\":{\"type\":\"user\",\"id\":\"238673879\",\"name\":" +
                "\"Daniyar Itegulov\",\"login\":\"wibkwibk@gmail.com\"},\"trashed_at\":null,\"purged_at\":null," +
                "\"content_created_at\":null,\"content_modified_at\":null,\"owned_by\":{\"type\":\"user\",\"id" +
                "\":\"238673879\",\"name\":\"Daniyar Itegulov\",\"login\":\"wibkwibk@gmail.com\"},\"shared_link" +
                "\":null,\"folder_upload_email\":null,\"parent\":null,\"item_status\":\"active\",\"item_collection" +
                "\":{\"total_count\":1,\"entries\":[{\"type\":\"file\",\"id\":\"30789503399\",\"file_version\":{\"type" +
                "\":\"file_version\",\"id\":\"29678413691\",\"sha1\":\"dadcce00541bf6493bab31b347d6aad5caa7a740\"}," +
                "\"sequence_id\":\"0\",\"etag\":\"0\",\"sha1\":\"dadcce00541bf6493bab31b347d6aad5caa7a740\",\"name" +
                "\":\"obama.txt\"}],\"offset\":0,\"limit\":100,\"order\":[{\"by\":\"type\",\"direction\":\"ASC\"},{" +
                "\"by\":\"name\",\"direction\":\"ASC\"}]}} ", null);
    }
}