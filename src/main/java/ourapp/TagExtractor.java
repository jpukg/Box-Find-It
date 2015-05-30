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
    private static final String INSERT_SQL =  "INSERT INTO tags VALUES(?,?,?)";

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



    public static Set<String> extract(BoxAccount boxAccount) throws IOException,
            URISyntaxException, SQLException, ClassNotFoundException {
        return extract(boxAccount.getRoot(), boxAccount);
    }

    public static Set<String> extract(BoxDirectory boxDirectory, BoxAccount boxAccount) throws IOException, SQLException {
        Set<String> result = new HashSet<>();
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
                            EntityType.COMPAINES_ENG, EntityType.COMPAINES_ENG);
                    result.addAll(tags);
                    String[] array = new String[tags.size()];
                    tags.toArray(array);
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL);
                    ps.setLong(1, file.getId());
                    ps.setDate(2, new Date(file.getModifiedAt().toDate().getTime()));
                    ps.setArray(3, connection.createArrayOf("text", array));
                    ps.executeUpdate();
                } else {
                    Date date = rs.getDate("loaded");
                    Array array = rs.getArray("tag");
                    String[] tags = (String[]) array.getArray();
                    Collections.addAll(result, tags);
                }
            } else {
                result.addAll(extract((BoxDirectory) element, boxAccount));
            }
        }
        connection.close();
        return result;
    }

    public static Set<String> findMatching(String string) throws SQLException {
        Connection connection = getConnection();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT tag FROM (SELECT unnest(tag) tag FROM tags) x WHERE tag LIKE '%" + string + "%'");
        Set<String> set = new HashSet<>();
        while (rs.next()) {
            String tag = rs.getString("tag");
            set.add(tag);
        }
        connection.close();
        return set;
    }

    public static Set<Long> findFileIds(String tag) throws SQLException {
        Connection connection = getConnection();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT DISTINCT file_id FROM tags WHERE '" + tag + "' = ANY(tag)");
        Set<Long> set = new HashSet<>();
        while (rs.next()) {
            long id = rs.getLong("file_id");
            set.add(id);
        }
        connection.close();
        return set;
    }
}
