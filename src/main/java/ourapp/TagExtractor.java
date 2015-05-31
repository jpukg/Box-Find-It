package ourapp;

import box.BoxAccount;
import box.BoxDirectory;
import box.BoxElement;
import box.BoxFile;
import idolondemand.entityextractor.EntitiesExtractor;
import idolondemand.entityextractor.EntityType;
import idolondemand.ocrdocument.OCRDocument;
import idolondemand.textextraction.TextExtractor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * @author Daniyar Itegulov
 */
public class TagExtractor {
    private static final String INSERT_SQL =  "INSERT INTO tags VALUES(?,?,?,?)";

    private static Connection getConnection() throws SQLException {
        URI dbUri;
        try {
            dbUri = new URI(System.getenv("DATABASE_URL"));
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid DATABASE_URL");
        }

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        int port = dbUri.getPort();

        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }



    public static Map<String, Integer> extract(BoxAccount boxAccount, long userId) throws IOException,
            URISyntaxException, SQLException, ClassNotFoundException {
        return extract(boxAccount.getRoot(), boxAccount, userId);
    }

    public static Map<String, Integer> extract(BoxDirectory boxDirectory, BoxAccount boxAccount, long userId) throws IOException, SQLException {
        Map<String, Integer> result = new HashMap<>();
        Connection connection = getConnection();
        for (BoxElement element : boxDirectory.getElementList()) {
            if (element instanceof BoxFile) {
                BoxFile file = (BoxFile) element;
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT loaded, tag FROM tags WHERE file_id = " + file.getId());
                if (!rs.next()) {
                    byte[] data = boxAccount.getFileContent(file.getId());
                    File f = Files.createTempFile("hackathonapp", "elitebox").toFile();
                    FileUtils.writeByteArrayToFile(f, data);
                    String stringData;
                    stringData = TextExtractor.fetchByFile(f);
                    if (stringData.equals("")) {
                        stringData = OCRDocument.fetchByFile(f);
                    }
                    FileUtils.writeStringToFile(f, stringData);
                    Set<String> tags = EntitiesExtractor.fetchByFile(f, EntityType.PEOPLE_ENG,
                            EntityType.COMPAINES_ENG, EntityType.COMPAINES_ENG, EntityType.ORGANIZATIONS,
                            EntityType.BANKACCOUNT, EntityType.DATE, EntityType.INTERNET, EntityType.NUMBER_PHONES,
                            EntityType.UNIVERSITY);
                    for (String tag : tags) {
                        result.putIfAbsent(tag, 0);
                        result.put(tag, result.get(tag) + 1);
                    }
                    String[] array = new String[tags.size()];
                    tags.toArray(array);
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL);
                    ps.setLong(1, file.getId());
                    ps.setDate(2, new Date(file.getModifiedAt().toDate().getTime()));
                    ps.setArray(3, connection.createArrayOf("text", array));
                    ps.setLong(4, userId);
                    ps.executeUpdate();
                } else {
                    Date date = rs.getDate("loaded");
                    Array array = rs.getArray("tag");
                    String[] tags = (String[]) array.getArray();
                    for (String tag : tags) {
                        result.putIfAbsent(tag, 0);
                        result.put(tag, result.get(tag) + 1);
                    }
                }
            } else {
                Map<String, Integer> tags = extract((BoxDirectory) element, boxAccount, userId);
                for (Map.Entry<String, Integer> tag : tags.entrySet()) {
                    result.putIfAbsent(tag.getKey(), 0);
                    result.put(tag.getKey(), result.get(tag.getKey()) + tag.getValue());
                }
            }
        }
        connection.close();
        return result;
    }

    public static Set<String> findMatching(String string, long userId) throws SQLException {
        Connection connection = getConnection();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT tag FROM (SELECT user_id, unnest(tag) tag FROM tags) x WHERE tag ILIKE '%" + string + "%' AND user_id = " + userId);
        Set<String> set = new HashSet<>();
        while (rs.next()) {
            String tag = rs.getString("tag");
            set.add(tag);
        }
        connection.close();
        return set;
    }

    public static Set<Long> findFileIds(String tag, long userId) throws SQLException {
        Connection connection = getConnection();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT DISTINCT file_id FROM tags WHERE '" + tag + "' = ANY(tag) AND user_id = " + userId);
        Set<Long> set = new HashSet<>();
        while (rs.next()) {
            long id = rs.getLong("file_id");
            set.add(id);
        }
        connection.close();
        return set;
    }
}
